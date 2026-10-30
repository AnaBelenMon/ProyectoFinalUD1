package es.iesfranciscodelosrios.chat.controlador;

import es.iesfranciscodelosrios.chat.modelo.ListaMensajes;
import es.iesfranciscodelosrios.chat.modelo.ListaUsuarios;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

/**
 * Lee y guarda usuarios y mensajes en archivos XML mediante JAXB.
 */
public class GestorXML {
    /**
     * Guarda la lista de usuarios en {@code usuarios.xml}.
     *
     * @param usuarios usuarios que se van a guardar
     * @throws JAXBException si JAXB no puede crear o escribir el XML
     */
    public void guardarUsuarios(ListaUsuarios usuarios) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ListaUsuarios.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(usuarios, new File("usuarios.xml"));
    }

    /**
     * Lee los usuarios de {@code usuarios.xml}.
     *
     * @return usuarios leídos, o una lista vacía si el archivo aún no existe
     * @throws JAXBException si JAXB no puede leer el archivo
     */
    public ListaUsuarios leerUsuarios() throws JAXBException {
        File archivo = new File("usuarios.xml");
        if (!archivo.exists()) {
            return new ListaUsuarios();
        }
        JAXBContext context = JAXBContext.newInstance(ListaUsuarios.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return  (ListaUsuarios) unmarshaller.unmarshal(archivo);
    }

    /**
     * Guarda la lista de mensajes en {@code mensajes.xml}.
     *
     * @param mensajes mensajes que se van a guardar
     * @throws JAXBException si JAXB no puede crear o escribir el XML
     */
    public void guardarMensajes(ListaMensajes mensajes) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ListaMensajes.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(mensajes, new File("mensajes.xml"));
    }

    /**
     * Lee los mensajes de {@code mensajes.xml}.
     *
     * @return mensajes leídos, o una lista vacía si el archivo aún no existe
     * @throws JAXBException si JAXB no puede leer el archivo
     */
    public ListaMensajes leerMensajes() throws JAXBException {
        File archivo = new File("mensajes.xml");
        if (!archivo.exists()) {
            return new ListaMensajes();
        }
        JAXBContext context = JAXBContext.newInstance(ListaMensajes.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return   (ListaMensajes) unmarshaller.unmarshal(archivo);
    }
}
