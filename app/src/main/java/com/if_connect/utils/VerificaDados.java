package com.if_connect.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerificaDados {

    public static boolean validarEmail(String email) {
        // Expressão regular para validar email
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+ifce\\.edu\\.br$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
