# Sistema de Gestión de Taller Mecánico

Proyecto de Algorítmica II · Grupo 4 · UNMSM
Taller especializado en el Toyota Corolla Cross 2.0 CVT.

## Cómo abrirlo
1. Clonar el repositorio con GitHub Desktop.
2. En NetBeans: File → Open Project y elegir la carpeta clonada.
3. Clase principal: principal.Main

## Estructura
- src/modelo: clases del dominio
- src/gestores: clases intermedias (arreglos y CRUD)
- src/persistencia: lectura y escritura de archivos
- src/vista: interfaz gráfica
- docs: entregable y diagramas
- datos: archivos generados por el programa

## Requisitos y versiones

Todos los integrantes deben usar estas versiones para evitar errores de compilación.

| Herramienta | Versión |
|---|---|
| JDK | 26 |
| Apache NetBeans | 30 — https://netbeans.apache.org/download/nb30/ |

El proyecto está configurado con `javac.source=26` y `javac.target=26`
(archivo `nbproject/project.properties`). No cambiarlo sin avisar al equipo.

### Configurar NetBeans con el JDK 26
1. Tools → Java Platforms: debe aparecer un JDK 26. Si no aparece, usar
   Add Platform… y elegir la carpeta donde está instalado.
2. Clic derecho en el proyecto → Properties → Libraries → Java Platform: JDK 26.
3. Pulsar Run (F6): debe imprimir "Sistema de Gestion de Taller Mecanico".

No se guíen por `javac -version` en CMD: depende del PATH de cada equipo.

## Cómo unirse al proyecto
1. Aceptar la invitación de colaborador que llega al correo.
2. GitHub Desktop → File → Clone repository… → pestaña GitHub.com → elegir Sistema_de_Gestion_de_Taller_Mecanico y una carpeta local sin espacios ni OneDrive.
3. NetBeans → File → Open Project… → seleccionar la carpeta clonada (se reconoce por el ícono de proyecto) → Open Project.
4. Probar con Run (F6): debe imprimir el mensaje del Main.

## Forma de trabajo
- Antes de empezar: Fetch origin y, si hay cambios, Pull origin.
- Cada integrante trabaja en su propia rama (Current Branch → New Branch), por ejemplo a-cliente-vehiculo. Al terminar un avance: commit, Push y Create Pull Request hacia main.
- Dos personas no deben editar el mismo archivo a la vez. Los archivos .form de las ventanas generan conflictos difíciles de resolver.
- Commits pequeños y con mensaje claro (por ejemplo: Agrega CRUD de GestorClientes).

## Reparto de clases
| Integrante | Clases |
|------------|--------|
|   Malon    | Cliente, Vehiculo, GestorClientes, GestorVehiculos |
|     B      | ItemCatalogo, Repuesto, Servicio, Catalogo |
|     C      | OrdenTrabajo, EstadoOrden, Diagnostico, ItemCotizacion, GestorOrdenes |
|     D      | Factura, HistorialFacturas, GestorArchivos (persistencia) |
|     E      | Paquete vista (ventanas) y Main |
