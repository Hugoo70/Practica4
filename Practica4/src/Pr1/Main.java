package Pr1;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String hashObjetivo = "b221d9dbb083a7f33428d7c2a3c3198ae925614d70210e28716ccaa7cd4ddb79"; 
        
        long startTime = System.currentTimeMillis();  
        String resultado = buscarPalabraPorFuerzaBruta(hashObjetivo);
        
        long endTime = System.currentTimeMillis(); 

        if (resultado != null) {
            System.out.println("Palabra encontrada: " + resultado);
        } else {
            System.out.println("Palabra no encontrada.");
        }

        System.out.println("Tiempo de ejecución: " + (endTime - startTime) + " ms");
    }

    public static String buscarPalabraPorFuerzaBruta(String hashObjetivo) {
        for (char c1 = 'a'; c1 <= 'z'; c1++) {
            for (char c2 = 'a'; c2 <= 'z'; c2++) {
                for (char c3 = 'a'; c3 <= 'z'; c3++) {
                    for (char c4 = 'a'; c4 <= 'z'; c4++) {
                        String palabra = "" + c1 + c2 + c3 + c4;
                        byte[] hash = getHash(palabra);

                    }
                }
            }
        }
        return null;  
    }

    public static byte[] getHash(String text) {
        byte[] encodedhash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            encodedhash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodedhash;
    }

}
