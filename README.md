# Recipe App

Una aplicación de recetas de cocina desarrollada en Android con Kotlin y Jetpack Compose. Esta app permite a los usuarios iniciar sesión, ver, agregar y modificar recetas, así como gestionar su perfil y preferencias.

## Descripción

**Recipe App** es una aplicación moderna que integra diversas tecnologías y patrones de diseño para ofrecer una experiencia fluida y responsiva. Entre sus características principales se encuentran:

- **Autenticación de usuarios:** Pantalla de login con validación.
- **Gestión de recetas:** Listado de recetas, detalle de cada receta, formulario para agregar/editar recetas y filtros (por favoritos, tiempo de preparación, búsqueda, etc.).
- **Gestión de cuenta:** Permite editar el perfil del usuario, cambiar la imagen de cuenta y cerrar sesión.
- **Pantalla Splash:** Verificación del estado de sesión y navegación inicial.
- **Diseño con Jetpack Compose:** UI moderna y responsiva.
- **Persistencia local:** Uso de Room para la base de datos de recetas y DataStore para almacenar las preferencias del usuario.
- **Arquitectura MVVM:** Uso de ViewModels, StateFlow y Coroutines para una gestión asíncrona y reactiva del estado de la aplicación.

## Características

- **Inicio de Sesión:**  
  - Validación de credenciales ("info@mobile.dev" / "mobile123") **con estas podrás iniciar sesión**.
  - Gestión de estado de sesión mediante DataStore.

- **Recetas:**  
  - Listado de recetas almacenadas en una base de datos local (Room).
  - Filtros y búsqueda en el listado.
  - Detalle de cada receta con información completa (título, descripción, tiempo de preparación, imagen y estado de favorito).
  - Funcionalidad para marcar o desmarcar una receta como favorita.

- **Formulario de Receta:**  
  - Creación y edición de recetas.
  - Validación de campos (título, descripción, tiempo de preparación).
  - Selección y guardado de imágenes de recetas (copiado a almacenamiento interno).

- **Cuenta de Usuario:**  
  - Visualización del perfil (nombre e imagen).
  - Actualización de la imagen de perfil a través del selector de imágenes.
  - Opción para cerrar sesión.

- **Navegación:**  
  - Implementada con Jetpack Navigation para una navegación fluida entre pantallas (Splash, Login, Listado, Detalle, Formulario, Cuenta).

## Arquitectura y Tecnologías

- **Lenguaje:** Kotlin
- **UI:** Jetpack Compose
- **Persistencia:**  
  - **Room:** Base de datos local para almacenar las recetas.
  - **DataStore:** Manejo de preferencias de usuario.
- **Navegación:** Jetpack Navigation Compose.
- **Patrón Arquitectónico:** MVVM (ViewModel, StateFlow, Coroutines)


## Estructura del Proyecto

La estructura del proyecto está organizada en varios paquetes para separar las distintas responsabilidades:

- **data**
  - **model:** Definición de modelos de datos (por ejemplo, `Recipe`).
  - **local/preferences:** Implementación de DataStore para guardar preferencias de usuario (`DataStoreUserPrefs` y `UserPreferences`).
  - **local/room:** Configuración de la base de datos local con Room, DAOs y entidades (`RecipeEntity`, `RecipeDao`, `AppDataBase`).
  - **local/room/repository:** Repositorios para interactuar con la base de datos local (`RecipeRepository`).

- **presentation**
  - **login:** Pantalla y lógica para el inicio de sesión (`LoginScreen`, `LoginViewModel`, `LocalLoginRepository`).
  - **mainFlow/recipes:** Módulos para el flujo principal de recetas:
    - **recipeList:** Listado de recetas.
    - **recipeProfile:** Detalle de cada receta.
    - **recipeForm:** Formulario para agregar/editar recetas.
    - **account:** Gestión del perfil y cuenta de usuario.
  - **splash:** Pantalla inicial de verificación de sesión (`SplashScreen`, `SplashViewModel`).
  - **reusableScreens:** Componentes reutilizables (por ejemplo, pantalla de carga).

- **navigation:** Configuración y definición de rutas de navegación entre pantallas (`AppNavigation`).

- **ui/theme:** Temas y estilos de la aplicación (`RecipeAppTheme`).

## Instalación y Ejecución

### Prerrequisitos

- [Android Studio](https://developer.android.com/studio) instalado.
- SDK de Android configurado (recomendado Android 12 o superior).

### Pasos para ejecutar el proyecto

1. **Clonar el repositorio:**

```
git clone https://github.com/Bamo0507/RecipeApp.git
```

