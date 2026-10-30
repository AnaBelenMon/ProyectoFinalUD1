# Chat Offline

Aplicación de consola desarrollada en Java para gestionar una pequeña red de chat local con persistencia en XML.

## Descripción

Chat Offline permite:
- registrar usuarios,
- enviar mensajes entre dos personas,
- consultar conversaciones concretas,
- obtener estadísticas por emisor,
- exportar una conversación a un archivo `.txt`,
- adjuntar archivos a mensajes y exportarlos después.

La información se guarda en ficheros XML, y los archivos adjuntos se copian en la carpeta `media/` del proyecto.

## Tecnologías
- Java 17
- Maven
- Jakarta XML Binding (JAXB)

## Requisitos
- JDK 17 o superior
- Maven 3.8+

## Ejecución
Desde la carpeta del proyecto:

```bash
mvn clean compile
mvn exec:java
```

También puedes arrancarlo directamente con:

```bash
mvn exec:java
```

## Menú principal
La aplicación ofrece estas opciones:

1. Registrar usuario
2. Enviar mensaje
3. Consultar conversación
4. Estadísticas
5. Exportar conversación
6. Adjuntar archivo a un mensaje
7. Exportar un archivo adjunto
0. Salir

## Persistencia de datos
Al ejecutarse, la aplicación genera y reutiliza los siguientes archivos en la raíz del proyecto:

- `usuarios.xml`
- `mensajes.xml`

Los archivos adjuntos se almacenan en:

- `media/`

## Estructura del proyecto

```text
ChatOffline_Plantilla/
├── src/
│   └── main/
│       └── java/
│           └── es/iesfranciscodelosrios/chat/
│               ├── Main.java
│               ├── controlador/
│               ├── modelo/
│               └── vista/
├── media/
├── pom.xml
├── README.md
└── usuarios.xml / mensajes.xml (generados al ejecutar)
```

## Funcionalidades detalladas

### 1. Gestión de usuarios
- Se pueden registrar nuevos usuarios.
- Los datos permanecen almacenados entre ejecuciones gracias a JAXB.

### 2. Envío y consulta de mensajes
- Se envían mensajes con emisor y destinatario.
- La aplicación permite consultar solo la conversación entre dos usuarios.
- Los mensajes se muestran con fecha, emisor, destinatario y contenido.

### 3. Estadísticas
- Se calculan cuántos mensajes ha enviado cada participante dentro de una conversación.
- La lógica se apoya en streams de Java para tratar las colecciones de forma más clara.

### 4. Exportación de conversaciones
- Se puede exportar la conversación completa a un fichero `.txt`.
- El resultado incluye el contenido de los mensajes y, si procede, información del adjunto asociado.

### 5. Archivos adjuntos
- Se permiten adjuntar archivos de tipos comunes como JPG, PNG, GIF, PDF o TXT.
- El tamaño máximo recomendado es 10 MB.
- Los archivos se copian a la carpeta `media/` y se guarda su metadata asociada en XML.
- Si un archivo adjunto no existe, la conversación sigue existiendo y el sistema lo informa claramente.

## Pruebas rápidas de validación
1. Registrar dos usuarios.
2. Cerrar la aplicación y volver a abrirla para comprobar que siguen guardados.
3. Enviar mensajes entre ambos usuarios.
4. Consultar la conversación y revisar las estadísticas.
5. Exportar la conversación a un archivo TXT.
6. Adjuntar un fichero y exportarlo desde el menú.
7. Comprobar que, si falta un archivo, el mensaje no se elimina y la aplicación lo indica.

## Notas
Este proyecto está pensado como ejercicio práctico de acceso a datos con XML y Java, aplicando serialización, manejo de ficheros, consulta de datos y organización en capas (modelo, controlador y vista).
