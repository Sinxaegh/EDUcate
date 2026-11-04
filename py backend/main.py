import os
import json
import sqlite3
from datetime import datetime
from starlette.applications import Starlette
from starlette.middleware import Middleware
from starlette.middleware.cors import CORSMiddleware
from starlette.requests import Request
from starlette.responses import JSONResponse
from starlette.routing import Route

DB_PATH = os.path.join(os.path.dirname(__file__), "educate.db")


def get_db():
    conn = sqlite3.connect(DB_PATH)
    conn.row_factory = sqlite3.Row
    return conn


def init_db():
    conn = get_db()
    cursor = conn.cursor()

    # Tabla usuarios
    cursor.execute("""
        CREATE TABLE IF NOT EXISTS usuarios (
            id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT NOT NULL,
            correo TEXT NOT NULL UNIQUE,
            pass TEXT NOT NULL,
            rol TEXT NOT NULL DEFAULT 'estudiante',
            creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        );
    """)

    # Tabla actividades_entregas
    cursor.execute("""
        CREATE TABLE IF NOT EXISTS actividades_entregas (
            id_entrega INTEGER PRIMARY KEY AUTOINCREMENT,
            id_estudiante INTEGER NOT NULL,
            id_actividad INTEGER NOT NULL,
            archivo_nombre TEXT,
            sello_tiempo TEXT,
            estado TEXT DEFAULT 'Pendiente'
        );
    """)

    # Tabla calificaciones
    cursor.execute("""
        CREATE TABLE IF NOT EXISTS calificaciones (
            id_calificacion INTEGER PRIMARY KEY AUTOINCREMENT,
            id_entrega INTEGER,
            id_estudiante INTEGER NOT NULL,
            actividad TEXT NOT NULL,
            puntos REAL,
            maximo REAL DEFAULT 20.0,
            retroalimentacion TEXT,
            estado TEXT DEFAULT 'Evaluada',
            fecha TEXT
        );
    """)

    # Tabla donaciones
    cursor.execute("""
        CREATE TABLE IF NOT EXISTS donaciones (
            id_donacion INTEGER PRIMARY KEY AUTOINCREMENT,
            monto REAL NOT NULL,
            correo TEXT,
            metodo TEXT,
            transaccion_id TEXT,
            fecha TEXT
        );
    """)

    # Tabla foro_posts
    cursor.execute("""
        CREATE TABLE IF NOT EXISTS foro_posts (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            id_curso INTEGER DEFAULT 1,
            author TEXT NOT NULL,
            author_role TEXT DEFAULT 'Estudiante',
            avatar_initials TEXT DEFAULT 'US',
            content TEXT NOT NULL,
            likes_count INTEGER DEFAULT 0,
            date TEXT
        );
    """)

    # Usuarios iniciales por defecto si la base de datos es nueva
    cursor.execute("SELECT COUNT(*) FROM usuarios;")
    if cursor.fetchone()[0] == 0:
        cursor.execute(
            "INSERT INTO usuarios (nombre, correo, pass, rol) VALUES (?, ?, ?, ?)",
            ("Ana López", "ana.lopez@universidad.edu", "123456", "estudiante")
        )
        cursor.execute(
            "INSERT INTO usuarios (nombre, correo, pass, rol) VALUES (?, ?, ?, ?)",
            ("Dra. Elena Ruiz", "elena.ruiz@universidad.edu", "123456", "profesor")
        )

    # Calificaciones iniciales por defecto si está vacía
    cursor.execute("SELECT COUNT(*) FROM calificaciones;")
    if cursor.fetchone()[0] == 0:
        cursor.execute(
            "INSERT INTO calificaciones (id_estudiante, actividad, puntos, maximo, estado) VALUES (?, ?, ?, ?, ?)",
            (1, "T1 - Conceptos básicos", 18.0, 20.0, "Evaluada")
        )
        cursor.execute(
            "INSERT INTO calificaciones (id_estudiante, actividad, puntos, maximo, estado) VALUES (?, ?, ?, ?, ?)",
            (1, "Cuestionario 1", 16.0, 20.0, "Evaluada")
        )
        cursor.execute(
            "INSERT INTO calificaciones (id_estudiante, actividad, puntos, maximo, estado) VALUES (?, ?, ?, ?, ?)",
            (1, "Foro 1 - Variables", 9.0, 10.0, "Evaluada")
        )

    conn.commit()
    conn.close()


# Inicializar SQLite al cargar el backend
init_db()


# ---------- Handlers Base ----------
async def index(request: Request):
    return JSONResponse({
        "status": "online",
        "app": "EDUcate Backend API (Base de datos SQLite local activa)",
        "version": "1.2.0",
    })


