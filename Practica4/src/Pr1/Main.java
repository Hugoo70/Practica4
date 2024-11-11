package Pr1;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class Main {
	public static void main(String[] args) {
		String hashObjetivo = "b221d9dbb083a7f33428d7c2a3c3198ae925614d70210e28716ccaa7cd4ddb79";
		long startTime = System.currentTimeMillis();

		ForkJoinPool pool = new ForkJoinPool();
		String resultado = pool.invoke(new FuerzaBrutaTask(hashObjetivo, 'a', 'z'));
		long endTime = System.currentTimeMillis();

		if (resultado != null) {
			System.out.println("Palabra encontrada: " + resultado);
		} else {
			System.out.println("Palabra no encontrada.");
		}
		System.out.println("Tiempo de ejecución: " + (endTime - startTime) + " ms");

	}

	public static class FuerzaBrutaTask extends RecursiveTask<String> {
		private final String hashObjetivo;
		private final char start;
		private final char end;

		public FuerzaBrutaTask(String hashObjetivo, char start, char end) {
			this.hashObjetivo = hashObjetivo;
			this.start = start;
			this.end = end;
		}

		@Override
		protected String compute() {
			for (char c1 = start; c1 <= end; c1++) {
				for (char c2 = start; c2 <= end; c2++) {
					for (char c3 = start; c3 <= end; c3++) {
						for (char c4 = start; c4 <= end; c4++) {
							String palabra = "" + c1 + c2 + c3 + c4;
							String hashHex = bytesToHex(getHash(palabra));
							if (hashHex.equals(hashObjetivo)) {
								return palabra;
							}
						}
					}
				}
			}
			return null;
		}
	}

	public static byte[] getHash(String text) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			return digest.digest(text.getBytes(StandardCharsets.UTF_8));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String bytesToHex(byte[] hash) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : hash) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}
}
