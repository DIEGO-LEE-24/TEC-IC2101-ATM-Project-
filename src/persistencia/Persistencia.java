package persistencia;

import modelo.Cliente;
import modelo.Cuenta;
import modelo.Transaccion;
import java.util.List;

/**
 * Contrato de persistencia para clientes, cuentas y transacciones.
 */
public interface Persistencia {

    List<Cliente> cargarClientes() throws Exception;

    List<Cuenta> cargarCuentas() throws Exception;

    List<Transaccion> cargarTransacciones(String numeroCuenta) throws Exception;

    void guardarClientes(List<Cliente> clientes) throws Exception;

    void guardarCuentas(List<Cuenta> cuentas) throws Exception;

    void guardarTransacciones(String numeroCuenta, List<Transaccion> transacciones) throws Exception;

    void eliminarArchivoTransacciones(String numeroCuenta);
}
