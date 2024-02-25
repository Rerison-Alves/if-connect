package com.if_connect.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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

    public static boolean verificasenha(String senha) {
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(senha);
        return m.matches();
    }

    public static boolean validarDataNascimento(String datanascString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sdf.setLenient(false); // Não permite datas inválidas como 30/02/2023

        try {
            // Convertendo a string para um objeto Date
            Date dataNascimento = sdf.parse(datanascString);
            // Verificar se a data de nascimento é anterior à data atual
            Calendar calDataNascimento = Calendar.getInstance();
            if (dataNascimento != null) {
                calDataNascimento.setTime(dataNascimento);
            }
            Calendar calAtual = Calendar.getInstance();
            return calDataNascimento.before(calAtual);
        } catch (ParseException e) {
            // Se ocorrer uma exceção ao fazer o parsing, a data é inválida
            return false;
        }
    }
}
