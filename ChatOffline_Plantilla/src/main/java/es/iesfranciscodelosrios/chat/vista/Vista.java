package es.iesfranciscodelosrios.chat.vista;

import es.iesfranciscodelosrios.chat.controlador.GestorChat;
import es.iesfranciscodelosrios.chat.modelo.Mensaje;
import jakarta.xml.bind.JAXBException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Muestra el menú de consola y recoge los datos introducidos por el usuario.
 */
public class Vista {
    /**
     * Ejecuta el menú hasta que se selecciona la opción de salida.
     *
     * @param teclado lector de las entradas escritas en la consola
     */
    public static void ejecutarMenu(Scanner teclado) {
        GestorChat chat = new GestorChat();
        boolean salir = false;

        while (!salir) {
            System.out.println("\nChat Offline");
            System.out.println("1. Registrar usuario");
            System.out.println("2. Enviar mensaje");
            System.out.println("3. Consultar conversación");
            System.out.println("4. Estadísticas");
            System.out.println("5. Exportar conversación");
            System.out.println("6. Adjuntar archivo a un mensaje");
            System.out.println("7. Exportar un archivo adjunto");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            String opcion = teclado.nextLine().trim();

            try {
                switch (opcion) {
                    case "1" -> {
                        System.out.print("Nombre de usuario: ");
                        chat.registrarUsuario(teclado.nextLine());
                        System.out.println("Usuario registrado.");
                    }
                    case "2" -> {
                        System.out.print("Emisor: ");
                        String emisor = teclado.nextLine();
                        System.out.print("Destinatario: ");
                        String destinatario = teclado.nextLine();
                        System.out.print("Mensaje: ");
                        String contenido = teclado.nextLine();
                        chat.enviarMensaje(emisor, destinatario, contenido);
                        System.out.println("Mensaje enviado.");
                    }
                    case "3" -> {
                        String[] usuarios = pedirUsuarios(teclado);
                        var mensajes = chat.consultarConversacion(usuarios[0], usuarios[1]);
                        if (mensajes.isEmpty()) {
                            System.out.println("No hay mensajes en esta conversación.");
                        } else {
                            for (Mensaje mensaje : mensajes) {
                                mostrarMensaje(chat, mensaje);
                            }
                        }
                    }
                    case "4" -> {
                        String[] usuarios = pedirUsuarios(teclado);
                        var estadisticas = chat.estadisticas(usuarios[0], usuarios[1]);
                        if (estadisticas.isEmpty()) {
                            System.out.println("No hay mensajes en esta conversación.");
                        } else {
                            estadisticas.forEach((emisor, cantidad) ->
                                    System.out.println(emisor + ": " + cantidad + " mensaje(s)"));
                        }
                    }
                    case "5" -> {
                        String[] usuarios = pedirUsuarios(teclado);
                        System.out.print("Ruta del archivo TXT de destino: ");
                        Path destino = Path.of(teclado.nextLine().trim());
                        chat.exportarConversacion(usuarios[0], usuarios[1], destino);
                        System.out.println("Conversación exportada a " + destino);
                    }
                    case "6" -> adjuntarArchivo(teclado, chat);
                    case "7" -> exportarAdjunto(teclado, chat);
                    case "0" -> salir = true;
                    default -> System.out.println("Opción no válida.");
                }
            } catch (JAXBException | IOException | IllegalArgumentException e) {
                System.out.println("No se pudo completar la operación: " + e.getMessage());
            }
        }
        System.out.println("Hasta luego.");
    }

    /**
     * Solicita los nombres de los dos participantes de una conversación.
     *
     * @param teclado lector de las entradas escritas en la consola
     * @return array con los nombres de los dos usuarios
     */
    private static String[] pedirUsuarios(Scanner teclado) {
        System.out.print("Usuario A: ");
        String usuarioA = teclado.nextLine();
        System.out.print("Usuario B: ");
        String usuarioB = teclado.nextLine();
        return new String[]{usuarioA, usuarioB};
    }

