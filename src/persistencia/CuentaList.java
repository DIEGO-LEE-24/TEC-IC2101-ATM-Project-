package persistencia;

import modelo.Cuenta;
import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "cuentas")
public class CuentaList {
    private List<Cuenta> cuentas;

    @XmlElement(name = "cuenta")
    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }
}