package Pr1;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
	/**
	 * @param numHilo Número de hilos que queremos 
	 * @param encontrado Variable AtomicBoolean
	 * @param resultado Si es true es que el programa a encontrado la palabra
	 */
public class Main {
    private static final int numHilo = 4;
    private static final AtomicBoolean encontrado = new AtomicBoolean(false);
    private static String resultado = null;
    public static void main(String[] args) {
        String hashObjetivo = "b221d9dbb083a7f33428d7c2a3c3198ae925614d70210e28716ccaa7cd4ddb79";l        
        long inicio = System.currentTimeMillis();

        Thread[] hilo = new Thread[numHilo];
        int rangoPorHilo = 26 / numHilo; 

        for (int i = 0; i < numHilo; i++) {
            char start = (char) ('a' + i * rangoPorHilo);
            char end = (char) ('a' + (i + 1) * rangoPorHilo -1 );
            hilo[i] = new Thread(new FuerzaBrutaTask(hashObjetivo, start, end));
            hilo[i].start();
            
            try {
				hilo[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        long fin = System.currentTimeMillis();

        if (resultado != null) {
            System.out.println("Palabra encontrada: " + resultado);
        } else {
            System.out.println("Palabra no encontrada.");
        }

        System.out.println("Tiempo de ejecución: " + (fin - inicio) + " ms");
    }

    static class FuerzaBrutaTask implements Runnable {
        private final String hashObjetivo;
        private final char start;
        private final char end;

        public FuerzaBrutaTask(String hashObjetivo, char start, char end) {
            this.hashObjetivo = hashObjetivo;
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            for (char c1 = start; c1 <= end; c1++) {
                for (char c2 = 'a'; c2 <= 'z'; c2++) {
                    for (char c3 = 'a'; c3 <= 'z'; c3++) {
                        for (char c4 = 'a'; c4 <= 'z'; c4++) {
                            if (encontrado.get()) return; // Detener si ya se encontró

                            String palabra = "" + c1 + c2 + c3 + c4;
                            byte[] hash = getHash(palabra);

                            if (Arrays.equals(hash, hexStringToByteArray(hashObjetivo))) {
                                encontrado.set(true); // Marcar como encontrado
                                resultado = palabra; // Guardar resultado
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public static byte[] getHash(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(text.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

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