async def test_db(request: Request):
    try:
        conn = get_db()
        cursor = conn.cursor()
        cursor.execute("SELECT COUNT(*) FROM usuarios")
        total_usuarios = cursor.fetchone()[0]
        conn.close()
        return JSONResponse({
            "estatus": "ok",
            "mensaje": f"Conexión a base de datos SQLite activa correctamente. Usuarios guardados: {total_usuarios}"
        })
    except Exception as e:
        return JSONResponse({"detail": f"Error base de datos: {str(e)}"}, status_code=500)


# ---------- Autenticación y Registro ----------
async def registrar_usuario(request: Request):
    try:
        datos = await request.json()
    except Exception:
        return JSONResponse({"detail": "JSON inválido"}, status_code=400)

    nombre = (datos.get("nombre") or "").strip()
    correo = (datos.get("correo") or "").strip().lower()
    contrasena = datos.get("contrasena") or ""
    rol = (datos.get("rol") or "estudiante").strip().lower()

    if not nombre or not correo or not contrasena:
        return JSONResponse({"detail": "Nombre, correo y contraseña son obligatorios"}, status_code=400)

    try:
        conn = get_db()
        cursor = conn.cursor()

        # Comprobar si el correo ya existe
        cursor.execute("SELECT id_usuario FROM usuarios WHERE correo = ?", (correo,))
        if cursor.fetchone():
            conn.close()
            return JSONResponse({"detail": "Ya existe una cuenta con ese correo"}, status_code=409)

        # Insertar nuevo usuario
        cursor.execute(
            "INSERT INTO usuarios (nombre, correo, pass, rol) VALUES (?, ?, ?, ?)",
            (nombre, correo, contrasena, rol)
        )
        conn.commit()
        new_id = cursor.lastrowid
        conn.close()

        return JSONResponse({
            "id_usuario": new_id,
            "nombre": nombre,
            "correo": correo,
            "rol": rol,
            "token": f"bearer_token_{new_id}",
        })
    except Exception as e:
        return JSONResponse({"detail": f"Error al registrar usuario: {str(e)}"}, status_code=500)


async def iniciar_sesion(request: Request):
    try:
        datos = await request.json()
    except Exception:
        return JSONResponse({"detail": "JSON inválido"}, status_code=400)

    correo = (datos.get("correo") or "").strip().lower()
    contrasena = datos.get("contrasena") or ""

    if not correo or not contrasena:
        return JSONResponse({"detail": "Correo y contraseña son obligatorios"}, status_code=400)

    try:
        conn = get_db()
        cursor = conn.cursor()
        cursor.execute("SELECT * FROM usuarios WHERE correo = ?", (correo,))
        user = cursor.fetchone()
        conn.close()

        if not user:
            return JSONResponse({"detail": "Usuario no encontrado. Por favor regístrate."}, status_code=401)

        if user["pass"] != contrasena:
            return JSONResponse({"detail": "Contraseña incorrecta"}, status_code=401)

        return JSONResponse({
            "id_usuario": user["id_usuario"],
            "nombre": user["nombre"],
            "correo": user["correo"],
            "rol": user["rol"],
            "token": f"bearer_token_{user['id_usuario']}",
        })
    except Exception as e:
        return JSONResponse({"detail": f"Error al iniciar sesión: {str(e)}"}, status_code=500)


# ---------- Dashboard Estudiante ----------
async def obtener_dashboard_estudiante(request: Request):
    id_estudiante = int(request.path_params.get("id_estudiante", 1))

    conn = get_db()
    cursor = conn.cursor()
    cursor.execute("SELECT COUNT(*) FROM actividades_entregas WHERE id_estudiante = ? AND estado = 'Pendiente'", (id_estudiante,))
    row = cursor.fetchone()
    pendientes_count = row[0] if row else 0
    conn.close()

    return JSONResponse({
        "id_estudiante": id_estudiante,
        "resumen": {
            "cursos_activos": 1,
            "pendientes": pendientes_count if pendientes_count > 0 else 2,
            "promedio_actual": 86.0,
        },
        "cursos": [
            {
                "id_curso": 101,
                "nombre": "Estadística aplicada",
                "docente": "Dra. Elena Ruiz",
                "avance_porcentaje": 67
            }
        ],
        "avisos": [
            {"titulo": "Guía de la Tarea 2 disponible", "fecha": "Dra. Elena Ruiz · Hoy, 08:30"},
            {"titulo": "Tutoría de dudas hoy", "fecha": "Estadística aplicada · Sala virtual, 17:00"}
        ],
    })


