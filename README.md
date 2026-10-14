# TecnoStore — Sistema de ventas de celulares

Aplicación de **consola en Java** que automatiza el control de **catálogo de celulares, clientes, ventas y reportes** de TecnoStore, una tienda minorista de teléfonos celulares. Reemplaza el manejo manual en hojas de cálculo por un sistema con base de datos, validaciones y reportes.

**Autor:** Daniel Castillo — Grupo S2

---

## Funcionalidades

- **Celulares:** registrar, actualizar, eliminar y listar (marca, modelo, precio, stock, sistema operativo y gama).
- **Clientes:** registrar, actualizar, eliminar y listar, con validación de correo e identificación única.
- **Ventas:** seleccionar un cliente y uno o varios celulares, calcular el total con **IVA del 19 %**, descontar el stock y guardar todo en la base de datos con JDBC.
- **Reportes (con Stream API):** celulares con stock bajo (menos de 5), top 3 de celulares más vendidos y ventas totales por mes.
- **Archivo `reporte_ventas.txt`:** resumen de todas las ventas realizadas.
- **Inicio de sesión por rol:** administrador, vendedor y cliente, cada uno con su propio menú.
- **Gestión de personal:** el administrador registra y elimina vendedores; solo el **administrador principal** puede crear o eliminar administradores.

## Tecnologías

| Herramienta | Uso |
|---|---|
| Java 21 | Lenguaje (el `pom.xml` compila con nivel 21) |
| Maven | Gestión del proyecto y dependencias |
| MariaDB | Base de datos |
| MariaDB Connector/J 3.4.1 | Driver JDBC |
| NetBeans | IDE de desarrollo |

## Arquitectura

El proyecto sigue el patrón **Modelo – Vista – Controlador**, con una capa de servicio y una de persistencia:

```
Vista  →  Controlador  →  Servicio (Gestor)  →  Persistencia (DAO)  →  Base de datos
                     ↘          ↓                       ↙
                          Modelo (datos)
```

| Capa | Responsabilidad |
|---|---|
| **Modelo** | Representa los datos del negocio. No tiene SQL ni lectura por teclado. |
| **Vista** | Lee y muestra en consola. Solo habla con los controladores. |
| **Controlador** | Recibe lo que escribió el usuario, arma los objetos del modelo y llama al gestor. |
| **Servicio** | Reglas de negocio y validaciones (los `Gestor...`). |
| **Persistencia** | Todo el SQL/JDBC. Recibe la conexión por constructor. |
| **Util** | Validaciones, cálculos de reportes y escritura de archivos. |
| **Excepciones** | Errores propios del sistema. |

## Estructura del proyecto

```
ProyectoDanielJava/
├── pom.xml
├── README.md
├── reporte_ventas.txt              (se genera al usar el reporte)
└── src/main/java/
    ├── com/clases/proyectodanieljava/
    │   └── Main.java               (crea los objetos y los conecta; arranca el menú)
    ├── Modelo/
    │   ├── CategoriaGama.java      (enum: ALTA, MEDIA, BAJA)
    │   ├── TipoPersona.java        (enum: CLIENTE, VENDEDOR, ADMINISTRADOR)
    │   ├── Persona.java            (clase padre)
    │   ├── Cliente.java            (extends Persona)
    │   ├── Vendedor.java           (extends Persona)
    │   ├── Administrador.java      (extends Persona)
    │   ├── Celular.java
    │   ├── ItemVenta.java          (celular, cantidad, precio unitario)
    │   └── Venta.java              (lista de ItemVenta; calcula subtotal, IVA y total)
    ├── Persistencia/
    │   ├── ConexionDB.java         (patrón Singleton)
    │   ├── SqlHelper.java          (transacciones y SQL compartido)
    │   ├── CelularDAO.java
    │   ├── ClienteDAO.java
    │   ├── UsuarioDAO.java         (login, vendedores y administradores)
    │   └── VentaDAO.java
    ├── Servicio/
    │   ├── GestorCelulares.java
    │   ├── GestorClientes.java
    │   ├── GestorUsuarios.java
    │   └── GestorVentas.java
    ├── Controlador/
    │   ├── CelularControlador.java
    │   ├── ClienteControlador.java
    │   ├── UsuarioControlador.java
    │   ├── VentaControlador.java
    │   └── ReporteControlador.java
    ├── Vista/
    │   ├── Consola.java            (único Scanner; imprime las tablas)
    │   ├── Menu.java               (interfaz)
    │   ├── MenuPrincipal.java      (login y registro de clientes)
    │   ├── MenuAdministrador.java
    │   ├── MenuVendedor.java
    │   ├── MenuCliente.java
    │   └── MenuReportes.java
    ├── Util/
    │   ├── Validador.java
    │   ├── ReporteUtils.java       (cálculos con Stream API)
    │   └── ArchivoUtils.java       (escribe reporte_ventas.txt)
    └── Excepciones/
        ├── DatosInvalidosException.java
        ├── StockInsuficienteException.java
        └── ErrorSistemaException.java
```

