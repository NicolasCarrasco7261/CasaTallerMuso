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
    contenido TEXT NOT NULL,
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
    contenido TEXT NOT NULL,
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

INSERT INTO cursos (
    id,
    nombre,
    descripcion,
    contenido,
    imagen_storage_key,
    precio,
    cupos,
    activo,
    creado_en
) VALUES 
(
    'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a51',
    'Taller de Cerámica Básica',
    'Aprende las técnicas fundamentales del modelado en arcilla.',
    $$En este taller introductorio conocerás el comportamiento de la arcilla. Aprenderás a preparar el material y mantener una humedad adecuada.\nPracticarás técnicas de pellizco, planchas y churros desde cero. Revisaremos herramientas básicas y su uso correcto en cada etapa.\nTrabajarás piezas utilitarias simples como cuencos y pequeños platos. También veremos nociones de textura, unión de piezas y secado.\nProfundizaremos en el uso de engobes y acabados iniciales. Habrá ejercicios de observación para mejorar proporción y forma.\nConversaremos sobre referentes de cerámica utilitaria y artística. Cada sesión incluirá tiempo de práctica personal acompañada.\nEl proceso incluye una guía para evitar grietas y deformaciones. Al finalizar tendrás una base sólida para seguir creando en cerámica.$$,
    '18df6819-5f02-41f9-8aed-2e1687f80ddb',
    45000,
    10,
    TRUE,
    CURRENT_TIMESTAMP
),
(
    'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a52',
    'Curso de Pintura Óleo',
    'Explora el mundo del color y la textura con la técnica del óleo.',
    $$Este curso propone una aproximación práctica a la pintura al óleo. Estudiarás mezclas de color, temperatura y armonías cromáticas.\nAprenderás a preparar soportes y elegir pinceles según el efecto. Ejercitaremos capas, veladuras y construcción gradual de volumen.\nSe revisará composición para naturalezas muertas y escenas simples. También veremos cómo controlar tiempos de secado y médiums.\nTrabajaremos luces, sombras y transición de planos con mayor precisión. Habrá análisis de obras para comprender decisiones de color y materia.\nPracticarás bocetos previos antes de avanzar a la pieza final. Se fomentará una observación paciente y una pincelada más consciente.\nRecibirás orientación para desarrollar una obra personal paso a paso. La experiencia está pensada para quienes buscan soltura y criterio visual.$$,
    '91384f44-9963-416b-b944-7939fa4bfe7d',
    55000,
    8,
    TRUE,
    CURRENT_TIMESTAMP
),
(
    'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a53',
    'Taller de Fotografía Digital',
    'Captura momentos inolvidables aprendiendo el uso manual de tu cámara.',
    $$A lo largo del taller comprenderás el funcionamiento de tu cámara. Trabajaremos apertura, velocidad e ISO con ejercicios guiados.\nAprenderás a exponer correctamente en distintas condiciones de luz. Exploraremos encuadre, foco y profundidad de campo con intención.\nHabrá salidas prácticas para retrato, detalle y fotografía urbana. También revisaremos fundamentos de edición y selección de imágenes.\nSe propondrán ejercicios para narrar una historia en serie fotográfica. Conversaremos sobre el uso del color y el blanco y negro.\nPracticarás lectura de luz natural y apoyo con iluminación simple. Cada clase incluirá revisión colectiva de resultados y aprendizajes.\nCada participante desarrollará una mirada propia con acompañamiento. El objetivo es ganar seguridad técnica y expresiva al fotografiar.$$,
    'e43b07e5-7ed4-4a91-a2da-c84658e7f974',
    40000,
    12,
    TRUE,
    CURRENT_TIMESTAMP
),
(
    'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a54',
    'Curso de Guitarra Acústica',
    'Domina los acordes y ritmos básicos de la guitarra.',
    $$Este curso entrega una base clara para iniciarse en guitarra acústica. Aprenderás postura, afinación y cuidado básico del instrumento.\nTrabajaremos acordes abiertos, cambios fluidos y patrones rítmicos. Practicarás acompañamiento en canciones sencillas y progresivas.\nTambién veremos lectura básica de cifrado y diagramas de acordes. Se incluirán ejercicios para coordinación, mano derecha y tempo.\nHabrá trabajo de escucha para reconocer compases y estructuras simples. Exploraremos dinámicas de rasgueo para dar más intención al sonido.\nCada participante avanzará con repertorio acorde a su nivel. Se reforzará la constancia de estudio con rutinas cortas y efectivas.\nCada clase busca fortalecer confianza y musicalidad en conjunto. Al terminar podrás acompañar repertorio inicial de forma autónoma.$$,
    '1b467017-91f3-4df7-b88f-c43181c02d3f',
    35000,
    6,
    TRUE,
    CURRENT_TIMESTAMP
),
(
    'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a55',
    'Taller de Escritura Creativa',
    'Desbloquea tu imaginación y comienza a escribir tus propias historias.',
    $$En este espacio exploraremos recursos para activar la imaginación. Trabajarás ejercicios breves de observación, voz y punto de vista.\nAprenderás a construir personajes con motivaciones y conflicto. Revisaremos escenas, diálogos y ritmo narrativo en textos cortos.\nTambién habrá lectura compartida y retroalimentación respetuosa. Se propondrán disparadores para cuento, crónica y microficción.\nProfundizaremos en atmósferas, detalles sensoriales y tono narrativo. Habrá momentos para reescritura y edición guiada de borradores.\nLeeremos fragmentos de autoras y autores como referencia de trabajo. Se cuidará un ambiente de confianza para probar nuevas ideas.\nEl taller prioriza la práctica constante por sobre la teoría abstracta. Cada participante saldrá con materiales propios para seguir escribiendo.$$,
    '4aceffc6-7c3c-48af-acef-793d7a9f9146',
    30000,
    15,
    TRUE,
    CURRENT_TIMESTAMP
),
(
    'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a56',
    'Curso de Yoga y Meditación',
    'Encuentra el equilibrio físico y mental a través del yoga.',
    $$El curso integra movimiento consciente, respiración y atención plena. Comenzaremos con secuencias accesibles para todos los niveles.\nAprenderás alineación básica para posturas de pie, suelo y descanso. Se incorporarán técnicas respiratorias para regular energía y enfoque.\nLa meditación guiada acompañará cada sesión de manera progresiva. También revisaremos hábitos simples para sostener la práctica diaria.\nHabrá énfasis en movilidad suave, estabilidad y descanso consciente. Exploraremos adaptaciones para respetar necesidades y ritmos diversos.\nCada encuentro cerrará con un momento breve de integración y pausa. Se entregarán sugerencias para practicar en casa sin exigencia excesiva.\nEl énfasis estará en escuchar el cuerpo sin competir ni exigirlo. Buscamos cultivar bienestar físico, calma mental y presencia cotidiana.$$,
    '18df6819-5f02-41f9-8aed-2e1687f80ddb',
    32000,
    20,
    TRUE,
    CURRENT_TIMESTAMP
),
(
    'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a57',
    'Taller de Encuadernación',
    'Crea tus propios cuadernos y libros de forma artesanal.',
    $$Aprenderás principios básicos de la encuadernación hecha a mano. Trabajaremos plegado, perforado y costuras simples y decorativas.\nConocerás papeles, cartones, telas y adhesivos según cada proyecto. Realizarás cuadernos funcionales con terminaciones cuidadas y firmes.\nTambién veremos cómo resolver tapas blandas y tapas rígidas. Habrá atención especial al orden de armado y la precisión manual.\nExploraremos formatos, proporciones y decisiones de diseño de portada. Se revisarán detalles de terminación para piezas más durables.\nCada participante desarrollará al menos un proyecto completamente armado. También conversaremos sobre herramientas caseras y alternativas accesibles.\nEl taller combina oficio, diseño y posibilidades de personalización. Te llevarás piezas terminadas y criterios para seguir experimentando.$$,
    '91384f44-9963-416b-b944-7939fa4bfe7d',
    38000,
    8,
    TRUE,
    CURRENT_TIMESTAMP
),
(
    'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a58',
    'Curso de Cocina Vegana',
    'Aprende recetas deliciosas y saludables basadas en plantas.',
    $$Este curso propone una cocina vegetal sabrosa, nutritiva y accesible. Aprenderás técnicas base para legumbres, verduras y cereales.\nPrepararemos platos cotidianos y opciones para ocasiones especiales. Revisaremos aliños, texturas y combinaciones para lograr equilibrio.\nTambién abordaremos reemplazos comunes en recetas tradicionales. Se hablará de organización en cocina y aprovechamiento de ingredientes.\nHabrá preparación de salsas, fondos y acompañamientos versátiles. Exploraremos métodos de cocción para resaltar sabor y textura.\nConversaremos sobre compras, conservación y planificación semanal. Cada sesión incluirá degustación y ajustes según resultados del grupo.\nCada sesión incluye preparación práctica y consejos de presentación. La idea es ampliar tu repertorio con platos simples y creativos.$$,
    'e43b07e5-7ed4-4a91-a2da-c84658e7f974',
    50000,
    10,
    TRUE,
    CURRENT_TIMESTAMP
),
(
    'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a59',
    'Taller de Teatro para Jóvenes',
    'Desarrolla tus habilidades expresivas y de actuación.',
    $$El taller invita a explorar el cuerpo, la voz y la presencia escénica. A través de juegos teatrales se fortalecerá la confianza grupal.\nTrabajaremos improvisación, escucha activa y construcción de escenas. Habrá ejercicios de emoción, ritmo y desplazamiento en el espacio.\nTambién se abordará la creación colectiva desde ideas de los participantes. El proceso prioriza la expresión auténtica y el trabajo colaborativo.\nSe practicarán entradas, salidas y uso del espacio con intención. Habrá dinámicas para desarrollar imaginación, reacción y juego dramático.\nCada sesión abrirá un espacio para proponer escenas breves. También se reforzará la escucha y el cuidado dentro del grupo.\nCada encuentro suma herramientas para comunicar con mayor libertad. Cerraremos con una muestra breve del recorrido realizado en clase.$$,
    '1b467017-91f3-4df7-b88f-c43181c02d3f',
    28000,
    15,
    TRUE,
    CURRENT_TIMESTAMP
),
(
    'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a60',
    'Curso de Escultura en Madera',
    'Técnicas de tallado y modelado en diferentes tipos de madera.',
    $$En este curso conocerás el trabajo escultórico aplicado a la madera. Aprenderás sobre vetas, durezas y selección del material adecuado.\nRevisaremos herramientas manuales y normas básicas de seguridad. Practicarás desbaste, modelado y terminaciones en piezas pequeñas.\nTambién veremos cómo trasladar una idea desde boceto a volumen. Se pondrá atención a proporción, textura y lectura de la forma.\nHabrá ejercicios para comprender la relación entre luz y volumen. Exploraremos referentes de talla tradicional y enfoques contemporáneos.\nCada participante desarrollará una pieza con seguimiento progresivo. Se revisarán opciones de lijado, sellado y terminación superficial.\nEl acompañamiento será gradual para respetar el ritmo de aprendizaje. Al final contarás con una pieza propia y fundamentos del oficio.$$,
    '4aceffc6-7c3c-48af-acef-793d7a9f9146',
    60000,
    5,
    TRUE,
    CURRENT_TIMESTAMP
);

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

