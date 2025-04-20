# Project 1 – Object-Oriented Programming

**Institution:** Tecnológico de Costa Rica  
**Course:** IC2101 – Object-Oriented Programming  
**Professor:** Luis Pablo Soto Chaves  
**Student:**  

---

## 📦 Project Structure (ZIP)

```
Proyecto1_CajeroCardless/
│
├── codigo/
│   ├── Main.java
│   ├── VistaGUI.java
│   ├── controlador/
│   │   └── ControladorCajero.java
│   ├── modelo/
│   │   ├── Cliente.java
│   │   ├── Cuenta.java
│   │   ├── Transaccion.java
│   │   ├── EstadoCuenta.java
│   │   ├── Cifrado.java
│   │   ├── Validacion.java
│   │   ├── ServicioSMS.java
│   │   ├── ServicioBCCR.java
│   │   ├── TipoTransaccion.java
│   │   └── excepciones/
│   │       ├── PinInvalidoException.java
│   │       └── SaldoInsuficienteException.java
│   ├── persistencia/
│   │   ├── Persistencia.java
│   │   ├── PersistenciaXML.java
│   │   ├── ClienteList.java
│   │   ├── CuentaList.java
│   │   └── TransaccionWrapper.java
│
├── xml/
│   ├── Clientes.xml
│   ├── Cuentas.xml
│   └── Transacciones_CTA1.xml
│
├── documentacion/
│   └── Documentacion_Final.pdf
│
└── README.md
```

---

## ▶️ Execution Instructions

1. Open the project in NetBeans or another IDE compatible with Java 17.
2. Run the main class: `Main.java`
3. The GUI will launch, providing access to all ATM functionalities.

---

## ✅ Implemented Features

- Create client and account with input validation using regex.
- Deposit in colones or dollars (with BCCR exchange rate).
- Withdraw with PIN and SMS word verification (account blocked after 3 failed attempts).
- Check balance (in colones and USD).
- View transaction history.
- View detailed account status (including encrypted PIN and commissions).
- Transfer between accounts owned by the same client.
- Change PIN, phone number, and email.
- Delete accounts (also removes transaction XML).
- Automatic XML persistence for clients, accounts, and transactions.
- Full separation into model, view, controller, and persistence layers.

---

## 🔒 Technical Notes

- PIN encryption using AES (`Cifrado.java`).
- Simulated connection to BCCR (`ServicioBCCR.java`).
- SMS sending simulated via console (`ServicioSMS.java`).
- Centralized validation logic (`Validacion.java`).
- GUI implemented using `Swing` and `CardLayout`.
- Custom exceptions for PIN and balance errors.
- Interface-based connection between controller and model.

---

## 📝 Supporting Documentation

All theoretical content, design, and evidence are included in `documentacion/Documentacion_Final.pdf`, generated in LaTeX.

This document includes:
- Functional requirements
- Full UML design (class and architecture diagrams)
- Coverage matrix
- Design decisions and technical debt report
- GUI screenshots demonstrating functionality

---