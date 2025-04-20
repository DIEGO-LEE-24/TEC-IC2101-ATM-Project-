package modelo;

import java.util.regex.Pattern;

public class Validacion {

    private static final Pattern patronCorreo = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern patronTelefono = Pattern.compile("^[0-9]{8}$");
    private static final Pattern patronPIN = Pattern.compile("^[0-9]{6}$");

    public static boolean validarCorreo(String correo) {
        return patronCorreo.matcher(correo).matches();
    }

    public static boolean validarTelefono(String tel) {
        return patronTelefono.matcher(tel).matches();
    }

    public static boolean validarPin(String pin) {
        return patronPIN.matcher(pin).matches();
    }


    public static String generarPinToken() {
        String parte1 = String.valueOf((int)(Math.random() * 90 + 10)); // 10~99
        String parte2 = String.valueOf((int)(Math.random() * 90 + 10)); // 10~99
        return parte1 + parte2;
    }


    public static boolean validarEmail(String email) {
        return patronCorreo.matcher(email).matches();
    }
}
