package es.iesfranciscodelosrios.chat.modelo;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

/**
 * Representa a un usuario registrado en el chat.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Usuario {
    private String nombre;

    /**
     * Crea un usuario vacío para que JAXB pueda leerlo desde XML.
     */
    public Usuario() { }

    /**
     * Crea un usuario con el nombre indicado.
     *
     * @param nombre nombre del usuario
     */
    public Usuario(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve el nombre del usuario.
     *
     * @return nombre del usuario
     */
    public String getNombre() { return nombre; }

    /**
     * Cambia el nombre del usuario.
     *
     * @param nombre nuevo nombre
     */
    public void setNombre(String nombre) { this.nombre = nombre; }
}