/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.senai.fatesg.avcar.swing.views.telas.utils;

/**
 *
 * @author lucio-aguiar
 */
public class FormatadorUtil {

    public static String extrairNumeros(javax.swing.JFormattedTextField campo) {
        String texto = campo.getText();
        return (texto != null) ? texto.replaceAll("[^0-9]", "") : "";
    }
    
    public static String formatarCpfCnpj(String doc) {
        if (doc == null) return "";
        String numeros = doc.replaceAll("[^0-9]", "");
        if (numeros.length() == 11) {
            return numeros.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        } else if (numeros.length() == 14) {
            return numeros.replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
        }
        return doc; // Retorna sem máscara se for inválido
    }

    public static String formatarTelefone(String tel) {
        if (tel == null) return "";
        String numeros = tel.replaceAll("[^0-9]", "");
        
        // Se veio com o DDI do Brasil (55) salvo no banco
        if (numeros.length() == 13 && numeros.startsWith("55")) { // 55 + 11 dígitos
            return numeros.replaceFirst("(\\d{2})(\\d{2})(\\d{5})(\\d{4})", "+$1 ($2) $3-$4");
        } else if (numeros.length() == 12 && numeros.startsWith("55")) { // 55 + 10 dígitos
            return numeros.replaceFirst("(\\d{2})(\\d{2})(\\d{4})(\\d{4})", "+$1 ($2) $3-$4");
        } else if (numeros.length() == 11) {
            return numeros.replaceFirst("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
        } else if (numeros.length() == 10) {
            return numeros.replaceFirst("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
        }
        
        return tel; // Se tiver um tamanho esquisito, não quebra, só retorna original
    }

    public static String formatarPlaca(String placa) {
        if (placa == null) return "";
        placa = placa.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
        if (placa.length() == 7) {
            return placa.substring(0, 3) + "-" + placa.substring(3);
        }
        return placa.toUpperCase();
    }

    public static String formatarQuilometragem(int km) {
        return java.text.NumberFormat.getNumberInstance(new java.util.Locale("pt", "BR")).format(km) + " km";
    }
}
