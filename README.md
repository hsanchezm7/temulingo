<!-- PROJECT SHIELDS -->
![Java Version](https://img.shields.io/badge/java-17-orange)

<!-- PROJECT LOGO -->
<br />
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


<!-- GETTING STARTED -->
## Inicio rápido

### Requisitos

Para ejecutar el proyecto es necesario tener instalado:

* [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html): Se requiere Java SE 17 o superior.
* [Maven](https://maven.apache.org/download.cgi): Herramienta de gestión de dependencias y construcción del proyecto.

### Instalación

1. Clonar el repositorio
   ```sh
   $ git clone https://github.com/hsanchezm7/temulingo
   ```
2. Entrar al directorio
   ```sh
   $ cd temulingo/
   ```
3. Compilar y generar ejecutable
   ```sh
   $ mvn clean package
   ```
4. Ejecutar el `.jar` en la carpeta `target/` del directorio
   ```sh
   $ java -jar target/temulingo-{version}-jar-with-dependencies.jar
   ```
<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- USAGE EXAMPLES -->
## Uso

### Requisitos y casos de uso

Con aprobación del tutor del proyecto, se ha optado por llevar un seguimiento de los casos de uso usando la funcionalidad de _issues_ junto con el uso _pull requests_ de Github. Se ha definido un [documento raíz](https://github.com/hsanchezm7/temulingo/issues/9) que recoge todos los casos de uso contemplados de la aplicación.

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


<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- ACKNOWLEDGMENTS -->


<p align="right">(<a href="#readme-top">back to top</a>)</p>
