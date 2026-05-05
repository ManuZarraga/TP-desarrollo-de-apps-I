# 📱 Trabajo Práctico - Desarrollo de Aplicación Mobile

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