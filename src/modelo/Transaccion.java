package modelo;

import java.time.LocalDateTime;

/**
 * Representa una transacción realizada sobre una cuenta bancaria:
 * depósito, retiro o transferencia, con posible comisión.
 */
public class Transaccion {
    private final TipoTransaccion tipo;
    private final long monto;               // Monto en colones (o equivalente en colones)
    private final LocalDateTime fecha;
    private final boolean cobroComision;
    private final long montoComision;       // 0 si no hubo cobro

    /**
     * Construye una nueva transacción.
     *
     * @param tipo            tipo de transacción
     * @param monto           monto principal (en colones)
     * @param cobroComision   true si se aplicó comisión
     * @param montoComision   monto de la comisión (>0 si cobroComision = true)
     * @throws IllegalArgumentException si monto ≤ 0 o (cobroComision && montoComision ≤ 0)
     */
    public Transaccion(TipoTransaccion tipo,
                       long monto,
                       boolean cobroComision,
                       long montoComision) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        if (cobroComision && montoComision <= 0) {
            throw new IllegalArgumentException("Monto de comisión inválido");
        }
        this.tipo = tipo;
        this.monto = monto;
        this.cobroComision = cobroComision;
        this.montoComision = cobroComision ? montoComision : 0L;
        this.fecha = LocalDateTime.now();
    }

    public TipoTransaccion getTipo() {
        return tipo;
    }

    public long getMonto() {
        return monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public boolean isCobroComision() {
        return cobroComision;
    }

    public long getMontoComision() {
        return montoComision;
    }

    @Override
    public String toString() {
        return String.format(
            "Transaccion[tipo=%s, monto=%d, fecha=%s, comision=%s(%d)]",
            tipo, monto, fecha, cobroComision ? "sí" : "no", montoComision
        );
    }
}