# ---------- Entregas ----------
async def entregar_actividad(request: Request):
    try:
        form = await request.form()
    except Exception as e:
        return JSONResponse({"detail": f"Error multipart: {e}"}, status_code=400)

    id_estudiante = int(form.get("id_estudiante", 1))
    id_actividad = int(form.get("id_actividad", 2))
    archivo = form.get("archivo")

    nombre_archivo = getattr(archivo, "filename", "Informe_descriptivo.pdf")
    sello = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

    conn = get_db()
    cursor = conn.cursor()
    cursor.execute("""
        INSERT INTO actividades_entregas (id_estudiante, id_actividad, archivo_nombre, sello_tiempo, estado)
        VALUES (?, ?, ?, ?, 'Pendiente')
    """, (id_estudiante, id_actividad, nombre_archivo, sello))
    conn.commit()
    new_id = cursor.lastrowid
    conn.close()

    return JSONResponse({
        "id_entrega": new_id,
        "id_estudiante": id_estudiante,
        "id_actividad": id_actividad,
        "archivo_nombre": nombre_archivo,
        "sello_tiempo": sello,
        "estado": "Pendiente",
    })


# ---------- Calificaciones ----------
async def consultar_calificaciones(request: Request):
    id_estudiante = int(request.path_params.get("id_estudiante", 1))

    conn = get_db()
    cursor = conn.cursor()
    cursor.execute("SELECT actividad, puntos, maximo, estado FROM calificaciones WHERE id_estudiante = ?", (id_estudiante,))
    cals = cursor.fetchall()
    conn.close()

    calificaciones_list = []
    puntos_totales = 0.0
    valid_count = 0

    for c in cals:
        p = c["puntos"]
        m = c["maximo"] or 20.0
        calificaciones_list.append({
            "actividad": c["actividad"],
            "puntos": p,
            "maximo": m,
            "estado": c["estado"]
        })
        if p is not None:
            puntos_totales += p
            valid_count += 1

    if not calificaciones_list:
        calificaciones_list = [
            {"actividad": "T1 - Conceptos básicos", "puntos": 18.0, "maximo": 20.0, "estado": "Evaluada"},
            {"actividad": "Cuestionario 1", "puntos": 16.0, "maximo": 20.0, "estado": "Evaluada"},
            {"actividad": "Foro 1 - Variables", "puntos": 9.0, "maximo": 10.0, "estado": "Evaluada"},
            {"actividad": "T2 - Informe descriptivo", "puntos": None, "maximo": 20.0, "estado": "Pendiente"}
        ]
        promedio = 86.0
    else:
        promedio = round((puntos_totales / valid_count) * 5, 2) if valid_count > 0 else 0.0

    return JSONResponse({
        "id_estudiante": id_estudiante,
        "promedio_general": promedio,
        "calificaciones": calificaciones_list,
    })


# ---------- Panel Profesor ----------
async def panel_profesor(request: Request):
    id_profesor = int(request.path_params.get("id_profesor", 2))

    conn = get_db()
    cursor = conn.cursor()
    cursor.execute("SELECT nombre FROM usuarios WHERE id_usuario = ?", (id_profesor,))
    row = cursor.fetchone()
    docente = row["nombre"] if row else "Dra. Elena Ruiz"

    cursor.execute("""
        SELECT e.id_entrega, u.nombre as estudiante, e.sello_tiempo
        FROM actividades_entregas e
        LEFT JOIN usuarios u ON e.id_estudiante = u.id_usuario
        WHERE e.estado = 'Pendiente'
    """)
    rows = cursor.fetchall()

    pendientes = []
    for r in rows:
        pendientes.append({
            "id_entrega": r["id_entrega"],
            "estudiante": r["estudiante"] or "Estudiante",
            "actividad": "T2 - Informe descriptivo",
            "hora": r["sello_tiempo"] or "Reciente",
        })

    if not pendientes:
        pendientes = [
            {"id_entrega": 1, "estudiante": "Ana López", "actividad": "T2 - Informe descriptivo", "hora": "09:15"},
            {"id_entrega": 2, "estudiante": "Luis García", "actividad": "T2 - Informe descriptivo", "hora": "08:42"}
        ]

    cursor.execute("SELECT COUNT(*) FROM usuarios WHERE rol = 'estudiante'")
    alumnos_count = cursor.fetchone()[0]
    conn.close()

    return JSONResponse({
        "id_profesor": id_profesor,
        "docente": docente,
        "cursos_impartidos": 2,
        "alumnos_activos": alumnos_count or 120,
        "tareas_por_revisar": len(pendientes),
        "pendientes_revision": pendientes,
    })


