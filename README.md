# 📱 Trabajo Práctico - Desarrollo de Aplicación Mobile

## 🚀 Tecnologías Elegidas

### 📱 Desarrollo Mobile
- **Kotlin**  
Lenguaje principal utilizado para el desarrollo de la aplicación Android.

- **Jetpack Compose**  
Toolkit moderno de Android para la construcción de interfaces declarativas.

- **Material Design 3**  
Sistema de diseño utilizado para garantizar una interfaz moderna y accesible.

---

### 🗄️ Persistencia Local
- **Room **  
Librería de persistencia local utilizada como capa de abstracción sobre SQLite, facilitando el manejo de datos y entidades dentro de una arquitectura MVVM.

- **SQLite**  
Motor de base de datos local utilizado para almacenamiento persistente en el dispositivo y soporte de funcionamiento offline.

---

### ☁️ Backend / Cloud
- **Supabase**  
Plataforma BAAS (Backend-as-a-Service) utilizada para autenticación, persistencia remota de datos y sincronización en la nube mediante APIs REST y base de datos PostgreSQL.

---

### 🔗 Recursos de Figma
- [Pantallas y Componentes](https://www.figma.com/design/zAkLWTYiKDMIMoppcn3R2G/Recomiendo-App?node-id=73-281&t=rs3CCQTXnKjGCcn9-1)
- [Prototipo Interactivo](https://www.figma.com/proto/zAkLWTYiKDMIMoppcn3R2G/Recomiendo-App?node-id=73-282&p=f&viewport=241%2C286%2C0.17&t=VrK4877mYcLebbkp-1&scaling=scale-down&content-scaling=fixed&starting-point-node-id=73%3A282&page-id=73%3A281)

---

### 🔗 Tablero de Seguimiento
- [Notion](https://www.notion.so/2f4d080c7e4740b1b8da9e0b65155359?v=48de3b0c47ee433c9d28d3dc16bf4213&source=copy_link)

---

## 📌 Casos de Uso

---

# Caso de Uso 1: Realizar una Reseña

## 👤 Actor Principal
Usuario registrado


## 📝 Descripción
El usuario registra su experiencia real tras recibir un pedido de delivery, permitiendo comparar la expectativa del producto con el resultado recibido mediante evidencia fotográfica y una calificación.


## ✅ Precondiciones
- El usuario debe tener la aplicación instalada.
- El usuario debe encontrarse registrado e iniciar sesión en la aplicación.
- El usuario debe haber recibido un pedido de delivery previamente.


## 🔄 Flujo Principal

1. El usuario abre la aplicación y selecciona el botón central de cámara para crear una nueva reseña.
2. El usuario toma una fotografía del producto recibido.
3. El usuario busca y selecciona el local y producto correspondiente  
   _(Ejemplo: “McDonald's - Doble Cheeseburger”)_.
4. El usuario escribe una descripción de su experiencia y del producto recibido.
5. El usuario asigna una calificación de 1 a 5 estrellas.
6. Opcionalmente, el usuario agrega fotografías adicionales del producto.
7. El usuario publica la reseña.


## 📌 Postcondiciones
- La reseña queda almacenada en el sistema.
- La publicación se vuelve visible para la comunidad.
- La reseña aparece en el perfil del usuario autor.

---

# Caso de Uso 2: Ver una Reseña

## 👤 Actor Principal
Usuario


## 📝 Descripción
El usuario consulta experiencias previas de otros consumidores sobre un producto específico antes de realizar una compra en una aplicación de delivery externa, con el objetivo de tomar una decisión más informada.


## ✅ Precondiciones
- El usuario tiene la aplicación instalada.
- El usuario se encuentra evaluando opciones de comida o productos para realizar un pedido.


## 🔄 Flujo Principal

1. El usuario ingresa a la aplicación y utiliza la barra de búsqueda para filtrar productos por categoría o buscar una marca específica.
2. El usuario selecciona un producto desde la lista de resultados o desde la sección de productos destacados.
3. El sistema muestra las reseñas realizadas por otros usuarios sobre dicho producto.
4. El usuario selecciona una reseña específica para visualizar su detalle completo.
5. Opcionalmente, el usuario puede indicar que la reseña le resultó útil mediante la opción “Me gusta”.


## 📌 Postcondiciones
- El usuario obtiene información visual y opiniones reales sobre el producto consultado.
- El usuario puede tomar una decisión de compra más informada y confiable antes de realizar el pedido.

----

## Consignas del trabajo:

## 📌 Encuadre General

El presente trabajo práctico propone el desarrollo, desde el diseño hasta la puesta en producción, de una aplicación para dispositivos móviles con persistencia en un backend.

Para ello, se deberán aplicar buenas prácticas de:
- Arquitectura
- Diseño
- Pruebas
- Integración
- Documentación

La consigna es **abierta**, por lo que cada equipo deberá:
- Elegir un dominio real
- Justificar el problema a resolver
- Diseñar la solución propuesta

---

## 📅 Entregas

El trabajo contará con **4 entregas**, de las cuales **2 son obligatorias**:

### 🟢 H1 - Obligatorio
- Prototipo en Figma
- Flujo de pantallas
- Repositorio inicializado
- Tablero de seguimiento
- Al menos 2 casos de uso
- APK Demo (puede ser mockeado)
- Diagrama de arquitectura inicial (alto nivel)
- Descripción de tecnologías elegidas

### 🔵 H2 - Obligatorio
- Feature set completo
- Pruebas
- Métricas
- APK Release Candidate (RC)
- Documentación final
- Defensa del proyecto

---

## 👥 Gestión del Proyecto

- Equipo de **3 estudiantes**
- Roles (fijos o rotativos):
    - Product Owner
    - Tech Lead
    - UX/UI
    - Backend Lead
    - QA / DevOps
- Seguimiento de backlog e historias de usuario (con acceso de la cátedra)

---

## 🎯 Objetivos de Aprendizaje

1. Aplicar diseño centrado en el usuario (investigación, prototipado y validación)
2. Implementar arquitecturas modernas (MVVM, capas, repositorios)
3. Utilizar una metodología de desarrollo consistente
4. Integrar persistencia local y backend (REST / GraphQL)
5. Implementar pruebas y métricas de calidad
6. Elaborar documentación técnica y de usuario
7. Presentar y defender la solución

---

## ⚙️ Requisitos Funcionales

La aplicación debe incluir:

- Flujo de **onboarding inicial**
- Al menos **3 flujos de pantallas**
- Al menos **1 CRUD completo** del dominio principal
- **Autenticación**:
    - Email/contraseña y/o
    - Login federado (Cloud Services)
- **Modo offline** (con funcionalidad mínima)
- Al menos **1 listado con Card Views**
- Uso de:
    - Sensor y/o
    - Dispositivo de captura (audio o cámara)
- Accesibilidad:
    - Tamaños de fuente escalables
    - `contentDescription`
    - Tema oscuro
    - (Opcional) internacionalización

---

## 🚀 Requisitos No Funcionales

- **Cold start < 2.5 segundos**  
  (Dispositivo: Google Pixel 9 Pro, 4GB RAM, 2 cores)
- Scroll fluido (**> 54 FPS**)
- Manejo adecuado de errores de conectividad
- (Opcional) Cola de tareas offline
- Correcta elección de **API Level mínimo**

---

## 🏗️ Requisitos Arquitectónicos

- Lenguaje:
    - Kotlin (Android) o
    - React Native  
      *(Justificar elección)*

- Arquitectura:
    - MVVM + Repository

- UI:
    - Jetpack Compose

- Networking:
    - Retrofit2 / Gson o similares

- Accesibilidad:
    - Material 3
    - Dark Mode
    - Dynamic Color

---

## 🎨 UI/UX y Experiencia de Usuario

Se debe entregar:

- Mapa de navegación
- Design System básico:
    - Tipografías
    - Colores
    - Componentes
- Prototipo navegable

### Requisitos

- Aplicar **Material Design 3**
- Aplicar **Heurísticas de Nielsen** (con checklist)
- Buenas prácticas de accesibilidad
- Wireframes de alta fidelidad en **Figma**

---

## 🔄 Ciclo de Desarrollo y Colaboración

- Repositorio en GitHub o GitLab  
  *(público o privado con acceso a la cátedra)*

- Estrategia de ramas:
    - Trunk-based o
    - GitFlow

- Evidenciar trabajo en equipo

### Diagramas requeridos

- Diagrama de arquitectura (alto nivel)
- Diagramas de secuencia (mínimo 2 del flujo principal)

---

## 🎤 Presentación Final

- ⏱️ Demo en vivo:
    - 10 min demo
    - 5 min preguntas

- 📊 Pitch:
    - Problema
    - Usuarios
    - Métricas
    - Arquitectura
    - Decisiones clave
    - Aprendizajes

- 📱 Benchmark:
    - Comparación con 1 app similar (pros / contras)

- 📦 Entrega:
    - Release Candidate (RC)
    - Documentación completa

---

## ✅ Criterios de Aprobación

- Puntaje mínimo: **60/100**
- Cumplimiento de hitos **H1 y H2**
- Repositorios accesibles
- Builds reproducibles
- Demo funcional
