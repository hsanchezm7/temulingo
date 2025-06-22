<a id="readme-top"></a>

<!-- PROJECT SHIELDS -->
![Java Version](https://img.shields.io/badge/java-17-orange) ![Maven](https://img.shields.io/badge/build-Maven-blue) ![License](https://img.shields.io/badge/license-MIT-green)

<!-- PROJECT LOGO -->
<div align="center">
  <img src="temulingo/src/main/resources/media/banner.png" alt="Logo Temulingo" width="640" height="160">

  <h3 align="center">Temulingo</h3>

  <p align="center">
    Prácticas de Procesos de Desarrollo de Software
    <br />
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Contenidos</summary>
  <ol>
    <li>
      <a href="#acerca-del-proyecto">Acerca del proyecto</a>
      <ul>
        <li><a href="#miembros-del-equipo">Miembros del equipo</a></li>
      </ul>
    </li>
    <li>
      <a href="#inicio-rápido">Inicio rápido</a>
      <ul>
        <li><a href="#requisitos">Requisitos</a></li>
        <li><a href="#instalación">Instalación</a></li>
      </ul>
    </li>
    <li><a href="#uso">Uso</a></li>
    <li><a href="#documentación">Documentación</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## Acerca del proyecto

**Temulingo** es plataforma de aprendizaje desarrollada en Java 17 que permite realizar cursos con preguntas interactivas, 

Basada en soluciones reales como [Duolingo](https://duolingo.com/), Temulingo ofrece al usuario numerosas funcionalidades:

- Importar cursos a partir de archivos JSON/YAML.
- Realizar cursos interactivos con diferentes estrategias de aprendizaje.
- Guardar el progreso de un curso para continuarlo más adelante.
- Ver estadísticas de progreso.

Este proyecto forma parte de las prácticas de la asignatura de **Procesos de Desarrollo de Software** de la Universidad de Murcia del curso 24/25.

### Miembros del equipo

<table align="center">
  <tr>
    <td align="center"><a href="https://github.com/hsanchezm7"><img src="https://avatars.githubusercontent.com/u/61797804" width="140px;" alt=""/><br /><sub><b>Hugo Sánchez Martínez</b></sub><br /><sub>@hsanchezm7</sub></a></td>
    <td align="center"><a href="https://github.com/jsalinaspardo"><img src="https://avatars.githubusercontent.com/u/167551603" width="140px;" alt=""/><br /><sub><b>José Salinas Pardo</b></sub><br /><sub>@jsalinaspardo</ sub></a></td>
  </tr>
</table>

<p align="right">(<a href="#readme-top">volver arriba</a>)</p>

<!-- GETTING STARTED -->
## Inicio rápido

### Requisitos

Para ejecutar el proyecto es necesario tener instalado:

- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html): Se requiere Java SE 17 o superior.
- [Maven](https://maven.apache.org/download.cgi): Herramienta de gestión de dependencias y construcción del proyecto.

### Instalación

1. Clonar el repositorio
   ```sh
   $ git clone https://github.com/hsanchezm7/temulingo
   ```
2. Entrar al directorio
   ```sh
   $ cd temulingo/
   ```

3. Ejecutar
   ```sh
   $ java -jar temulingo-1.0.0.jar
   ```

También puedes compilar el código fuente de la siguiente forma:

   ```sh
   $ mvn clean package
   $ java -jar target/temulingo-{version}-jar-with-dependencies.jar
   ```

<p align="right">(<a href="#readme-top">volver arriba</a>)</p>

<!-- USAGE EXAMPLES -->
## Uso

### Requisitos y casos de uso

Con aprobación del tutor del proyecto, se ha optado por llevar un seguimiento de los casos de uso usando la funcionalidad de _issues_ junto con el uso _pull requests_ de Github. Se ha definido un [documento raíz](https://github.com/hsanchezm7/temulingo/issues/9) que recoge todos los casos de uso contemplados de la aplicación.

### Manual de usuario

Se ha definido un [manual de usuario](docs/manualUsuario.md) básico que recoge las principales funcionalidades de la aplicación.

### Estructura del proyecto

```
temulingo/           # Raíz
├── design           #
│   └── model        #
└── temulingo        # Proyecto Java
    ├── data         # Persistencia
    ├── src          # Código fuente de 
    │   ├── main
    │   └── test     # Suite de tests de Temulingo
    └── target
```

### Testing

> [!WARNING]
> Es probable que los tests no se ejecuten correctamente si no se utiliza la misma versión Java que el proyecto, Java 17.

Se ha creado una _suite_ de tests usando las siguientes herramientas:

- [JUnit5](https://junit.org/junit5/): Framework para pruebas unitarias en Java.
- [Mockito](https://mockito.org/): Librería para crear mocks y facilitar pruebas aisladas.
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/): Para simplificar la ejecución y configuración de tests.

  - [Maven Surefire JUnit5 Tree Reporter](https://github.com/fabriciorby/maven-surefire-junit5-tree-reporter): Mejora visualmente la salida de tests en consola con estructura jerárquica.

Para ejecutar los tests **se recomienda usar Maven**. Se pueden ejecutar con el comando:

```sh
   $ mvn test
```

También se puede ver en formato `.html` un _report_ de los tests ejecutados. El archivo se encuentra en `temulingo/target/site/surefire.html`.

```sh
   $ mvn site
```
<p align="right">(<a href="#readme-top">volver arriba</a>)</p>

<!-- ACKNOWLEDGMENTS -->

