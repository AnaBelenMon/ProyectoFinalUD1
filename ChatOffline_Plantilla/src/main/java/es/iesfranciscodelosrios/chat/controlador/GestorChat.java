package es.iesfranciscodelosrios.chat.controlador;

import es.iesfranciscodelosrios.chat.modelo.ListaMensajes;
import es.iesfranciscodelosrios.chat.modelo.ListaUsuarios;
import es.iesfranciscodelosrios.chat.modelo.Mensaje;
import es.iesfranciscodelosrios.chat.modelo.Usuario;
import jakarta.xml.bind.JAXBException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Gestiona las operaciones del chat y coordina su almacenamiento en XML.
 */
public class GestorChat {
    private final GestorXML gestorXML = new GestorXML();

    /**
     * Registra un usuario nuevo si su nombre no está vacío ni repetido.
     *
     * @param nombre nombre del usuario que se va a registrar
     * @throws JAXBException si ocurre un error al leer o guardar los usuarios XML
     * @throws IllegalArgumentException si el nombre está vacío o ya está registrado
     */
    public void registrarUsuario(String nombre) throws JAXBException {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Debe introducir su nombre");
        }
        nombre = nombre.trim();
        ListaUsuarios usuarios = gestorXML.leerUsuarios();
        for (Usuario usuario : usuarios.getUsuarios()) {
            if (usuario.getNombre() != null && usuario.getNombre().equals(nombre)) {
                throw new IllegalArgumentException("Este usuario ya está registrado");
            }
        }
        usuarios.getUsuarios().add(new Usuario(nombre));
        gestorXML.guardarUsuarios(usuarios);
    }

    /**
     * Envía y guarda un mensaje entre dos usuarios registrados.
     *
     * @param emisor nombre del usuario que envía el mensaje
     * @param destinatario nombre del usuario que recibe el mensaje
     * @param contenido texto del mensaje
     * @throws JAXBException si ocurre un error al leer o guardar los archivos XML
     * @throws IllegalArgumentException si algún dato es inválido o un usuario no está registrado
     */
    public void enviarMensaje(String emisor, String destinatario, String contenido) throws JAXBException {
        if (emisor == null || emisor.isBlank() || destinatario == null || destinatario.isBlank()
                || contenido == null || contenido.isBlank()) {
            throw new IllegalArgumentException("Es obligatorio rellenar el emisor, el destinatario y el contenido que se desea enviar");
        }
        emisor = emisor.trim();
        destinatario = destinatario.trim();
        ListaUsuarios usuarios = gestorXML.leerUsuarios();
        Usuario emisorEncontrado = null;
        Usuario destinatarioEncontrado = null;

        for (Usuario usuario : usuarios.getUsuarios()) {
            if (usuario.getNombre() != null) {
                if (usuario.getNombre().equals(emisor)) {
                    emisorEncontrado = usuario;
                }
                if (usuario.getNombre().equals(destinatario)) {
                    destinatarioEncontrado = usuario;
                }
            }
        }
        if (emisorEncontrado == null || destinatarioEncontrado == null) {
            throw new IllegalArgumentException("El emisor y el destinatario deben estar registrados.");
        }
        Mensaje mensaje = new Mensaje(emisorEncontrado.getNombre(), destinatarioEncontrado.getNombre(), contenido, LocalDateTime.now().toString());
        ListaMensajes mensajes = gestorXML.leerMensajes();
        mensajes.getMensajes().add(mensaje);
        gestorXML.guardarMensajes(mensajes);
    }

    /**
     * Devuelve los mensajes intercambiados entre dos usuarios, en ambos sentidos.
     *
     * @param usuarioA nombre del primer usuario
     * @param usuarioB nombre del segundo usuario
     * @return mensajes de la conversación
     * @throws JAXBException si ocurre un error al leer los mensajes XML
     * @throws IllegalArgumentException si no se indican los dos usuarios
     */
    public List<Mensaje> consultarConversacion(String usuarioA, String usuarioB) throws JAXBException {
        if (usuarioA == null || usuarioA.isBlank() || usuarioB == null || usuarioB.isBlank()) {
            throw new IllegalArgumentException("Debes indicar los dos usuarios. ");
        }
        usuarioA = usuarioA.trim();
        usuarioB = usuarioB.trim();
        List<Mensaje> conversacion = new ArrayList<>();
        for (Mensaje mensaje : gestorXML.leerMensajes().getMensajes()){
            if (mensaje.getEmisor() != null && mensaje.getDestinatario() != null) {
                boolean a = usuarioA.equals(mensaje.getEmisor()) && usuarioB.equals(mensaje.getDestinatario());
                boolean b = usuarioB.equals(mensaje.getEmisor()) && usuarioA.equals(mensaje.getDestinatario());
                if (a || b) {
                    conversacion.add(mensaje);
                }
            }
        }
        return  conversacion;
    }

    /**
     * Cuenta cuántos mensajes ha enviado cada participante de la conversación.
     *
     * @param usuarioA nombre del primer usuario
     * @param usuarioB nombre del segundo usuario
     * @return mapa con cada emisor y la cantidad de mensajes que ha enviado
     * @throws JAXBException si ocurre un error al leer los mensajes XML
     * @throws IllegalArgumentException si no se indican los dos usuarios
     */
    public Map<String, Long> estadisticas(String usuarioA, String usuarioB) throws JAXBException {
        List<Mensaje> conversacion = consultarConversacion(usuarioA, usuarioB);
        return conversacion.stream().collect(
                Collectors.groupingBy(Mensaje::getEmisor, Collectors.counting())
        );
    }

    /**
     * Exporta los mensajes de una conversación a un archivo de texto.
     *
     * @param usuarioA nombre del primer usuario
     * @param usuarioB nombre del segundo usuario
     * @param destino ruta del archivo de texto que se va a crear
     * @throws JAXBException si ocurre un error al leer los mensajes XML
     * @throws IOException si no se puede crear la carpeta o escribir el archivo
     * @throws IllegalArgumentException si no se indica una ruta de destino
     */
    public void exportarConversacion(String usuarioA, String usuarioB, Path destino) throws JAXBException, IOException {
        if (destino == null) {
            throw new IllegalArgumentException("Debes indicar el archivo de destino.");
        }

        Path carpeta = destino.toAbsolutePath().getParent();
        if (carpeta != null) {
            Files.createDirectories(carpeta);
        }

        String conversacion = consultarConversacion(usuarioA, usuarioB).stream().map(mensaje -> {
                    String linea = mensaje.getFecha() + " | " + mensaje.getEmisor()
                            + " -> " + mensaje.getDestinatario() + " | " + mensaje.getContenido();
                    if (mensaje.getAdjuntoRuta() != null && !tieneAdjuntoDisponible(mensaje)) {
                        linea += " | Adjunto no encontrado: " + mensaje.getAdjuntoNombre();
                    }
                    return linea;
                }).collect(Collectors.joining(System.lineSeparator()));
        try (FileWriter escritor = new FileWriter(destino.toFile())) {
            escritor.write(conversacion);
        }
    }

    /**
     * Valida un archivo, lo copia a {@code media/} y guarda sus datos en el mensaje.
     *
     * @param mensaje mensaje guardado al que se asociará el archivo
     * @param archivoOrigen ruta del archivo que se va a adjuntar
     * @throws IOException si ocurre un error al copiar el archivo
     * @throws JAXBException si ocurre un error al guardar los mensajes XML
     * @throws IllegalArgumentException si el archivo no es válido o el mensaje no está guardado
     */
    public void adjuntarArchivo(Mensaje mensaje, Path archivoOrigen) throws IOException, JAXBException {
        if (mensaje == null || archivoOrigen == null) {
            throw new IllegalArgumentException("Debes indicar el mensaje y el archivo.");
        }
        File archivo = archivoOrigen.toFile();
        if (!archivo.isFile() || !archivo.canRead()) {
            throw new IllegalArgumentException("El archivo no existe o no se puede leer.");
        }

        String nombre = archivo.getName();
        String nombreMinusculas = nombre.toLowerCase(Locale.ROOT);
        String[] extensionesPermitidas = {".jpg", ".jpeg", ".png", ".gif", ".pdf", ".txt"};
        String extension = null;
        for (String extensionPermitida : extensionesPermitidas) {
            if (nombreMinusculas.endsWith(extensionPermitida)) {
                extension = extensionPermitida;
                break;
            }
        }
        if (extension == null) {
            throw new IllegalArgumentException(
                    "Extensión no permitida. Usa JPG, PNG, GIF, PDF o TXT.");
        }

        long tamano = archivo.length();
        if (tamano > 10L * 1024 * 1024) {
            throw new IllegalArgumentException("El archivo no puede superar los 10 MB.");
        }

        ListaMensajes mensajes = gestorXML.leerMensajes();
        Mensaje mensajeGuardado = null;
        for (Mensaje candidato : mensajes.getMensajes()) {
            if (mismoMensaje(candidato, mensaje)) {
                mensajeGuardado = candidato;
                break;
            }
        }
        if (mensajeGuardado == null) {
            throw new IllegalArgumentException("El mensaje debe estar guardado antes de adjuntar un archivo.");
        }

        File carpetaMedia = new File("media");
        if (!carpetaMedia.exists() && !carpetaMedia.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta media.");
        }
        File copia = Files.createTempFile(
                carpetaMedia.toPath(), System.currentTimeMillis() + "_", "_" + nombre).toFile();
        try (FileInputStream entrada = new FileInputStream(archivo);
             FileOutputStream salida = new FileOutputStream(copia)) {
            byte[] buffer = new byte[8192];
            int bytesLeidos;
            while ((bytesLeidos = entrada.read(buffer)) != -1) {
                salida.write(buffer, 0, bytesLeidos);
            }
        }

        mensajeGuardado.setAdjuntoRuta(copia.getPath());
        mensajeGuardado.setAdjuntoNombre(nombre);
        mensajeGuardado.setAdjuntoTamano(tamano);
        mensajeGuardado.setAdjuntoTipo(tipoArchivo(extension));
        gestorXML.guardarMensajes(mensajes);

        mensaje.setAdjuntoRuta(copia.getPath());
        mensaje.setAdjuntoNombre(nombre);
        mensaje.setAdjuntoTamano(tamano);
        mensaje.setAdjuntoTipo(tipoArchivo(extension));
    }

    /**
     * Comprueba si el archivo asociado al mensaje existe en el disco.
     *
     * @param mensaje mensaje cuyo adjunto se comprobará
     * @return {@code true} si hay una ruta de adjunto y el archivo existe
     */
    public boolean tieneAdjuntoDisponible(Mensaje mensaje) {
        return mensaje != null && mensaje.getAdjuntoRuta() != null
                && !mensaje.getAdjuntoRuta().isBlank()
                && new File(mensaje.getAdjuntoRuta()).isFile();
    }

    /**
     * Copia el archivo adjunto de un mensaje a la ruta indicada.
     *
     * @param mensaje mensaje que contiene la ruta del adjunto
     * @param destino ruta donde se guardará la copia
     * @throws IOException si el archivo de origen no existe o no se puede copiar
     * @throws IllegalArgumentException si no se indica el mensaje, el destino o un adjunto
     */
    public void exportarAdjunto(Mensaje mensaje, Path destino) throws IOException {
        if (mensaje == null || destino == null) {
            throw new IllegalArgumentException("Debes indicar el mensaje y el destino.");
        }
        if (mensaje.getAdjuntoRuta() == null || mensaje.getAdjuntoRuta().isBlank()) {
            throw new IllegalArgumentException("El mensaje no tiene ningún archivo adjunto.");
        }

        Path origen = Path.of(mensaje.getAdjuntoRuta());
        if (!Files.isRegularFile(origen) || !Files.isReadable(origen)) {
            throw new IOException("El archivo adjunto no se encuentra o no se puede leer.");
        }
        Path carpeta = destino.toAbsolutePath().getParent();
        if (carpeta != null) {
            Files.createDirectories(carpeta);
        }
        Files.copy(origen, destino);
    }

    /**
     * Comprueba si dos mensajes tienen los mismos datos principales.
     *
     * @param a primer mensaje
     * @param b segundo mensaje
     * @return {@code true} si ambos mensajes tienen emisor, destinatario, fecha y contenido iguales
     */
    private boolean mismoMensaje(Mensaje a, Mensaje b) {
        return a != null && b != null
                && Objects.equals(a.getEmisor(), b.getEmisor())
                && Objects.equals(a.getDestinatario(), b.getDestinatario())
                && Objects.equals(a.getFecha(), b.getFecha())
                && Objects.equals(a.getContenido(), b.getContenido());
    }

    /**
     * Obtiene el tipo MIME correspondiente a una extensión permitida.
     *
     * @param extension extensión del archivo, incluido el punto
     * @return tipo MIME del archivo
     */
    private String tipoArchivo(String extension) {
        if (extension.equals(".jpg") || extension.equals(".jpeg")) return "image/jpeg";
        if (extension.equals(".png")) return "image/png";
        if (extension.equals(".gif")) return "image/gif";
        if (extension.equals(".pdf")) return "application/pdf";
        return "text/plain";
    }
}
