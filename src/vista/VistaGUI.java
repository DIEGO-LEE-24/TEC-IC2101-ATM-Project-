package vista;

import controlador.ControladorCajero;
import modelo.Transaccion;
import modelo.Validacion;
import persistencia.PersistenciaXML;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Interfaz gráfica del Cajero Automático con soporte para colones y dólares.
 */
public class VistaGUI extends JFrame {
    private final ControladorCajero ctrl;
    private final JPanel cards;

    // IDs de cada tarjeta
    private static final String MENU          = "MENU";
    private static final String CREAR_CLIENTE = "CREAR_CLIENTE";
    private static final String CREAR_CUENTA  = "CREAR_CUENTA";
    private static final String CAMBIAR_PIN   = "CAMBIAR_PIN";
    private static final String DEPOSITAR     = "DEPOSITAR";
    private static final String RETIRAR       = "RETIRAR";
    private static final String DEPOSITAR_USD = "DEPOSITAR_USD";
    private static final String RETIRAR_USD   = "RETIRAR_USD";
    private static final String SALDO         = "SALDO";
    private static final String SALDO_USD     = "SALDO_USD";
    private static final String HISTORIAL     = "HISTORIAL";
    private static final String ESTADO_CUENTA = "ESTADO_CUENTA";
    private static final String TRANSFERIR    = "TRANSFERIR";
    private static final String CAMBIO_COMPRA = "CAMBIO_COMPRA";
    private static final String CAMBIO_VENTA  = "CAMBIO_VENTA";
    private static final String CAMBIAR_TEL   = "CAMBIAR_TEL";
    private static final String CAMBIAR_EMAIL = "CAMBIAR_EMAIL";
    private static final String ELIMINAR_CUENTA = "ELIMINAR_CUENTA";
    private static final String SALIR         = "SALIR";    


    public VistaGUI() throws Exception {
        super("Banco CR - Cajero Automático");
        ctrl = new ControladorCajero(new PersistenciaXML());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);

        // Menú lateral
        JPanel menu = new JPanel(new GridLayout(0,1,5,5));
        menu.setBackground(new Color(0,120,0));
        menu.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        String[] textos = {
            "Crear Cliente", "Crear Cuenta", "Cambiar PIN",
            "Depositar", "Retirar", "Saldo",
            "Depositar USD", "Retirar USD", "Saldo USD",
            "Transferir", "Estado de Cuenta", "Historial",
            "Tipo Cambio Compra", "Tipo Cambio Venta",
            "Cambiar Teléfono", "Cambiar Email", "Eliminar Cuenta",
            "Salir"
        };

        String[] cmds = {
            CREAR_CLIENTE, CREAR_CUENTA, CAMBIAR_PIN,
            DEPOSITAR, RETIRAR, SALDO,
            DEPOSITAR_USD, RETIRAR_USD, SALDO_USD,
            TRANSFERIR, ESTADO_CUENTA, HISTORIAL,
            CAMBIO_COMPRA, CAMBIO_VENTA,
            CAMBIAR_TEL, CAMBIAR_EMAIL, ELIMINAR_CUENTA,
            SALIR
        };
        for(int i=0;i<textos.length;i++){
            JButton b = new JButton(textos[i]);
            b.setActionCommand(cmds[i]);
            b.setForeground(Color.WHITE);
            b.setBackground(new Color(0,150,0));
            b.setFocusPainted(false);
            b.addActionListener(this::onMenuClick);
            menu.add(b);
        }

