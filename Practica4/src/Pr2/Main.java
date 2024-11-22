package Pr2;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

import Pr2.Objetos.TipoElemento;

public class Main {
	static ConcurrentHashMap<Posicion, TipoElemento> campo = new ConcurrentHashMap<>();
	static ArrayList<Jugador> jugadores = new ArrayList<>();
	static ArrayList<Posicion> posicionesJugadores;
	static Random random = new Random();
	static int X = 15;
	static int Y = 15;
	static CyclicBarrier barrera;

	public static void main(String[] args) {
		// Crear jugadores
		jugadores.add(new Jugador("Hugo", 1, "H"));
		jugadores.add(new Jugador("Sergio", 2,"S"));
		jugadores.add(new Jugador("Victor de Juan", 3, "V"));
		jugadores.add(new Jugador("Daniel", 4, "D"));

		// Crear máximo de objetos
		int maxJugadores = jugadores.size();
		int maxMina = maxJugadores / 2;
		int maxPepita = maxJugadores * 3;

		// Inicializar el campo con valores predeterminados
		for (int y = 0; y < Y; y++) {
			for (int x = 0; x < X; x++) {
				campo.put(new Posicion(x, y), TipoElemento.VACIO);
			}
		}

		// Asignar posiciones iniciales a los jugadores
		posicionesJugadores = new ArrayList<>();
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

		// Establecer las posiciones de las minas
		for (int i = 0; i < maxMina; i++) {
			int MX, MY;
			do {
				MX = random.nextInt(X);
				MY = random.nextInt(Y);
			} while (campo.get(new Posicion(MX, MY)) != TipoElemento.VACIO);
			campo.put(new Posicion(MX, MY), TipoElemento.MINA);
		}

		// Establecer las posiciones de las pepitas
		for (int i = 0; i < maxPepita; i++) {
			int PX, PY;
			do {
				PX = random.nextInt(X);
				PY = random.nextInt(Y);
			} while (campo.get(new Posicion(PX, PY)) != TipoElemento.VACIO);
			campo.put(new Posicion(PX, PY), TipoElemento.PEPITA);
		}
		
		// Concurrencia con el CyclicBarrier
		barrera = new CyclicBarrier(jugadores.size(), imprimirTablero());
		System.out.println("Estamos en la barrera todos los hilos!");

	       for (Jugador jugador : jugadores) {
	            new Thread(jugador).start();
	        }
		
	}

	// Método para imprimir el tablero
	public static Runnable imprimirTablero() {
		for (int y = 0; y < Y; y++) {
			for (int x = 0; x < X; x++) {
				Posicion pos = new Posicion(x, y);
				TipoElemento elemento = campo.get(pos);
				if (elemento == null) {
					System.out.print("*  ");
				} else {
					System.out.print(elemento.getSimbolo() + "  ");
				}
			}
			System.out.println();
		}
		System.out.println();
		return null;
	}
	
	
	
	
	public static void Game(ArrayList<Posicion> posicionesJugadores) {
		// Movimiento de los jugadores por turnos
		int[] fortuna = new int[jugadores.size()]; // Cada jugador tiene su propio contador de pepitas
		boolean partidaTermina = false; //

		do {
				
		        System.out.println("Turno de " + jugadores.get(0).getName());
		        Posicion posActual = posicionesJugadores.get(0); // Toma la posición actual del ArrayList de posiciones del jugador
		        int JugX = posActual.getX();
		        int JugY = posActual.getY();
		        int seleccion = random.nextInt(1, 9); // Movimiento aleatorio para las fichas
		        switch (seleccion) {
		        case 1 -> {
		            JugX++;
		            System.out.println("X - Derecha");
		        }
		        case 2 -> {
		            JugX--;
		            System.out.println("X - Izquierda");
		        }
		        case 3 -> {
		            JugY++;
		            System.out.println("Y - Abajo");
		        }
		        case 4 -> {
		            JugY--;
		            System.out.println("Y - Arriba");
		        }
		        case 5 -> {
		            JugX++;
		            JugY--;
		            System.out.println("X - Derecha, Y - Arriba");
		        }
		        case 6 -> {
		            JugX++;
		            JugY++;
		            System.out.println("X - Derecha, Y - Abajo");
		        }
		        case 7 -> {
		            JugX--;
		            JugY--;
		            System.out.println("X - Izquierda, Y - Arriba");
		        }
		        case 8 -> {
		            JugX--;
		            JugY++;
		            System.out.println("X - Izquierda, Y - Abajo");
		        }
		        default -> System.out.println("Movimiento erróneo");
		        }

		        // Verificar si la nueva posición está dentro de los límites del mapa
		        if (JugX >= 0 && JugX < X && JugY >= 0 && JugY < Y) {
		            // Solo actualizar la posición si está dentro del mapa
		            Posicion nuevaPos = new Posicion(JugX, JugY);
		            TipoElemento elemento = campo.get(nuevaPos);
		            switch (elemento) {
		            case PEPITA -> {
		                fortuna[0]++;
		                System.out.println(jugadores.get(0).getName() + " ha recogido una pepita. Fortuna: " + fortuna[0]);
		            }
		            case MINA -> {
		                System.out.println(jugadores.get(0).getName() + " ha pisado una mina. ¡Queda fuera del juego!");
		                posicionesJugadores.remove(0);
		                jugadores.remove(0);
		                if (jugadores.isEmpty()) {
		                    System.out.println("FINAL DE PARTIDA, Todos los jugadores han perdido.");
		                    partidaTermina = true;
		                }
		            }
		            case JUGADOR -> {
		                System.out.println("Ya hay un jugador en esa posición.");
		            }
		            default -> System.out.println(jugadores.get(0).getName() + ": movimiento valido.");
		            }

		            // Cambiar variables para limpiar la casilla donde estaba el jugador y actualizar la nueva
		            campo.put(posActual, TipoElemento.VACIO);
		            campo.put(nuevaPos, TipoElemento.JUGADOR);
		            posicionesJugadores.set(0, nuevaPos);
		        } else {
		            // Borde para que el jugador no se salga del mapa, ese turno seria invalido y tendria que esperar al siguiente para jugar
		            System.out.println("El movimiento está fuera del mapa. El jugador no se mueve.");
		        }

		        // Si la fortuna es igual a 4 ha ganada (cambiar el numero para determinar el valor de la victoria)
		        int victoria = 3;
		        if (fortuna[0] >= victoria) {
		            System.out.println(jugadores.get(0).getName() + " ha ganado la partida con " + victoria + " pepitas.");
		            partidaTermina = true;
		        }

		        // Sleep para hacer una pausa entre turnos y poder ver la partida mas tranquilo, ajustable a la necesidad de lectura 
		        try {
		            Thread.sleep(1000);
		        } catch (InterruptedException e) {
		            System.err.println("Error en la pausa: " + e.getMessage());
		        }
		} while (!partidaTermina);
	}
}
