package persistencia;

import modelo.Transaccion;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Wrapper para serializar/deserializar las transacciones de una cuenta en XML.
 */
@XmlRootElement(name = "transacciones")
public class TransaccionWrapper {

    private List<Transaccion> transacciones;

    /** Lista de elementos <transaccion> */
    @XmlElement(name = "transaccion")
    public List<Transaccion> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
    }
}
