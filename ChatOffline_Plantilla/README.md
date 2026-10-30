# Chat Offline — Plantilla de partida

**2.º DAM · Acceso a Datos · UD1**

Aplicación de consola para registrar usuarios, intercambiar mensajes almacenados
en XML, consultar conversaciones, generar estadísticas, exportarlas a texto y
gestionar archivos adjuntos.

## Requisitos
- JDK 17 o posterior
- Maven

## Ejecutar
Desde la carpeta del proyecto:

```bash
mvn compile exec:java
```

## Funcionalidades
- Los usuarios y mensajes se leen y guardan con JAXB en `usuarios.xml` y
  `mensajes.xml`, generados automáticamente al ejecutar la aplicación.
- Se pueden consultar los mensajes entre dos usuarios en ambos sentidos.
- Las estadísticas por emisor y la exportación de conversaciones usan Java
  Stream API.
- Los adjuntos JPG/JPEG, PNG, GIF, PDF y TXT de hasta 10 MB se copian a `media/`
  mediante `InputStream`/`OutputStream`; sus metadatos quedan guardados en XML.
- Se pueden exportar los adjuntos desde el menú. Si falta un archivo, el mensaje
  se conserva y la conversación lo indica.

### Comprobaciones mínimas
- Registrar dos usuarios, cerrar el programa y comprobar que siguen existiendo al reabrirlo.
- Enviar mensajes en ambos sentidos, cerrar y comprobar que persisten.
- Consultar solo los mensajes entre dos usuarios y comprobar las estadísticas.
- Exportar una conversación y verificar su contenido en el TXT.
- Adjuntar y exportar un archivo; después, comprobar que un adjunto ausente se indica sin borrar el mensaje.