async def calificar_entrega(request: Request):
    try:
        datos = await request.json()
    except Exception:
        return JSONResponse({"detail": "JSON inválido"}, status_code=400)

    id_entrega = datos.get("id_entrega", 1)
    puntuacion = float(datos.get("puntuacion", 0))
    retro = datos.get("retroalimentacion", "")

    conn = get_db()
    cursor = conn.cursor()
    cursor.execute("UPDATE actividades_entregas SET estado = 'Evaluada' WHERE id_entrega = ?", (id_entrega,))
    cursor.execute("""
        INSERT INTO calificaciones (id_entrega, id_estudiante, actividad, puntos, maximo, retroalimentacion, estado, fecha)
        VALUES (?, 1, 'T2 - Informe descriptivo', ?, 20.0, ?, 'Evaluada', ?)
    """, (id_entrega, puntuacion, retro, datetime.now().strftime("%Y-%m-%d %H:%M:%S")))
    conn.commit()
    conn.close()

    return JSONResponse({
        "mensaje": "Calificación guardada exitosamente",
        "id_entrega": id_entrega,
        "puntuacion": puntuacion,
        "estado": "Evaluada",
    })


# ---------- Donaciones ----------
async def procesar_donacion(request: Request):
    try:
        datos = await request.json()
    except Exception:
        return JSONResponse({"detail": "JSON inválido"}, status_code=400)

    monto = float(datos.get("monto", 0))
    correo = datos.get("correo", "")
    metodo = datos.get("metodo", "Tarjeta")
    transaccion_id = f"txn_educate_{int(datetime.now().timestamp())}"
    fecha_str = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

    conn = get_db()
    cursor = conn.cursor()
    cursor.execute("""
        INSERT INTO donaciones (monto, correo, metodo, transaccion_id, fecha)
        VALUES (?, ?, ?, ?, ?)
    """, (monto, correo, metodo, transaccion_id, fecha_str))
    conn.commit()
    conn.close()

    return JSONResponse({
        "estatus": "Exitoso",
        "mensaje": "Gracias por apoyar la educación gratuita bajo el ODS 17",
        "monto": monto,
        "correo": correo,
        "transaccion_id": transaccion_id,
    })


# ---------- Catálogo y Lecciones ----------
async def obtener_cursos_catalogo(request: Request):
    return JSONResponse([
        {
            "id_curso": 101,
            "title": "Estadística Aplicada",
            "instructor": "Dra. Elena Ruiz",
            "level": "3° Primaria / Básico",
            "duration": "8 semanas",
            "category": "Matemáticas",
            "is_free": True,
            "is_volunteer": True
        },
        {
            "id_curso": 102,
            "title": "Comprensión LECTURA",
            "instructor": "Prof. Marco Solís",
            "level": "4° Primaria",
            "duration": "6 semanas",
            "category": "Español",
            "is_free": True,
            "is_volunteer": True
        }
    ])


async def obtener_modulos_curso(request: Request):
    id_curso = int(request.path_params.get("id_curso", 1))
    return JSONResponse([
        {
            "id": 1,
            "title": "Semana 1 · Fundamentos",
            "statusText": "Completada · 3 recursos",
            "isCompleted": True,
            "isExpanded": False,
            "lessons": [
                {"id": 101, "title": "Introducción a la estadística", "type": "LECTURA", "subtitle": "Lectura · PDF · 8 páginas"},
                {"id": 102, "title": "Conceptos de variables", "type": "LECTURA", "subtitle": "Lectura · PDF · 10 páginas"},
                {"id": 103, "title": "Tarea 1 · Conceptos básicos", "type": "TAREA", "subtitle": "Entregada · Valor: 20 puntos"}
            ]
        },
        {
            "id": 2,
            "title": "Semana 2 · Análisis descriptivo",
            "statusText": "En curso · 12–18 de octubre",
            "isCompleted": False,
            "isExpanded": True,
            "lessons": [
                {"id": 201, "title": "Guía de estadística descriptiva", "type": "LECTURA", "subtitle": "Lectura · PDF · 12 páginas"},
                {"id": 202, "title": "Foro · Interpretar los datos", "type": "FORO", "subtitle": "8 aportes · Cierra el 18 oct, 18:00"},
                {"id": 203, "title": "Tarea 2 · Informe descriptivo", "type": "TAREA", "subtitle": "16 oct, 23:59 · Valor: 20 puntos", "isPending": True, "dueDate": "16 oct 2026, 23:59", "points": 20}
            ]
        }
    ])


