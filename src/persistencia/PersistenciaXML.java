package persistencia;

import modelo.Cliente;
import modelo.Cuenta;
import modelo.Transaccion;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de Persistencia usando JAXB para archivos XML:
 * - Clientes.xml
 * - Cuentas.xml
 * - Transacciones_<numeroCuenta>.xml
 */
public class PersistenciaXML implements Persistencia {
    private static final String PATH = ".";

    @Override
    public List<Cliente> cargarClientes() throws Exception {
        File file = new File(PATH, "Clientes.xml");
        if (!file.exists()) {
            return new ArrayList<>();
        }
        JAXBContext ctx = JAXBContext.newInstance(ClienteList.class);
        Unmarshaller u = ctx.createUnmarshaller();
        ClienteList wrapper = (ClienteList) u.unmarshal(file);
        return wrapper.getClientes();
    }

    @Override
    public List<Cuenta> cargarCuentas() throws Exception {
        File file = new File(PATH, "Cuentas.xml");
        if (!file.exists()) {
            return new ArrayList<>();
        }
        JAXBContext ctx = JAXBContext.newInstance(CuentaList.class);
        Unmarshaller u = ctx.createUnmarshaller();
        CuentaList wrapper = (CuentaList) u.unmarshal(file);
        return wrapper.getCuentas();
    }

    @Override
    public List<Transaccion> cargarTransacciones(String numeroCuenta) throws Exception {
        File file = new File(PATH, "Transacciones_" + numeroCuenta + ".xml");
        if (!file.exists()) {
            return new ArrayList<>();
        }
        JAXBContext ctx = JAXBContext.newInstance(TransaccionWrapper.class);
        Unmarshaller u = ctx.createUnmarshaller();
        TransaccionWrapper wrapper = (TransaccionWrapper) u.unmarshal(file);
        return wrapper.getTransacciones();
    }

    @Override
    public void guardarClientes(List<Cliente> clientes) throws Exception {
        ClienteList wrapper = new ClienteList();
        wrapper.setClientes(clientes);
        JAXBContext ctx = JAXBContext.newInstance(ClienteList.class);
        Marshaller m = ctx.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(wrapper, new File(PATH, "Clientes.xml"));
    }

    @Override
    public void guardarCuentas(List<Cuenta> cuentas) throws Exception {
        CuentaList wrapper = new CuentaList();
        wrapper.setCuentas(cuentas);
        JAXBContext ctx = JAXBContext.newInstance(CuentaList.class);
        Marshaller m = ctx.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(wrapper, new File(PATH, "Cuentas.xml"));
    }

    @Override
    public void guardarTransacciones(String numeroCuenta, List<Transaccion> transacciones) throws Exception {
        TransaccionWrapper wrapper = new TransaccionWrapper();
        wrapper.setTransacciones(transacciones);
        JAXBContext ctx = JAXBContext.newInstance(TransaccionWrapper.class);
        Marshaller m = ctx.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(wrapper, new File(PATH, "Transacciones_" + numeroCuenta + ".xml"));
    }

    @Override
    public void eliminarArchivoTransacciones(String numeroCuenta) {
        File archivo = new File("Transacciones_" + numeroCuenta + ".xml");
        if (archivo.exists()) {
            archivo.delete();
        }
    }
}
