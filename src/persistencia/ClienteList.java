package persistencia;

import modelo.Cliente;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Wrapper para serializar/deserializar listas de Cliente en XML.
 */
@XmlRootElement(name = "clientes")
public class ClienteList {

    private List<Cliente> clientes;

    /** Lista de elementos &lt;cliente&gt; */
    @XmlElement(name = "cliente")
    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }
}
