package es.iesfranciscodelosrios.chat.modelo;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

/**
 * Representa un mensaje entre dos usuarios y, opcionalmente, su archivo adjunto.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Mensaje {
    private String emisor;
    private String destinatario;
    private String contenido;
    private String fecha; // ISO-8601, por ejemplo: 2026-10-26T13:45:00
    private String adjuntoRuta; // Puede ser null si no hay adjunto.
    private String adjuntoNombre;
    private long adjuntoTamano;
    private String adjuntoTipo;

    /**
     * Crea un mensaje vacío para que JAXB pueda leerlo desde XML.
     */
    public Mensaje() { }

    /**
     * Crea un mensaje con sus datos principales.
     *
     * @param emisor nombre del usuario que envía el mensaje
     * @param destinatario nombre del usuario que recibe el mensaje
     * @param contenido texto del mensaje
     * @param fecha fecha y hora del mensaje
     */
    public Mensaje(String emisor, String destinatario, String contenido, String fecha) {
        this.emisor = emisor;
        this.destinatario = destinatario;
        this.contenido = contenido;
        this.fecha = fecha;
    }

    /**
     * @return nombre del emisor
     */
    public String getEmisor() { return emisor; }
    /**
     * @param emisor nombre del emisor
     */
    public void setEmisor(String emisor) { this.emisor = emisor; }
    /**
     * @return nombre del destinatario
     */
    public String getDestinatario() { return destinatario; }
    /**
     * @param destinatario nombre del destinatario
     */
    public void setDestinatario(String destinatario) { this.destinatario = destinatario; }
    /**
     * @return contenido del mensaje
     */
    public String getContenido() { return contenido; }
    /**
     * @param contenido texto del mensaje
     */
    public void setContenido(String contenido) { this.contenido = contenido; }
    /**
     * @return fecha y hora del mensaje
     */
    public String getFecha() { return fecha; }
    /**
     * @param fecha fecha y hora del mensaje
     */
    public void setFecha(String fecha) { this.fecha = fecha; }
    /**
     * @return ruta del archivo adjunto, o {@code null} si no hay archivo
     */
    public String getAdjuntoRuta() { return adjuntoRuta; }
    /**
     * @param adjuntoRuta ruta del archivo adjunto
     */
    public void setAdjuntoRuta(String adjuntoRuta) { this.adjuntoRuta = adjuntoRuta; }
    /**
     * @return nombre original del archivo adjunto
     */
    public String getAdjuntoNombre() { return adjuntoNombre; }
    /**
     * @param adjuntoNombre nombre original del archivo adjunto
     */
    public void setAdjuntoNombre(String adjuntoNombre) { this.adjuntoNombre = adjuntoNombre; }
    /**
     * @return tamaño del archivo adjunto en bytes
     */
    public long getAdjuntoTamano() { return adjuntoTamano; }
    /**
     * @param adjuntoTamano tamaño del archivo adjunto en bytes
     */
    public void setAdjuntoTamano(long adjuntoTamano) { this.adjuntoTamano = adjuntoTamano; }
    /**
     * @return tipo MIME del archivo adjunto
     */
    public String getAdjuntoTipo() { return adjuntoTipo; }
    /**
     * @param adjuntoTipo tipo MIME del archivo adjunto
     */
    public void setAdjuntoTipo(String adjuntoTipo) { this.adjuntoTipo = adjuntoTipo; }
}