## Instalación y configuración

### 1. Requisitos
- JDK 21
- Maven (viene integrado en NetBeans)
- MariaDB instalado y en ejecución
- Conexión a internet en la **primera** compilación (Maven descarga el driver de MariaDB)

### 2. Base de datos
1. Abre tu cliente de MariaDB (HeidiSQL, DBeaver, terminal, etc.).
2. Ejecuta el script `tecnostoreDaniel_db.sql`. **Recrea la base de datos desde cero** (empieza con `DROP DATABASE IF EXISTS`), así que borrará los datos que ya existan en `tecnostoreDaniel_db`.
3. Comprueba que existan las tablas y la vista `vista_celular` (el programa la usa para listar celulares).

### 3. Conexión
Abre `Persistencia/ConexionDB.java` y ajusta las tres constantes del inicio según tu MariaDB:

```java
private static final String URL = "jdbc:mariadb://localhost:3306/tecnostoreDaniel_db";
private static final String USUARIO = "root";
private static final String CLAVE = "";
```

### 4. Compilar y ejecutar (NetBeans)
1. **File → Open Project** y selecciona la carpeta del proyecto.
2. Clic derecho sobre el proyecto → **Clean and Build**. Debe terminar en `BUILD SUCCESS`.
3. Ejecuta con **F6** (o clic derecho → **Run**). La clase principal es `com.clases.proyectodanieljava.Main`.
4. Para escribir en el programa, haz clic dentro de la ventana **Output** de NetBeans y presiona Enter después de cada dato.

## Usuarios de ejemplo

| Rol | Correo | Contraseña |
|---|---|---|
| Administrador principal | admin@tecnostore.com | admin123 |
| Vendedor | carlos@tecnostore.com | vendedor123 |
| Vendedora | laura@tecnostore.com | vendedor123 |
| Cliente | juan@correo.com | cliente123 |
| Cliente | maria@correo.com | cliente123 |
| Cliente | pedro@correo.com | cliente123 |

Cualquier persona también puede crear una cuenta de cliente desde el menú principal (**Registrarme como cliente**).

## Uso del sistema

Al iniciar aparece el menú principal: **iniciar sesión**, **registrarme como cliente** o **salir**. Después del login se abre el menú del rol.

### Administrador
Celulares (listar, registrar, actualizar, eliminar) · Clientes (listar, actualizar, eliminar) · Ventas (listar, ver detalle) · Reportes · Vendedores (listar, registrar, eliminar) · Administradores (listar, crear y eliminar — **solo el principal**).

### Vendedor
Listar celulares · Registrar y listar clientes · **Registrar venta** · Listar ventas · Ver detalle de una venta · Reportes.

### Cliente
Ver el catálogo de celulares · **Mis compras** (solo ve las suyas).

