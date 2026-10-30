package es.iesfranciscodelosrios.chat;

import java.util.Scanner;

import static es.iesfranciscodelosrios.chat.vista.Vista.ejecutarMenu;

/**
 * Punto de entrada de la aplicación Chat Offline.
 */
public class Main {
    /**
     * Inicia el menú principal y cierra la entrada de teclado al terminar.
     *
     * @param args argumentos recibidos al iniciar el programa
     */
    public static void main(String[] args) {
        try (Scanner teclado = new Scanner(System.in)) {
            ejecutarMenu(teclado);
        }
    }
}
