package Pr1;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Clase principal que implementa un programa que por fuerza bruta encuentra
 * una palabra con cifrado hash SHA-256.
 * 
 * @author Sergio Mostacero.
 * @author Hugo Jiménez.
 */
public class Main {

    /** 
     * Número de hilos para dividir el trabajo.
     */
    private static final int numHilo = 4;

    /** 
     * Indica si la palabra ya fue encontrada.
     */
    private static final AtomicBoolean encontrado = new AtomicBoolean(false);

    /** 
     * Variable para almacenar el resultado (palabra encontrada). 
     */
    private static String resultado = null;

    /**
     * Método principal que divide el trabajo entre hilos y busca la palabra.
     */
    public static void main(String[] args) {
        // Hash objetivo.
        String hashObjetivo = "b221d9dbb083a7f33428d7c2a3c3198ae925614d70210e28716ccaa7cd4ddb79";

        // Registro del tiempo inicial.
        long inicio = System.currentTimeMillis();

        // Array de hilos.
        Thread[] hilo = new Thread[numHilo];

        // Dividir el alfabeto español entre los hilos.
        int rangoPorHilo = 26 / numHilo;

        // Crea y ejecuta los hilos.
        for (int i = 0; i < numHilo; i++) {
            char start = (char) ('a' + i * rangoPorHilo);
            char end = (char) ('a' + (i + 1) * rangoPorHilo - 1);
            hilo[i] = new Thread(new FuerzaBrutaTask(hashObjetivo, start, end));
            hilo[i].start();

            try {
                hilo[i].join(); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Registro del tiempo final.
        long fin = System.currentTimeMillis();

        if (resultado != null) {
            System.out.println("Palabra encontrada: " + resultado);
        } else {
            System.out.println("Palabra no encontrada.");
        }

        System.out.println("Tiempo de ejecución: " + (fin - inicio) + " ms");
    }

    /**
     * Clase que busca palabras dentro de un rango específico.
     */
    static class FuerzaBrutaTask implements Runnable {
        private final String hashObjetivo;
        private final char start;
        private final char end;

        /**
         * Constructor.
         * 
         * @param hashObjetivo Hash objetivo.
         * @param start Letra inicial.
         * @param end Letra final.
         */
        public FuerzaBrutaTask(String hashObjetivo, char start, char end) {
            this.hashObjetivo = hashObjetivo;
            this.start = start;
            this.end = end;
        }

        /**
         * Método que ejecuta la búsqueda de palabras entre vocales.
         */
        @Override
        public void run() {
            for (char c1 = start; c1 <= end; c1++) {
                for (char c2 = 'a'; c2 <= 'z'; c2++) {
                    for (char c3 = 'a'; c3 <= 'z'; c3++) {
                        for (char c4 = 'a'; c4 <= 'z'; c4++) {
                            if (encontrado.get()) return;

                            String palabra = "" + c1 + c2 + c3 + c4;
                            byte[] hash = getHash(palabra);

                            if (Arrays.equals(hash, hexStringToByteArray(hashObjetivo))) {
                                encontrado.set(true); // Marcar como encontrado.
                                resultado = palabra; // Guardar el resultado.
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Genera el hash SHA-256 para una cadena.
     * 
     * @param text La cadena de texto a cifrar.
     * @return El hash en array de bytes.
     */
    public static byte[] getHash(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(text.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Convierte una cadena hexadecimal a un array de bytes.
     * 
     * @param s Cadena hexadecimal.
     * @return El Array de bytes.
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
