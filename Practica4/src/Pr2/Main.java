package Pr2;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

import Pr2.Objetos.TipoElemento;

/**
 * Clase Main del juego de recolecta de pepitas y minas.
 *
 * Reglas: - Los jugadores se mueven aleatoriamente en el tablero. - Cada turno,
 * los jugadores pueden recoger pepitas, pisar minas, o moverse. - Si un jugador
 * recoge el número necesario de pepitas, gana la partida. - Si un jugador pisa
 * una mina, muere y se acaba la partida.
  * @author Sergio Mostacero.
 * @author Hugo Jiménez.
 */
public class Main {

	/**
	 * Tablero del juego representado como un ConcurrentHashMap de posiciones y
	 * elementos.
	 */
	static ConcurrentHashMap<Posicion, TipoElemento> campo = new ConcurrentHashMap<>();

	/** Lista de jugadores. */
	static ArrayList<Jugador> jugadores = new ArrayList<>();

	/** Posiciones iniciales de los jugadores. */
	static ArrayList<Posicion> posicionesJugadores = new ArrayList<>();

	/**
	 * Generador de números aleatorios para los movimientos
	 */
	static Random random = new Random();

	/** Dimensiones del tablero (15x15), se puede cambiar segun los gustos del player. */
	static int X = 15;
	static int Y = 15;

	/** Barrera cíclica que sincroniza los turnos de todos los jugadores. */
	static CyclicBarrier barrera;

	/** Boolean para indicar si el juego está activo. */
	static volatile boolean partidaActiva = true;

	/**
	 * Main, inicializa el tablero, los jugadores y los elementos.
	 */
	public static void main(String[] args) {
		// Crear jugadores
		jugadores.add(new Jugador("Hugo", 1, "H"));
		jugadores.add(new Jugador("Sergio", 2, "S"));
		jugadores.add(new Jugador("Victor de Juan", 3, "V"));
		jugadores.add(new Jugador("Daniel", 4, "D"));

		// Crea el tablero con casillas vacías
		for (int y = 0; y < Y; y++) {
			for (int x = 0; x < X; x++) {
				campo.put(new Posicion(x, y), TipoElemento.VACIO);
			}
		}

		// Asignar posiciones a los jugadores
		for (Jugador jugador : jugadores) {
			Posicion posInicial;
			do {
				int x = random.nextInt(X);
				int y = random.nextInt(Y);
				posInicial = new Posicion(x, y);
			} while (campo.get(posInicial) != TipoElemento.VACIO);
			posicionesJugadores.add(posInicial);
			campo.put(posInicial, TipoElemento.JUGADOR);
		}

		// Coloca minas en el tablero
		int maxMina = jugadores.size() / 2; // Número de minas: mitad del número de jugadores
		for (int i = 0; i < maxMina; i++) {
			Posicion posMina;
			do {
				int x = random.nextInt(X);
				int y = random.nextInt(Y);
				posMina = new Posicion(x, y);
			} while (campo.get(posMina) != TipoElemento.VACIO);
			campo.put(posMina, TipoElemento.MINA);
		}

		// Coloca pepitas en el tablero
		int maxPepita = jugadores.size() * 3; // Número de pepitas: tres veces el número de jugadores
		for (int i = 0; i < maxPepita; i++) {
			Posicion posPepita;
			do {
				int x = random.nextInt(X);
				int y = random.nextInt(Y);
				posPepita = new Posicion(x, y);
			} while (campo.get(posPepita) != TipoElemento.VACIO);
			campo.put(posPepita, TipoElemento.PEPITA);
		}

		// Inicializa la barrera para sincronizar turnos de todos los jugadores.
		barrera = new CyclicBarrier(jugadores.size(), Main::imprimirTablero);

		// Inicia los hilos de cada jugador.
		for (int i = 0; i < jugadores.size(); i++) {
			Jugador jugador = jugadores.get(i);
			jugador.setPosicion(posicionesJugadores.get(i));
			new Thread(jugador).start();
		}
	}

	/**
	 * Imprime el tablerocon los elementos en sus posiciones actualizadas.
	 */
	public static synchronized void imprimirTablero() {
		System.out.println("\nMapa actualizado con todos los movimientos:");
		for (int y = 0; y < Y; y++) {
			for (int x = 0; x < X; x++) {
				Posicion pos = new Posicion(x, y);
				TipoElemento elemento = campo.get(pos);
				if (elemento == null) {
					System.out.print("* "); // Casilla nula (no debería suceder)
				} else {
					System.out.print(elemento.getSimbolo() + " "); // Imprimir símbolo del elemento
				}
			}
			System.out.println(); // Nueva línea por cada fila
		}
		System.out.println();
	}

	/**
	 * Acaba la partida y declara un ganador.
	 * 
	 * @param ganador Nombre del jugador que ha ganado.
	 */
	public static void finalizarJuego(String ganador) {
		partidaActiva = false; // Cambiar estado de la partida
		System.out.println("\nJuego finalizado. Ganador: " + ganador);
		System.exit(0); // Finalizar ejecución del programa
	}
}