async def obtener_leccion_detalle(request: Request):
    id_leccion = int(request.path_params.get("id_leccion", 201))
    return JSONResponse({
        "id": id_leccion,
        "title": "Guía de estadística descriptiva",
        "course": "Estadística aplicada · Módulo 2",
        "youtubeVideoId": "dQw4w9WgXcQ",
        "durationText": "15 min de lectura",
        "weekText": "Semana 2",
        "summaryText": "Aprende a resumir y presentar conjuntos de datos utilizando medidas numéricas y gráficos.",
        "transcriptText": "En esta lección exploraremos las medidas de tendencia central y dispersión.",
        "resources": [
            {"id": 1, "title": "Guía en PDF", "info": "PDF · 12 páginas", "size": "1,2 MB"},
            {"id": 2, "title": "Ejercicios de práctica", "info": "PDF · 4 páginas", "size": "580 KB"}
        ]
    })


# ---------- Foro ----------
async def obtener_foro(request: Request):
    id_curso = int(request.path_params.get("id_curso", 1))
    conn = get_db()
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM foro_posts WHERE id_curso = ? ORDER BY id DESC", (id_curso,))
    posts = cursor.fetchall()
    conn.close()

    result = []
    for p in posts:
        result.append({
            "id": p["id"],
            "author": p["author"],
            "authorRole": p["author_role"],
            "avatarInitials": p["avatar_initials"],
            "date": p["date"],
            "content": p["content"],
            "likesCount": p["likes_count"]
        })
    return JSONResponse(result)


async def publicar_foro(request: Request):
    try:
        datos = await request.json()
    except Exception:
        return JSONResponse({"detail": "JSON inválido"}, status_code=400)

    author = datos.get("author", "Usuario")
    author_role = datos.get("authorRole", "Estudiante")
    avatar_initials = datos.get("avatarInitials", "US")
    content = datos.get("content", "")
    id_curso = datos.get("id_curso", 1)
    fecha_str = "Hace un momento"

    conn = get_db()
    cursor = conn.cursor()
    cursor.execute("""
        INSERT INTO foro_posts (id_curso, author, author_role, avatar_initials, content, date)
        VALUES (?, ?, ?, ?, ?, ?)
    """, (id_curso, author, author_role, avatar_initials, content, fecha_str))
    conn.commit()
    new_id = cursor.lastrowid
    conn.close()

    return JSONResponse({
        "id": new_id,
        "author": author,
        "authorRole": author_role,
        "avatarInitials": avatar_initials,
        "date": fecha_str,
        "content": content,
        "likesCount": 0
    })


# ---------- Rutas API ----------
routes = [
    Route("/", index, methods=["GET"]),
    Route("/health", index, methods=["GET"]),
    Route("/api/v1/test-db", test_db, methods=["GET"]),
    Route("/api/v1/auth/login", iniciar_sesion, methods=["POST"]),
    Route("/api/v1/auth/register", registrar_usuario, methods=["POST"]),
    Route("/api/v1/cursos", obtener_cursos_catalogo, methods=["GET"]),
    Route("/api/v1/cursos/{id_curso:int}/modulos", obtener_modulos_curso, methods=["GET"]),
    Route("/api/v1/lecciones/{id_leccion:int}", obtener_leccion_detalle, methods=["GET"]),
    Route("/api/v1/foro/{id_curso:int}", obtener_foro, methods=["GET"]),
    Route("/api/v1/foro/publicar", publicar_foro, methods=["POST"]),
    Route("/api/v1/cursos/estudiante/{id_estudiante:int}", obtener_dashboard_estudiante, methods=["GET"]),
    Route("/api/v1/actividades/entregar", entregar_actividad, methods=["POST"]),
    Route("/api/v1/calificaciones/{id_estudiante:int}", consultar_calificaciones, methods=["GET"]),
    Route("/api/v1/profesor/panel/{id_profesor:int}", panel_profesor, methods=["GET"]),
    Route("/api/v1/profesor/calificar", calificar_entrega, methods=["POST"]),
    Route("/api/v1/donaciones/procesar", procesar_donacion, methods=["POST"]),
]

middleware = [
    Middleware(
        CORSMiddleware,
        allow_origins=["*"],
        allow_credentials=True,
        allow_methods=["*"],
        allow_headers=["*"],
    )
]

app = Starlette(debug=True, routes=routes, middleware=middleware)

if __name__ == "__main__":
    import uvicorn
    print("=" * 66)
    print("  EDUcate Backend API v1.2.0 (Base de datos SQLite local)")
    print("  Local:    http://127.0.0.1:8000")
    print("  Emulador: http://10.0.2.2:8000")
    print("=" * 66)
    uvicorn.run(app, host="0.0.0.0", port=8000)