        // Panel central con CardLayout
        cards = new JPanel(new CardLayout());
        cards.setBackground(new Color(0,100,0));
        cards.add(crearPanelMenu(),          MENU);
        cards.add(crearPanelCliente(),      CREAR_CLIENTE);
        cards.add(crearPanelCrearCuenta(), CREAR_CUENTA);
        cards.add(crearPanelCambiarPin(),   CAMBIAR_PIN);
        cards.add(crearPanelDepositar(),    DEPOSITAR);
        cards.add(crearPanelRetirar(),      RETIRAR);
        cards.add(crearPanelDepositarUsd(), DEPOSITAR_USD);
        cards.add(crearPanelRetirarUsd(),   RETIRAR_USD);
        cards.add(crearPanelSaldo(),        SALDO);
        cards.add(crearPanelSaldoUsd(),     SALDO_USD);
        cards.add(crearPanelHistorial(),    HISTORIAL);
        cards.add(crearPanelEstadoCuenta(), ESTADO_CUENTA);
        cards.add(crearPanelCambioCompra(), CAMBIO_COMPRA);
        cards.add(crearPanelCambioVenta(),  CAMBIO_VENTA);
        cards.add(crearPanelCambiarTelefono(), CAMBIAR_TEL);
        cards.add(crearPanelCambiarEmail(), CAMBIAR_EMAIL);
        cards.add(crearPanelEliminarCuenta(), ELIMINAR_CUENTA);
        cards.add(crearPanelTransferir(),   TRANSFERIR);