### Cómo registrar una venta
1. Entra como vendedor y elige **Registrar venta**.
2. Escribe el id del cliente.
3. Escribe el id de cada celular con su cantidad. Termina con `0`.
4. El sistema muestra el detalle con subtotal, IVA (19 %) y total, y descuenta el stock.

## Reportes y archivo

Disponibles para el administrador y el vendedor, en el menú **Reportes**:

1. **Celulares con stock bajo:** los que tienen menos de 5 unidades.
2. **Top 3 de celulares más vendidos:** por unidades vendidas.
3. **Ventas totales por mes:** suma de los totales (con IVA), agrupados por año y mes.
4. **Generar `reporte_ventas.txt`:** escribe el resumen de todas las ventas (cliente, vendedor, ítems, subtotal, IVA, total y total general). El programa imprime la ruta completa del archivo creado.

El archivo se crea en el directorio desde el que se ejecuta el programa (al ejecutar desde NetBeans, la carpeta del proyecto). Cada vez que se genera, **reemplaza** al anterior.

## Reglas de negocio y validaciones

- **Celular:** modelo no vacío, precio mayor que cero, stock mayor o igual a cero (llega a 0 cuando se vende todo), y la marca, el sistema operativo y la gama deben existir.
- **Cliente, vendedor y administrador:** nombre no vacío, identificación solo con números (5 a 20 dígitos), teléfono solo con números (7 a 15 dígitos), correo con formato válido y contraseña no vacía.
- **Unicidad:** no pueden repetirse la identificación ni el correo.
- **Venta:** el cliente debe existir, debe haber al menos un celular, cada cantidad debe ser mayor que cero y debe haber stock suficiente. El precio siempre se toma de la base de datos. Si se repite un celular en el mismo pedido, las cantidades se suman.
- **Transacciones:** guardar una venta (venta, ítems y descuento de stock) y registrar personas (persona + rol) se hace en una sola transacción: o se guarda todo o no se guarda nada. El descuento de stock es condicional, para evitar vender más de lo disponible.
- **Eliminación:** no se puede eliminar un celular, cliente o vendedor que ya tiene ventas asociadas. El administrador principal no se puede eliminar.

## Patrones de diseño y principios

- **Singleton:** `ConexionDB` entrega una única conexión a toda la aplicación.
- **POO:** atributos privados con getters y setters (**encapsulamiento**), `Cliente`, `Vendedor` y `Administrador` extienden `Persona` (**herencia**), y `Venta` contiene `ItemVenta`, que a su vez contiene `Celular` (**composición**).
- **Colecciones y Stream API:** `List`, `Map` y `Optional` en todo el sistema; los reportes usan `filter`, `flatMap`, `groupingBy`, `sorted` y `limit`.
- **Excepciones:** tres excepciones propias y `try-with-resources` en el acceso a la base de datos y en la escritura del archivo.
- **SOLID:**
  - *S* — cada clase tiene una sola responsabilidad (el DAO solo habla con la base de datos, el gestor solo aplica reglas, el menú solo muestra y lee).
  - *O* — `MenuPrincipal` abre el menú de cada rol mediante un mapa de `Menu`; un rol nuevo no obliga a cambiar el login.
  - *L* — `Cliente`, `Vendedor` y `Administrador` pueden usarse donde se espera una `Persona`.
  - *I* — la interfaz `Menu` tiene un solo método.
  - *D* — los gestores y controladores reciben sus dependencias por constructor y solo `Main` crea los objetos.

## Notas

- Las contraseñas se guardan y se comparan en **texto normal**, sin encriptar (decisión de este proyecto).
- Si en otro computador falla la conexión, revisa que MariaDB esté activo, que la base `tecnostoreDaniel_db` exista y que `USUARIO` y `CLAVE` de `ConexionDB.java` sean los de ese equipo.
- Al ejecutar desde NetBeans puede aparecer un aviso de `SLF4J: No SLF4J providers were found`. Es inofensivo: lo emite una librería interna del driver y no afecta al programa.
