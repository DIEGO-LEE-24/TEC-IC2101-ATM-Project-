package modelo;

import javax.xml.ws.WebServiceException;

/**
 * Cliente simulado para el servicio web del Banco Central de Costa Rica (BCCR).
 * Proporciona tipos de cambio de compra y venta. En producción, reemplazar
 * la implementación por una llamada real al web service público del BCCR.
 */
public final class ServicioBCCR {
    private ServicioBCCR() {
        // No se instancia
    }

    /**
     * Obtiene el tipo de cambio de compra del dólar en colones.
     *
     * @return precio de compra (colones por USD)
     * @throws WebServiceException si falla la llamada al servicio
     */
    public static double getTipoCompra() {
        // TODO: implementar llamada real al web service del BCCR
        return 610.50;
    }

    /**
     * Obtiene el tipo de cambio de venta del dólar en colones.
     *
     * @return precio de venta (colones por USD)
     * @throws WebServiceException si falla la llamada al servicio
     */
    public static double getTipoVenta() {
        // TODO: implementar llamada real al web service del BCCR
        return 600.75;
    }
}