        // Agregar al layout principal
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(menu,  BorderLayout.WEST);
        getContentPane().add(cards, BorderLayout.CENTER);
        mostrarCard(MENU);
        setVisible(true);
    }

    // Escucha el menú vía actionCommand (IDs)
    private void onMenuClick(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd) {
            case CREAR_CLIENTE: mostrarCard(CREAR_CLIENTE); break;
            case CREAR_CUENTA:  mostrarCard(CREAR_CUENTA);  break;
            case CAMBIAR_PIN:   mostrarCard(CAMBIAR_PIN);   break;
            case DEPOSITAR:     mostrarCard(DEPOSITAR);     break;
            case RETIRAR:       mostrarCard(RETIRAR);       break;
            case DEPOSITAR_USD: mostrarCard(DEPOSITAR_USD); break;
            case RETIRAR_USD:   mostrarCard(RETIRAR_USD);   break;
            case SALDO:         mostrarCard(SALDO);         break;
            case SALDO_USD:     mostrarCard(SALDO_USD);     break;
            case HISTORIAL:     mostrarCard(HISTORIAL);     break;
            case ESTADO_CUENTA: mostrarCard(ESTADO_CUENTA); break;
            case TRANSFERIR:    mostrarCard(TRANSFERIR);    break;
            case CAMBIO_COMPRA: mostrarCard(CAMBIO_COMPRA); break;
            case CAMBIO_VENTA:  mostrarCard(CAMBIO_VENTA);  break;
            case CAMBIAR_TEL:   mostrarCard(CAMBIAR_TEL);   break;
            case CAMBIAR_EMAIL: mostrarCard(CAMBIAR_EMAIL); break;
            case ELIMINAR_CUENTA: mostrarCard(ELIMINAR_CUENTA); break;
            case SALIR:         System.exit(0);             break;
            default:            mostrarCard(MENU);          break;
        }
    }

    // Muestra la tarjeta indicada
    private void mostrarCard(String id) {
        CardLayout cl = (CardLayout)cards.getLayout();
        cl.show(cards, id);
    }

    // Panel de bienvenida
    private JPanel crearPanelMenu() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.add(new JLabel("<html><h1 style='color:white'>Bienvenido al Banco CR</h1></html>"));
        return p;
    }

    // -------------------------
    // Aquí van tus paneles con formularios completos
    // -------------------------

    private JPanel crearPanelCliente() {
        GridLayoutPanel p = new GridLayoutPanel(5);
        JTextField nombre = new JTextField(), id = new JTextField();
        JTextField tel = new JTextField(), email = new JTextField();
        JButton ok = new JButton("Crear Cliente");
        ok.addActionListener(ev -> {
            String t = tel.getText().trim(), e = email.getText().trim();
            if (!Validacion.validarTelefono(t)) {
                JOptionPane.showMessageDialog(this, "Teléfono inválido (8 dígitos).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!Validacion.validarCorreo(e)) {
                JOptionPane.showMessageDialog(this, "Correo inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                ctrl.crearCliente(nombre.getText().trim(), id.getText().trim(), t, e);
                JOptionPane.showMessageDialog(this, "Cliente creado.");
                mostrarCard(MENU);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        p.addPair("Nombre:", nombre);
        p.addPair("ID Cliente:", id);
        p.addPair("Teléfono:", tel);
        p.addPair("Email:", email);
        p.addPair("", ok);
        return p;
    }

private JPanel crearPanelCrearCuenta() {
    GridLayoutPanel p = new GridLayoutPanel(4);
    JTextField txtId = new JTextField();
    JTextField txtMonto = new JTextField();
    JButton ok = new JButton("Crear Cuenta");

    ok.addActionListener(ev -> {
        try {
            String id = txtId.getText().trim();
            long deposito = Long.parseLong(txtMonto.getText().trim());

            String pinGenerado = Validacion.generarPinToken();
            
            JOptionPane.showMessageDialog(this, pinGenerado);
            
            ctrl.crearCuenta(id, pinGenerado, deposito);

            JOptionPane.showMessageDialog(this,
                "Cuenta creada exitosamente.\nSu PIN temporal es: " + pinGenerado,
                "PIN generado",
                JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    p.addPair("ID Cliente:", txtId);
    p.addPair("Depósito (₡):", txtMonto);
    p.addPair("", ok);
    return p;
}


    private JPanel crearPanelCambiarPin() {
        GridLayoutPanel p = new GridLayoutPanel(4);
        JTextField num = new JTextField();
        JPasswordField pinA = new JPasswordField(), pinN = new JPasswordField();
        JButton ok = new JButton("Cambiar PIN");
        ok.addActionListener(ev -> {
            String oldPin = new String(pinA.getPassword()).trim();
            String newPin = new String(pinN.getPassword()).trim();
            if (!Validacion.validarPin(oldPin) || !Validacion.validarPin(newPin)) {
                JOptionPane.showMessageDialog(this, "El PIN debe tener 6 dígitos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                ctrl.cambiarPin(num.getText().trim(), oldPin, newPin);
                JOptionPane.showMessageDialog(this, "PIN cambiado.");
                mostrarCard(MENU);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        p.addPair("Número Cuenta:", num);
        p.addPair("PIN Actual:", pinA);
        p.addPair("PIN Nuevo:", pinN);
        p.addPair("", ok);
        return p;
    }

    private JPanel crearPanelDepositar() {
        GridLayoutPanel p = new GridLayoutPanel(3);
        JTextField num = new JTextField(), monto = new JTextField();
        JButton ok = new JButton("Depositar");
        ok.addActionListener(ev -> {
            try {
                ctrl.depositarColones(num.getText().trim(),
                                      Long.parseLong(monto.getText().trim()));
                JOptionPane.showMessageDialog(this, "Depósito exitoso.");
                mostrarCard(MENU);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        p.addPair("Número Cuenta:", num);
        p.addPair("Monto (₡):", monto);
        p.addPair("", ok);
        return p;
    }

    private JPanel crearPanelRetirar() {
        GridLayoutPanel p = new GridLayoutPanel(5);
        JTextField num = new JTextField(), codigo = new JTextField(), monto = new JTextField();
        JPasswordField pin = new JPasswordField();
        JButton ok = new JButton("Retirar");
        ok.addActionListener(ev -> {
            try {
                Transaccion t = ctrl.retirarConSms(num.getText().trim(),
                                                   new String(pin.getPassword()).trim(),
                                                   codigo.getText().trim(),
                                                   Long.parseLong(monto.getText().trim()));
                JOptionPane.showMessageDialog(this, "Retiro: " + t);
                mostrarCard(MENU);
            } catch (Exception ex) {
                String msg = ex.getMessage();
                if (msg != null && msg.toLowerCase().contains("bloqueada")) {
                    JOptionPane.showMessageDialog(this, "⚠️ Cuenta bloqueada. Se enviará una notificación al correo del cliente.");
                    // Simulación de correo + SMS
                    System.out.println("📧 Enviando email al cliente...");
                    System.out.println("📱 Enviando SMS de bloqueo al número registrado...");
                } else {
                    JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
        p.addPair("Número Cuenta:", num);
        p.addPair("PIN:", pin);
        p.addPair("Código SMS:", codigo);
        p.addPair("Monto (₡):", monto);
        p.addPair("", ok);
        return p;
    }

    private JPanel crearPanelDepositarUsd() {
        GridLayoutPanel p = new GridLayoutPanel(3);
        JTextField num = new JTextField(), usd = new JTextField();
        JButton ok = new JButton("Depositar USD");
        ok.addActionListener(ev -> {
            try {
                double m = Double.parseDouble(usd.getText().trim());
                ctrl.depositarDolares(num.getText().trim(), m);
                JOptionPane.showMessageDialog(this,
                    String.format("Se depositaron %.2f USD", m));
                mostrarCard(MENU);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        p.addPair("Número Cuenta:", num);
        p.addPair("Monto (USD):", usd);
        p.addPair("", ok);
        return p;
    }

private JPanel crearPanelRetirarUsd() {
    GridLayoutPanel p = new GridLayoutPanel(5);
    JTextField num = new JTextField(), codigo = new JTextField(), usd = new JTextField();
    JPasswordField pin = new JPasswordField();
    JButton ok = new JButton("Retirar USD");

    ok.addActionListener(ev -> {
        try {
            double m = Double.parseDouble(usd.getText().trim());
            ctrl.retirarDolares(
                num.getText().trim(),
                new String(pin.getPassword()).trim(),
                codigo.getText().trim(),
                m
            );
            JOptionPane.showMessageDialog(this,
                String.format("Se retiraron %.2f USD", m));
            mostrarCard(MENU);
        } catch (Exception ex) {
            String msg = ex.getMessage();
            if (msg != null && msg.toLowerCase().contains("bloqueada")) {
                JOptionPane.showMessageDialog(this,
                    "⚠️ Cuenta bloqueada. Se enviará una notificación al correo del cliente.");
                System.out.println("📧 Enviando email al cliente...");
                System.out.println("📱 Enviando SMS de bloqueo...");
            } else {
                JOptionPane.showMessageDialog(this, msg,
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });  

    p.addPair("Número Cuenta:", num);
    p.addPair("PIN:", pin);
    p.addPair("Código SMS:", codigo);
    p.addPair("Monto (USD):", usd);
    p.addPair("", ok);
    return p;
}


    private JPanel crearPanelSaldo() {
        GridLayoutPanel p = new GridLayoutPanel(3);
        JTextField num = new JTextField();
        JPasswordField pin = new JPasswordField();
        JButton ok = new JButton("Consultar Saldo");
        ok.addActionListener(ev -> {
            try {
                long s = ctrl.consultarSaldo(
                    num.getText().trim(),
                    new String(pin.getPassword()).trim()
                );
                JOptionPane.showMessageDialog(this, "Saldo: " + s + " ₡");
                mostrarCard(MENU);
            } catch (Exception ex) {
                String msg = ex.getMessage();
                if (msg != null && msg.toLowerCase().contains("bloqueada")) {
                    JOptionPane.showMessageDialog(this,
                        "⚠️ Cuenta bloqueada. Se enviará una notificación al correo del cliente.");
                    System.out.println("📧 Enviando email al cliente...");
                    System.out.println("📱 Enviando SMS de bloqueo al número registrado...");
                } else {
                    JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        p.addPair("Número Cuenta:", num);
        p.addPair("PIN:", pin);
        p.addPair("", ok);
        return p;
    }
 
    private JPanel crearPanelSaldoUsd() {
        GridLayoutPanel p = new GridLayoutPanel(3);
        JTextField num = new JTextField();
        JPasswordField pin = new JPasswordField();
        JButton ok = new JButton("Consultar Saldo USD");
        ok.addActionListener(ev -> {
            try {
                double s = ctrl.consultarSaldoDolares(num.getText().trim(),
                                                      new String(pin.getPassword()).trim());
                JOptionPane.showMessageDialog(this,
                    String.format("Saldo USD: %.2f", s));
                mostrarCard(MENU);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        p.addPair("Número Cuenta:", num);
        p.addPair("PIN:", pin);
        p.addPair("", ok);
        return p;
    }


private JPanel crearPanelHistorial() {
    GridLayoutPanel p = new GridLayoutPanel(3);
    JTextField num = new JTextField();
    JPasswordField pin = new JPasswordField();
    JButton ok = new JButton("Ver Historial");

    ok.addActionListener(ev -> {
        try {
            List<Transaccion> lista = ctrl.consultarTransacciones(
                num.getText().trim(),
                new String(pin.getPassword()).trim()
            );
            StringBuilder sb = new StringBuilder();
            for (Transaccion t : lista) sb.append(t).append("\n");
            JOptionPane.showMessageDialog(this, sb.toString());
            mostrarCard(MENU);
        } catch (Exception ex) {
            String msg = ex.getMessage();
            if (msg != null && msg.toLowerCase().contains("bloqueada")) {
                JOptionPane.showMessageDialog(this,
                    "⚠️ Cuenta bloqueada. Se enviará una notificación al correo del cliente.");
                System.out.println("📧 Enviando email al cliente...");
                System.out.println("📱 Enviando SMS de bloqueo...");
            } else {
                JOptionPane.showMessageDialog(this, msg,
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }    
    });  
    p.addPair("Número Cuenta:", num);
    p.addPair("PIN:", pin);
    p.addPair("", ok);
    return p;
}

   private JPanel crearPanelTransferir() {
    GridLayoutPanel p = new GridLayoutPanel(6);
    JTextField txtOrigen  = new JTextField();
    JPasswordField txtPin = new JPasswordField();
    JTextField txtSms     = new JTextField();
    JTextField txtDestino = new JTextField();
    JTextField txtMonto   = new JTextField();
    JButton ok = new JButton("Transferir");
    ok.addActionListener(ev -> {
        try {
            ctrl.transferir(
                txtOrigen.getText().trim(),
                new String(txtPin.getPassword()).trim(),
                txtSms.getText().trim(),
                txtDestino.getText().trim(),
                Long.parseLong(txtMonto.getText().trim())
            );
            JOptionPane.showMessageDialog(this,
                "Transferencia realizada con éxito.");
            mostrarCard(MENU);
        } catch (Exception ex) {
            String msg = ex.getMessage();
            if (msg != null && msg.toLowerCase().contains("bloqueada")) {
                JOptionPane.showMessageDialog(this, "⚠️ Cuenta bloqueada. Se enviará una notificación al correo del cliente.");
                // Simulación de correo + SMS
                System.out.println("📧 Enviando email al cliente...");
                System.out.println("📱 Enviando SMS de bloqueo al número registrado...");
            } else {
                JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }   
    });

    p.addPair("Cuenta Origen:",   txtOrigen);
    p.addPair("PIN:",              txtPin);
    p.addPair("Código SMS:",       txtSms);
    p.addPair("Cuenta Destino:",   txtDestino);
    p.addPair("Monto (₡):",        txtMonto);
    p.addPair("",                  ok);
    return p;
}
private JPanel crearPanelEstadoCuenta() {
    GridLayoutPanel p = new GridLayoutPanel(4);
    JTextField txtCuenta = new JTextField();
    JPasswordField txtPin = new JPasswordField();
    JTextArea txtInfo = new JTextArea(10, 30);
    txtInfo.setEditable(false);
    JScrollPane scroll = new JScrollPane(txtInfo);
    JButton ok = new JButton("Ver Estado");

    ok.addActionListener(ev -> {
        try {
            String info = ctrl.obtenerEstadoCuenta(
                txtCuenta.getText().trim(),
                new String(txtPin.getPassword()).trim()
            );
            txtInfo.setText(info);
        } catch (Exception ex) {
            String msg = ex.getMessage();
            if (msg != null && msg.toLowerCase().contains("bloqueada")) {
                JOptionPane.showMessageDialog(this,
                    "⚠️ Cuenta bloqueada. Se enviará una notificación al correo del cliente.");
                System.out.println("📧 Enviando email al cliente...");
                System.out.println("📱 Enviando SMS de bloqueo...");
            } else {
                JOptionPane.showMessageDialog(this, msg,
                    "Error", JOptionPane.ERROR_MESSAGE);
            } 
        }   
    });                    

    p.addPair("Número Cuenta:", txtCuenta);
    p.addPair("PIN:",           txtPin); 
    p.addPair("", ok);
    p.addPair("Resultado:", scroll);
    return p;
}
private JPanel crearPanelCambioCompra() {
    JPanel p = new JPanel();
    p.setOpaque(false);
    double valor = ctrl.getTipoCambioCompra();
    JLabel lbl = new JLabel("Tipo de Cambio (Compra): " + valor);
    lbl.setForeground(Color.WHITE);
    p.add(lbl);
    return p;
}

private JPanel crearPanelCambioVenta() {
    JPanel p = new JPanel();
    p.setOpaque(false);
    double valor = ctrl.getTipoCambioVenta();
    JLabel lbl = new JLabel("Tipo de Cambio (Venta): " + valor);
    lbl.setForeground(Color.WHITE);
    p.add(lbl);
    return p;
}
    private JPanel crearPanelCambiarTelefono() {
        GridLayoutPanel p = new GridLayoutPanel(3);
        JTextField id = new JTextField();
        JTextField nuevo = new JTextField();
        JButton ok = new JButton("Actualizar Teléfono");
        ok.addActionListener(ev -> {
            String t = nuevo.getText().trim();
            if (!Validacion.validarTelefono(t)) {
                JOptionPane.showMessageDialog(this, "Teléfono inválido (8 dígitos).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try { ctrl.cambiarTelefono(id.getText().trim(), t);
                JOptionPane.showMessageDialog(this, "Teléfono actualizado.");
                mostrarCard(MENU);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        p.addPair("ID Cliente:", id);
        p.addPair("Nuevo Teléfono:", nuevo);
        p.addPair("", ok);
        return p;
    }

    private JPanel crearPanelCambiarEmail() {
        GridLayoutPanel p = new GridLayoutPanel(3);
        JTextField id = new JTextField();
        JTextField nuevo = new JTextField();
        JButton ok = new JButton("Actualizar Email");
        ok.addActionListener(ev -> {
            String e = nuevo.getText().trim();
            if (!Validacion.validarCorreo(e)) {
                JOptionPane.showMessageDialog(this, "Correo inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try { ctrl.cambiarEmail(id.getText().trim(), e);
                JOptionPane.showMessageDialog(this, "Email actualizado.");
                mostrarCard(MENU);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            
    });

    p.addPair("ID Cliente:", id);
    p.addPair("Nuevo Email:", nuevo);
    p.addPair("", ok);
    return p;
}
private JPanel crearPanelEliminarCuenta() {
    GridLayoutPanel p = new GridLayoutPanel(3);
    JTextField num = new JTextField();
    JPasswordField pin = new JPasswordField();
    JButton ok = new JButton("Eliminar Cuenta");

    ok.addActionListener(ev -> {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea eliminar esta cuenta? Esta acción no se puede deshacer.",
            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                ctrl.eliminarCuenta(num.getText().trim(), new String(pin.getPassword()).trim());
                JOptionPane.showMessageDialog(this, "Cuenta eliminada exitosamente.");
                mostrarCard(MENU);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }   
    }})
    ;

    p.addPair("Número Cuenta:", num);
    p.addPair("PIN:", pin);
    p.addPair("", ok);
    return p;
}


    /** Panel helper con dos columnas label+campo */
    private static class GridLayoutPanel extends JPanel {
        GridLayoutPanel(int rows) {
            super(new GridLayout(rows,2,5,5));
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        }
        void addPair(String label, JComponent comp) {
            JLabel l = new JLabel(label);
            l.setForeground(Color.WHITE);
            add(l); add(comp);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new VistaGUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
       
        });}}
    
    

