# Banco CR – Cajero Cardless (Proyecto 1)

**Curso**: IC2101 – Programación Orientada a Objetos I (I Semestre 2025)  
**Profesor**: Luis Pablo Soto Chaves  
**Integrantes**: (Agregar nombres de equipo)

---

## 📋 Descripción
Implementación de un Cajero Automático sin tarjeta (Cardless ATM) basado en Java y Swing, aplicando:
- Paradigma orientado a objetos (MVC).  
- Validación de entradas con expresiones regulares.  
- Persistencia en XML.  
- Encriptación de PIN (AES).  
- Simulación de servicios externos (BCCR, SMS).  

## 🛠 Tecnologías y Herramientas
- Java 17  
- Swing (interfaz gráfica)  
- NetBeans / Ant (`build.xml`)  
- XML (JAXB o parser manual)  
- Git & GitHub (control de versiones)  

## 🚀 Ejecución
1. **Clonar repositorio**
   ```bash
   git clone https://github.com/YourUser/TEC-IC2101-ATM-Project.git
   cd TEC-IC2101-ATM-Project
   ```
2. **Abrir en NetBeans** (o ejecutarlo desde tu IDE favorito)  
3. **Compilar y ejecutar**
   - Desde NetBeans: `Run Main.java`  
   - Por línea de comandos con Ant:
     ```bash
     ant run
     ```

## 📂 Estructura de directorios
```
CajeroCardlessProyecto1/
├── build/               # resultados de compilación
├── lib/                 # bibliotecas externas (si aplica)
├── nbproject/           # configuración de NetBeans
├── src/
│   ├── controlador/     # Controlador del flujo (MVC)
│   ├── modelo/          # Clases de dominio y lógica de negocio
│   ├── persistencia/    # Lectura/escritura de XML
│   ├── excepciones/     # Excepciones personalizadas
│   └── principal/       # Clase Main
├── vista/               # Interfaz Swing (GUI)
├── test/                # Pruebas unitarias (JUnit)
├── build.xml            # Script de Ant
├── Clientes.xml         # Datos iniciales de clientes
├── Cuentas.xml          # Datos iniciales de cuentas
├── Transacciones_CTA1.xml # Ejemplo de transacciones
└── README.md            # Documentación del proyecto
```

## ✅ Funcionalidades
- **Gestión de Clientes**: Crear, cambiar teléfono y email.  
- **Gestión de Cuentas**: Alta, cambio de PIN, baja.  
- **Transacciones**:
  - Depósito y retiro en colones y dólares  
  - Comisión automática (primeras 5 gratuitas)  
  - Ver historial y estado de cuenta  
  - Transferencias internas (mismo dueño)  
- **Seguridad**:
  - Validación de PIN (formato alfanumérico, 6 caracteres)  
  - Encriptación AES del PIN  
  - Bloqueo tras 3 intentos fallidos (PIN o código SMS)  
- **Servicios Externos (simulados)**:
  - Consulta en tiempo real de tipo de cambio BCCR  
  - Envío de palabra SMS para validación  
- **Persistencia**:
  - XML para clientes, cuentas y transacciones  

## 📖 Documentación & Diseño
- **`/documentacion/Documentacion_Final.pdf`**: Requisitos, diagramas UML (arquetectura y clases), matriz de cobertura y decisiones de diseño.  
- **`/UML/`**: Diagramas fuente en formato XMI o PDF.  

---

> **Nota**: Ajustar ramas y PRs según la [estrategia de Git](#).  
> Para más detalles, revisar la carpeta `documentacion/`.

# Banco CR – Cajero Cardless (Proyecto 1)

**Curso**: IC2101 – Programación Orientada a Objetos I (I Semestre 2025)  
**Profesor**: Luis Pablo Soto Chaves  
**Integrantes**: (Agregar nombres de equipo)

---

## 📋 Descripción
Implementación de un Cajero Automático sin tarjeta (Cardless ATM) basado en Java y Swing, aplicando:
- Paradigma orientado a objetos (MVC).  
- Validación de entradas con expresiones regulares.  
- Persistencia en XML.  
- Encriptación de PIN (AES).  
- Simulación de servicios externos (BCCR, SMS).  

## 🛠 Tecnologías y Herramientas
- Java 17  
- Swing (interfaz gráfica)  
- NetBeans / Ant (`build.xml`)  
- XML (JAXB o parser manual)  
- Git & GitHub (control de versiones)  

## 🚀 Ejecución
1. **Clonar repositorio**
   ```bash
   git clone https://github.com/YourUser/TEC-IC2101-ATM-Project.git
   cd TEC-IC2101-ATM-Project
   ```
2. **Abrir en NetBeans** (o ejecutarlo desde tu IDE favorito)  
3. **Compilar y ejecutar**
   - Desde NetBeans: `Run Main.java`  
   - Por línea de comandos con Ant:
     ```bash
     ant run
     ```

## 📂 Estructura de directorios
```
CajeroCardlessProyecto1/
├── build/               # resultados de compilación
├── lib/                 # bibliotecas externas (si aplica)
├── nbproject/           # configuración de NetBeans
├── src/
│   ├── controlador/     # Controlador del flujo (MVC)
│   ├── modelo/          # Clases de dominio y lógica de negocio
│   ├── persistencia/    # Lectura/escritura de XML
│   ├── excepciones/     # Excepciones personalizadas
│   └── principal/       # Clase Main
├── vista/               # Interfaz Swing (GUI)
├── test/                # Pruebas unitarias (JUnit)
├── build.xml            # Script de Ant
├── Clientes.xml         # Datos iniciales de clientes
├── Cuentas.xml          # Datos iniciales de cuentas
├── Transacciones_CTA1.xml # Ejemplo de transacciones
└── README.md            # Documentación del proyecto
```

## ✅ Funcionalidades
- **Gestión de Clientes**: Crear, cambiar teléfono y email.  
- **Gestión de Cuentas**: Alta, cambio de PIN, baja.  
- **Transacciones**:
  - Depósito y retiro en colones y dólares  
  - Comisión automática (primeras 5 gratuitas)  
  - Ver historial y estado de cuenta  
  - Transferencias internas (mismo dueño)  
- **Seguridad**:
  - Validación de PIN (formato alfanumérico, 6 caracteres)  
  - Encriptación AES del PIN  
  - Bloqueo tras 3 intentos fallidos (PIN o código SMS)  
- **Servicios Externos (simulados)**:
  - Consulta en tiempo real de tipo de cambio BCCR  
  - Envío de palabra SMS para validación  
- **Persistencia**:
  - XML para clientes, cuentas y transacciones  

## 📖 Documentación & Diseño
- **`/documentacion/Documentacion_Final.pdf`**: Requisitos, diagramas UML (arquetectura y clases), matriz de cobertura y decisiones de diseño.  
- **`/UML/`**: Diagramas fuente en formato XMI o PDF.  

---

> **Nota**: Ajustar ramas y PRs según la [estrategia de Git](#).  
> Para más detalles, revisar la carpeta `documentacion/`.

