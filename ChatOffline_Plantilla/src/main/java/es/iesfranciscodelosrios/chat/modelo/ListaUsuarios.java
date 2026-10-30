package es.iesfranciscodelosrios.chat.modelo;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Contiene la lista de usuarios que se guarda y se lee con JAXB.
 */
@XmlRootElement(name = "usuarios")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListaUsuarios {
    @XmlElement(name = "usuario")
    private List<Usuario> usuarios = new ArrayList<>();

    /**
     * Crea una lista de usuarios vacía.
     */
    public ListaUsuarios() {}

    /**
     * Devuelve los usuarios registrados.
     *
     * @return lista de usuarios
     */
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * Reemplaza la lista de usuarios.
     *
     * @param usuarios nueva lista de usuarios
     */
    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
