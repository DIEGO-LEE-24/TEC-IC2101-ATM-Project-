package persistencia;

import modelo.Cuenta;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Wrapper para serializar/deserializar listas de Cuenta en XML.
 */
@XmlRootElement(name = "cuentas")
public class CuentaList {

    private List<Cuenta> cuentas;

    /** Lista de elementos <cuenta> */
    @XmlElement(name = "cuenta")
    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }
}
