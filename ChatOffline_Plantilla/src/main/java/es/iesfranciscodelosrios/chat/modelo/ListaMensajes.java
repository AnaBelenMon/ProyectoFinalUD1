package es.iesfranciscodelosrios.chat.modelo;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Contiene los mensajes que se guardan y se leen con JAXB.
 */
@XmlRootElement(name = "mensajes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListaMensajes {
    @XmlElement(name = "mensaje")
    private List<Mensaje> mensajes = new ArrayList<>();

    /**
     * Crea una lista de mensajes vacía.
     */
    public ListaMensajes() { }

    /**
     * Devuelve los mensajes almacenados.
     *
     * @return lista de mensajes
     */
    public List<Mensaje> getMensajes() { return mensajes; }

    /**
     * Reemplaza la lista de mensajes.
     *
     * @param mensajes nueva lista de mensajes
     */
    public void setMensajes(List<Mensaje> mensajes) { this.mensajes = mensajes; }
}