    /**
     * Muestra el contenido de un mensaje y el estado de su archivo adjunto.
     *
     * @param chat gestor que comprueba si el archivo adjunto está disponible
     * @param mensaje mensaje que se va a mostrar
     */
    private static void mostrarMensaje(GestorChat chat, Mensaje mensaje) {
        System.out.println(mensaje.getFecha() + " | "
                + mensaje.getEmisor() + " -> " + mensaje.getDestinatario()
                + ": " + mensaje.getContenido());
        if (mensaje.getAdjuntoRuta() != null) {
            System.out.println("  Adjunto: " + mensaje.getAdjuntoNombre()
                    + " (" + mensaje.getAdjuntoTamano() + " bytes)");
            if (!chat.tieneAdjuntoDisponible(mensaje)) {
                System.out.println("  Adjunto no encontrado.");
            }
        }
    }

    /**
     * Permite seleccionar un mensaje y asociarle un archivo.
     *
     * @param teclado lector de las entradas escritas en la consola
     * @param chat gestor que procesa la conversación y guarda el archivo
     * @throws JAXBException si ocurre un error al leer o guardar los mensajes XML
     * @throws IOException si ocurre un error al copiar el archivo
     */
    private static void adjuntarArchivo(Scanner teclado, GestorChat chat)
            throws JAXBException, IOException {
        Mensaje mensaje = seleccionarMensaje(teclado, chat);
        if (mensaje == null) {
            return;
        }

        System.out.print("Ruta del archivo que quieres adjuntar: ");
        Path archivo = Path.of(teclado.nextLine().trim());
        chat.adjuntarArchivo(mensaje, archivo);
        System.out.println("Archivo adjuntado.");
    }

    /**
     * Permite seleccionar un mensaje y copiar su archivo adjunto a otra ruta.
     *
     * @param teclado lector de las entradas escritas en la consola
     * @param chat gestor que consulta y exporta el archivo
     * @throws JAXBException si ocurre un error al leer los mensajes XML
     * @throws IOException si el archivo no existe o no se puede exportar
     */
    private static void exportarAdjunto(Scanner teclado, GestorChat chat)
            throws JAXBException, IOException {
        Mensaje mensaje = seleccionarMensaje(teclado, chat);
        if (mensaje == null) {
            return;
        }
        if (mensaje.getAdjuntoRuta() == null || mensaje.getAdjuntoRuta().isBlank()) {
            throw new IllegalArgumentException("El mensaje seleccionado no tiene ningún adjunto.");
        }
        if (!chat.tieneAdjuntoDisponible(mensaje)) {
            throw new IOException("El archivo adjunto no se encuentra.");
        }

        System.out.print("Ruta de destino para guardar el adjunto: ");
        Path destino = Path.of(teclado.nextLine().trim());
        chat.exportarAdjunto(mensaje, destino);
        System.out.println("Adjunto exportado a " + destino);
    }

    /**
     * Muestra la conversación y solicita el número del mensaje que se quiere usar.
     *
     * @param teclado lector de las entradas escritas en la consola
     * @param chat gestor que consulta los mensajes
     * @return mensaje seleccionado, o {@code null} si la conversación está vacía
     * @throws JAXBException si ocurre un error al leer los mensajes XML
     * @throws IllegalArgumentException si el número introducido no es válido
     */
    private static Mensaje seleccionarMensaje(Scanner teclado, GestorChat chat) throws JAXBException {
        String[] usuarios = pedirUsuarios(teclado);
        var mensajes = chat.consultarConversacion(usuarios[0], usuarios[1]);
        if (mensajes.isEmpty()) {
            System.out.println("No hay mensajes en esta conversación.");
            return null;
        }

        for (int i = 0; i < mensajes.size(); i++) {
            System.out.print((i + 1) + ". ");
            mostrarMensaje(chat, mensajes.get(i));
        }
        System.out.print("Número del mensaje: ");
        int numero;
        try {
            numero = Integer.parseInt(teclado.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Debes introducir un número válido.");
        }
        if (numero < 1 || numero > mensajes.size()) {
            throw new IllegalArgumentException("El número no corresponde a un mensaje.");
        }
        return mensajes.get(numero - 1);
    }
}