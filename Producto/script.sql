DROP TABLE IF EXISTS inscripciones_eventos CASCADE;
DROP TABLE IF EXISTS inscripciones_cursos CASCADE;
DROP TABLE IF EXISTS horarios_eventos CASCADE;
DROP TABLE IF EXISTS horarios_cursos CASCADE;
DROP TABLE IF EXISTS eventos CASCADE;
DROP TABLE IF EXISTS cursos CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;
DROP TABLE IF EXISTS detalles_usuario CASCADE;
DROP TABLE IF EXISTS ubicaciones_usuarios CASCADE;
DROP TABLE IF EXISTS roles_usuario CASCADE;

-- Tablas

CREATE TABLE roles_usuario (
    id UUID PRIMARY KEY,
    tipo_rol VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE ubicaciones_usuarios (
    id UUID PRIMARY KEY,
    region VARCHAR(50) NOT NULL,
    direccion VARCHAR(255) NOT NULL
);

CREATE TABLE detalles_usuario (
    id UUID PRIMARY KEY,
    numero_telefonico VARCHAR(16),
    fecha_nacimiento DATE,
    genero VARCHAR(50),
    ubicacion_usuario_id UUID REFERENCES ubicaciones_usuarios(id) ON DELETE CASCADE
);

CREATE TABLE usuarios (
    id UUID PRIMARY KEY,
    nombre VARCHAR(48) NOT NULL,
    apellido VARCHAR(48) NOT NULL,
    correo VARCHAR(320) NOT NULL UNIQUE,
    clave_hash VARCHAR(64) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    tipo_usuario_id UUID REFERENCES roles_usuario(id),
    detalle_usuario_id UUID REFERENCES detalles_usuario(id) ON DELETE CASCADE
);

CREATE TABLE cursos (
    id UUID PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT NOT NULL,
    imagen_storage_key VARCHAR(255) NOT NULL,
    precio INTEGER NOT NULL,
    cupos INTEGER NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE eventos (
    id UUID PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT NOT NULL,
    imagen_storage_key VARCHAR(255) NOT NULL,
    precio INTEGER NOT NULL,
    cupos INTEGER NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE horarios_cursos (
    id UUID PRIMARY KEY,
    dia_de_semana VARCHAR(50) NOT NULL,
    hora_desde TIME NOT NULL,
    hora_hasta TIME NOT NULL,
    curso_id UUID NOT NULL REFERENCES cursos(id) ON DELETE CASCADE
);

CREATE TABLE horarios_eventos (
    id UUID PRIMARY KEY,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    evento_id UUID NOT NULL REFERENCES eventos(id) ON DELETE CASCADE
);

CREATE TABLE inscripciones_cursos (
    id UUID PRIMARY KEY,
    usuario_id UUID NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    curso_id UUID NOT NULL REFERENCES cursos(id) ON DELETE CASCADE
);

CREATE TABLE inscripciones_eventos (
    id UUID PRIMARY KEY,
    usuario_id UUID NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    evento_id UUID NOT NULL REFERENCES eventos(id) ON DELETE CASCADE
);

-- Insertar datos

INSERT INTO roles_usuario (id, tipo_rol) VALUES 
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'ADMIN'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'CLIENTE');

INSERT INTO cursos (id, nombre, descripcion, imagen_storage_key, precio, cupos, activo, creado_en) VALUES 
('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a51', 'Taller de Cerámica Básica', 'Aprende las técnicas fundamentales del modelado en arcilla.', '18df6819-5f02-41f9-8aed-2e1687f80ddb', 45000, 10, TRUE, CURRENT_TIMESTAMP),
('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a52', 'Curso de Pintura Óleo', 'Explora el mundo del color y la textura con la técnica del óleo.', '91384f44-9963-416b-b944-7939fa4bfe7d', 55000, 8, TRUE, CURRENT_TIMESTAMP),
('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a53', 'Taller de Fotografía Digital', 'Captura momentos inolvidables aprendiendo el uso manual de tu cámara.', 'e43b07e5-7ed4-4a91-a2da-c84658e7f974', 40000, 12, TRUE, CURRENT_TIMESTAMP),
('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a54', 'Curso de Guitarra Acústica', 'Domina los acordes y ritmos básicos de la guitarra.', '1b467017-91f3-4df7-b88f-c43181c02d3f', 35000, 6, TRUE, CURRENT_TIMESTAMP),
('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a55', 'Taller de Escritura Creativa', 'Desbloquea tu imaginación y comienza a escribir tus propias historias.', '4aceffc6-7c3c-48af-acef-793d7a9f9146', 30000, 15, TRUE, CURRENT_TIMESTAMP),
('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a56', 'Curso de Yoga y Meditación', 'Encuentra el equilibrio físico y mental a través del yoga.', '18df6819-5f02-41f9-8aed-2e1687f80ddb', 32000, 20, TRUE, CURRENT_TIMESTAMP),
('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a57', 'Taller de Encuadernación', 'Crea tus propios cuadernos y libros de forma artesanal.', '91384f44-9963-416b-b944-7939fa4bfe7d', 38000, 8, TRUE, CURRENT_TIMESTAMP),
('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a58', 'Curso de Cocina Vegana', 'Aprende recetas deliciosas y saludables basadas en plantas.', 'e43b07e5-7ed4-4a91-a2da-c84658e7f974', 50000, 10, TRUE, CURRENT_TIMESTAMP),
('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a59', 'Taller de Teatro para Jóvenes', 'Desarrolla tus habilidades expresivas y de actuación.', '1b467017-91f3-4df7-b88f-c43181c02d3f', 28000, 15, TRUE, CURRENT_TIMESTAMP),
('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a60', 'Curso de Escultura en Madera', 'Técnicas de tallado y modelado en diferentes tipos de madera.', '4aceffc6-7c3c-48af-acef-793d7a9f9146', 60000, 5, TRUE, CURRENT_TIMESTAMP);

INSERT INTO horarios_cursos (id, dia_de_semana, hora_desde, hora_hasta, curso_id) VALUES 
('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a61', 'LUNES', '10:00:00', '12:00:00', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a51'),
('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a62', 'MIERCOLES', '10:00:00', '12:00:00', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a51'),
('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a63', 'MARTES', '15:00:00', '18:00:00', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a52'),
('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a64', 'SABADO', '09:00:00', '13:00:00', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a53'),
('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a65', 'JUEVES', '18:00:00', '20:00:00', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a54'),
('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a66', 'VIERNES', '17:00:00', '19:00:00', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a55'),
('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a67', 'MARTES', '08:00:00', '09:30:00', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a56'),
('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a68', 'JUEVES', '08:00:00', '09:30:00', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a56'),
('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a69', 'SABADO', '15:00:00', '18:00:00', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a57'),
('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a70', 'LUNES', '19:00:00', '21:30:00', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a58'),
('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a71', 'MIERCOLES', '16:00:00', '18:00:00', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a59'),
('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a72', 'VIERNES', '10:00:00', '14:00:00', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a60');

INSERT INTO eventos (id, nombre, descripcion, imagen_storage_key, precio, cupos, activo, creado_en) VALUES 
('01eebc99-9c0b-4ef8-bb6d-6bb9bd380a71', 'Concierto de Cuerdas Invernal', 'Una noche mágica con los mejores exponentes de la música de cámara.', '1b467017-91f3-4df7-b88f-c43181c02d3f', 15000, 50, TRUE, CURRENT_TIMESTAMP),
('01eebc99-9c0b-4ef8-bb6d-6bb9bd380a72', 'Exposición de Arte Local', 'Muestra colectiva de artistas de la región.', '4aceffc6-7c3c-48af-acef-793d7a9f9146', 0, 100, TRUE, CURRENT_TIMESTAMP),
('01eebc99-9c0b-4ef8-bb6d-6bb9bd380a73', 'Noche de Jazz y Poesía', 'Encuentro cultural con música en vivo y lectura de poemas.', '18df6819-5f02-41f9-8aed-2e1687f80ddb', 8000, 40, TRUE, CURRENT_TIMESTAMP),
('01eebc99-9c0b-4ef8-bb6d-6bb9bd380a74', 'Feria de Diseño Independiente', 'Descubre productos únicos de creadores locales.', '91384f44-9963-416b-b944-7939fa4bfe7d', 0, 200, TRUE, CURRENT_TIMESTAMP),
('01eebc99-9c0b-4ef8-bb6d-6bb9bd380a75', 'Charla sobre Patrimonio Histórico', 'Recorrido visual por la historia de nuestra ciudad.', 'e43b07e5-7ed4-4a91-a2da-c84658e7f974', 0, 60, TRUE, CURRENT_TIMESTAMP),
('01eebc99-9c0b-4ef8-bb6d-6bb9bd380a76', 'Cine Foro: Cine Chileno', 'Proyección de película y conversatorio con el director.', '1b467017-91f3-4df7-b88f-c43181c02d3f', 3000, 45, TRUE, CURRENT_TIMESTAMP),
('01eebc99-9c0b-4ef8-bb6d-6bb9bd380a77', 'Taller Abierto de Grabado', 'Demostración en vivo y participación del público.', '4aceffc6-7c3c-48af-acef-793d7a9f9146', 0, 30, TRUE, CURRENT_TIMESTAMP),
('01eebc99-9c0b-4ef8-bb6d-6bb9bd380a78', 'Festival de Danza Contemporánea', 'Presentación de diversas compañías de danza.', '18df6819-5f02-41f9-8aed-2e1687f80ddb', 12000, 80, TRUE, CURRENT_TIMESTAMP),
('01eebc99-9c0b-4ef8-bb6d-6bb9bd380a79', 'Lanzamiento de Libro Local', 'Presentación de la nueva obra de un autor regional.', '91384f44-9963-416b-b944-7939fa4bfe7d', 0, 50, TRUE, CURRENT_TIMESTAMP),
('01eebc99-9c0b-4ef8-bb6d-6bb9bd380a80', 'Peña Folclórica Familiar', 'Música, baile y comida tradicional en un ambiente familiar.', 'e43b07e5-7ed4-4a91-a2da-c84658e7f974', 5000, 150, TRUE, CURRENT_TIMESTAMP);

INSERT INTO horarios_eventos (id, fecha, hora, evento_id) VALUES 
('02eebc99-9c0b-4ef8-bb6d-6bb9bd380a81', '2024-07-15', '19:30:00', '01eebc99-9c0b-4ef8-bb6d-6bb9bd380a71'),
('02eebc99-9c0b-4ef8-bb6d-6bb9bd380a82', '2024-08-20', '11:00:00', '01eebc99-9c0b-4ef8-bb6d-6bb9bd380a72'),
('02eebc99-9c0b-4ef8-bb6d-6bb9bd380a83', '2024-07-28', '21:00:00', '01eebc99-9c0b-4ef8-bb6d-6bb9bd380a73'),
('02eebc99-9c0b-4ef8-bb6d-6bb9bd380a84', '2024-09-05', '10:00:00', '01eebc99-9c0b-4ef8-bb6d-6bb9bd380a74'),
('02eebc99-9c0b-4ef8-bb6d-6bb9bd380a85', '2024-08-10', '18:30:00', '01eebc99-9c0b-4ef8-bb6d-6bb9bd380a75'),
('02eebc99-9c0b-4ef8-bb6d-6bb9bd380a86', '2024-07-22', '19:00:00', '01eebc99-9c0b-4ef8-bb6d-6bb9bd380a76'),
('02eebc99-9c0b-4ef8-bb6d-6bb9bd380a87', '2024-08-15', '16:00:00', '01eebc99-9c0b-4ef8-bb6d-6bb9bd380a77'),
('02eebc99-9c0b-4ef8-bb6d-6bb9bd380a88', '2024-09-12', '20:00:00', '01eebc99-9c0b-4ef8-bb6d-6bb9bd380a78'),
('02eebc99-9c0b-4ef8-bb6d-6bb9bd380a89', '2024-08-05', '18:00:00', '01eebc99-9c0b-4ef8-bb6d-6bb9bd380a79'),
('02eebc99-9c0b-4ef8-bb6d-6bb9bd380a90', '2024-09-18', '13:00:00', '01eebc99-9c0b-4ef8-bb6d-6bb9bd380a80');