INSERT INTO eventos (
    id,
    nombre,
    descripcion,
    contenido,
    imagen_storage_key,
    precio,
    cupos,
    activo,
    creado_en
) VALUES 
(
    '01eebc99-9c0b-4ef8-bb6d-6bb9bd380a71',
    'Concierto de Cuerdas Invernal',
    'Una noche mágica con los mejores exponentes de la música de cámara.',
    $$Este concierto reunirá un repertorio íntimo y cuidadosamente seleccionado. Participarán intérpretes con trayectoria en música de cámara nacional.\nLa velada recorrerá obras clásicas y arreglos contemporáneos sensibles. El formato busca una escucha cercana y una experiencia envolvente.\nAntes de cada bloque habrá una breve mediación sobre las piezas. El público podrá apreciar matices, diálogos y contrastes sonoros.\nTambién habrá una introducción para contextualizar a las y los intérpretes. La programación está pensada para transitar distintos climas sonoros.\nSe cuidará especialmente la calidad de escucha dentro de la sala. Habrá un cierre conjunto con una obra de gran intensidad expresiva.\nSe recomienda llegar con anticipación para ingreso y acomodación. Será una instancia especial para disfrutar la música en invierno.$$,
    '1b467017-91f3-4df7-b88f-c43181c02d3f',
    15000,
    50,
    TRUE,
    CURRENT_TIMESTAMP
),
(
    '01eebc99-9c0b-4ef8-bb6d-6bb9bd380a72',
    'Exposición de Arte Local',
    'Muestra colectiva de artistas de la región.',
    $$La exposición presenta una selección diversa de obras contemporáneas. Participan creadoras y creadores de distintas disciplinas visuales.\nSe podrán ver pinturas, grabados, fotografías y piezas mixtas. Cada sala propone cruces entre memoria, territorio e identidad.\nEl recorrido invita a descubrir lenguajes y búsquedas personales. Habrá fichas de obra para profundizar en procesos y referencias.\nSe destacarán trayectorias emergentes junto a nombres ya consolidados. La museografía favorecerá un recorrido claro y pausado.\nHabrá mediación para grupos que quieran profundizar en la visita. El montaje busca poner en diálogo materiales, escalas y sensibilidades.\nLa muestra está pensada para visitantes habituales y nuevos públicos. Es una oportunidad para acercarse a la escena artística regional.$$,
    '4aceffc6-7c3c-48af-acef-793d7a9f9146',
    10000,
    100,
    TRUE,
    CURRENT_TIMESTAMP
),
(
    '01eebc99-9c0b-4ef8-bb6d-6bb9bd380a73',
    'Noche de Jazz y Poesía',
    'Encuentro cultural con música en vivo y lectura de poemas.',
    $$La jornada combinará música improvisada con lecturas poéticas en vivo. El ensamble de jazz dialogará con distintas voces invitadas.\nSe alternarán piezas instrumentales y textos de autoras y autores. El ambiente estará preparado para una escucha atenta y cercana.\nHabrá momentos de cruce entre palabra, ritmo y silencios compartidos. La programación busca generar un encuentro cálido y participativo.\nCada bloque propondrá una atmósfera distinta para la escucha. Se cuidará la cercanía entre escenario y público durante la velada.\nHabrá una breve presentación de quienes participan en el encuentro. El ritmo general buscará equilibrio entre intensidad y contemplación.\nEs ideal para quienes disfrutan propuestas escénicas sensibles. Una noche para dejarse llevar por sonoridades y lenguaje.$$,
    '18df6819-5f02-41f9-8aed-2e1687f80ddb',
    8000,
    40,
    TRUE,
    CURRENT_TIMESTAMP
),
(
    '01eebc99-9c0b-4ef8-bb6d-6bb9bd380a74',
    'Feria de Diseño Independiente',
    'Descubre productos únicos de creadores locales.',
    $$La feria reunirá proyectos de diseño con identidad y producción local. Encontrarás ilustración, objetos, vestuario, papelería y accesorios.\nCada expositor compartirá piezas originales y series de autor. También habrá espacios para conversar sobre materiales y procesos.\nLa instancia promueve el comercio justo y la circulación creativa. Es un panorama abierto para familias, coleccionistas y curiosos.\nSe priorizará una distribución cómoda para recorrer cada puesto. Habrá emprendimientos con propuestas utilitarias y también experimentales.\nPodrás conocer procesos de fabricación y motivaciones detrás de cada marca. La jornada incluirá momentos para descubrir lanzamientos y ediciones especiales.\nPodrás recorrer con calma, descubrir novedades y apoyar emprendimientos. Una jornada pensada para conectar con el diseño independiente.$$,
    '91384f44-9963-416b-b944-7939fa4bfe7d',
    6000,
    200,
    TRUE,
    CURRENT_TIMESTAMP
),
(
    '01eebc99-9c0b-4ef8-bb6d-6bb9bd380a75',
    'Charla sobre Patrimonio Histórico',
    'Recorrido visual por la historia de nuestra ciudad.',
    $$La charla ofrecerá una mirada accesible al patrimonio de la ciudad. Se revisarán hitos urbanos, oficios y relatos de distintas épocas.\nEl encuentro incluirá fotografías, documentos y material comparativo. La exposición buscará conectar historia local con vida cotidiana.\nHabrá espacio para preguntas, recuerdos y conversación con asistentes. Se abordarán desafíos actuales de conservación y puesta en valor.\nTambién se comentarán transformaciones del paisaje urbano en el tiempo. El material visual ayudará a reconocer lugares y memorias compartidas.\nSe invitará al público a vincular los contenidos con su experiencia. La conversación buscará abrir nuevas preguntas sobre identidad local.\nEs una actividad ideal para quienes quieren conocer su entorno. Un recorrido visual para comprender mejor nuestra memoria colectiva.$$,
    'e43b07e5-7ed4-4a91-a2da-c84658e7f974',
    9000,
    60,
    TRUE,
    CURRENT_TIMESTAMP
),
(
    '01eebc99-9c0b-4ef8-bb6d-6bb9bd380a76',
    'Cine Foro: Cine Chileno',
    'Proyección de película y conversatorio con el director.',
    $$La actividad contempla la exhibición de una película chilena reciente. Luego de la proyección se abrirá un diálogo con el director invitado.\nSe comentarán decisiones narrativas, visuales y de producción. El público podrá compartir preguntas y lecturas de la obra.\nLa conversación buscará acercar el proceso creativo del cine nacional. Será una instancia formativa y a la vez abierta a todo público.\nSe abordarán temas de montaje, dirección de actores y escritura. Habrá una breve presentación inicial para contextualizar la proyección.\nEl intercambio posterior buscará fomentar una mirada crítica y cercana. La actividad está pensada para amantes del cine y público general.\nRecomendamos revisar la clasificación etaria antes de asistir. Una oportunidad para ver cine y conversar sobre sus resonancias.$$,
    '1b467017-91f3-4df7-b88f-c43181c02d3f',
    3000,
    45,
    TRUE,
    CURRENT_TIMESTAMP
),
(
    '01eebc99-9c0b-4ef8-bb6d-6bb9bd380a77',
    'Taller Abierto de Grabado',
    'Demostración en vivo y participación del público.',
    $$Este taller abierto mostrará técnicas básicas de grabado en acción. Las y los mediadores explicarán materiales, matrices y herramientas.\nHabrá demostraciones paso a paso con ejemplos claros y cercanos. Parte del público podrá participar en ejercicios simples guiados.\nSe pondrá énfasis en el proceso de impresión y sus variaciones. También se compartirán cuidados de seguridad y recomendaciones.\nHabrá piezas de referencia para observar distintos resultados posibles. Se comentarán diferencias entre técnicas y soportes de impresión.\nLa actividad está pensada para personas curiosas sin experiencia previa. El formato permitirá mirar de cerca el trabajo manual del taller.\nLa actividad busca acercar el oficio a personas sin experiencia. Una invitación a conocer el grabado desde la práctica directa.$$,
    '4aceffc6-7c3c-48af-acef-793d7a9f9146',
    0,
    30,
    TRUE,
    CURRENT_TIMESTAMP
),
(
    '01eebc99-9c0b-4ef8-bb6d-6bb9bd380a78',
    'Festival de Danza Contemporánea',
    'Presentación de diversas compañías de danza.',
    $$El festival reunirá propuestas coreográficas de distintas compañías. Cada presentación explorará lenguajes corporales y puestas singulares.\nLa programación incluirá obras breves y montajes de mayor duración. Habrá diversidad de estilos, generaciones y enfoques creativos.\nEntre funciones se compartirán contextos sobre cada agrupación. El evento busca ampliar públicos y fortalecer redes de danza.\nSe propondrá una experiencia continua entre escena, pausa y recorrido. Algunas obras dialogarán con música en vivo y recursos visuales.\nEl programa destacará miradas emergentes junto a trayectorias consolidadas. Habrá instancias breves para conocer los ejes de cada propuesta.\nSe recomienda revisar horarios para asistir a más de una obra. Una celebración del movimiento, la escena y la creación colectiva.$$,
    '18df6819-5f02-41f9-8aed-2e1687f80ddb',
    12000,
    80,
    TRUE,
    CURRENT_TIMESTAMP
),
(
    '01eebc99-9c0b-4ef8-bb6d-6bb9bd380a79',
    'Lanzamiento de Libro Local',
    'Presentación de la nueva obra de un autor regional.',
    $$El lanzamiento presentará una nueva publicación de autoría regional. Durante la actividad se comentarán temas, motivaciones y proceso.\nHabrá lectura de fragmentos seleccionados por el propio autor. Personas invitadas compartirán impresiones y claves de lectura.\nEl encuentro busca celebrar la circulación de la literatura local. También habrá espacio para preguntas y firma de ejemplares.\nSe pondrá en valor el trabajo editorial que acompaña la publicación. Habrá una conversación cercana sobre escritura, lectura y territorio.\nEl formato favorecerá el intercambio entre autor y asistentes. Se espera una jornada íntima para escuchar y compartir impresiones.\nEs una instancia cercana para conocer la obra desde su origen. Una invitación a encontrarse con la escritura y su comunidad.$$,
    '91384f44-9963-416b-b944-7939fa4bfe7d',
    7000,
    50,
    TRUE,
    CURRENT_TIMESTAMP
),
(
    '01eebc99-9c0b-4ef8-bb6d-6bb9bd380a80',
    'Peña Folclórica Familiar',
    'Música, baile y comida tradicional en un ambiente familiar.',
    $$La peña ofrecerá una tarde festiva con música y baile en vivo. Participarán agrupaciones locales con repertorio folclórico variado.\nHabrá espacios para compartir danzas tradicionales con el público. Además se contará con oferta gastronómica típica para la jornada.\nEl ambiente está pensado para familias y personas de todas las edades. Se fomentará una participación cercana, alegre y comunitaria.\nHabrá momentos para escuchar, bailar y recorrer la propuesta completa. La programación buscará combinar repertorio conocido y nuevos cruces.\nSe cuidará un entorno acogedor para quienes asisten por primera vez. También será una oportunidad para compartir tradiciones entre generaciones.\nRecomendamos asistir con tiempo para recorrer y acomodarse. Será una celebración de tradiciones, encuentro y cultura popular.$$,
    'e43b07e5-7ed4-4a91-a2da-c84658e7f974',
    5000,
    150,
    TRUE,
    CURRENT_TIMESTAMP
);

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
