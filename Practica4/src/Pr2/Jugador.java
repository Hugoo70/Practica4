package Pr2;

import Pr2.Objetos.TipoElemento;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;

/**
 * Clase que representa a un jugador en el juego.
 * Cada jugador puede moverse por el mapa, recolectar pepitas, y evitar minas.
 * El juego termina cuando un jugador alcanza el número de pepitas necesarias para ganar o si un jugador pisa una mina.
 */
public class Jugador implements Runnable {

    /** Nombre del jugador */
    private String name;

    /** Identificador único del jugador */
    private int id;

    /** Inicial del jugador para representarlo en el mapa */
    private String inicial;

    /** Cantidad de pepitas recolectadas por el jugador */
    private int fortuna = 0;

    /** Número de pepitas necesarias para ganar */
    private static final int PEPITAS_GANAR = 3;

    /** Estado del jugador, true si está vivo */
    private boolean vivo = true;

    /** Posición actual del jugador en el mapa */
    private Posicion posicion;

    /**
     * Constructor de la clase Jugador.
     * @param name Nombre del jugador.
     * @param id Identificador único del jugador.
     * @param inicial Inicial del jugador.
     */
    public Jugador(String name, int id, String inicial) {
        this.name = name;
        this.id = id;
        this.inicial = inicial;
    }

    /**
     * Establece la posición inicial del jugador en el mapa.
     * @param posicion Objeto Posicion que representa la posición inicial del jugador.
     */
    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    /**
     * Método ejecutado por el hilo del jugador.
     * Mientras el jugador esté vivo y la partida activa, realiza su turno y espera a los demás jugadores.
     */
    @Override
    public void run() {
        while (vivo && Main.partidaActiva) { // Solo sigue si está vivo y la partida está activa
            try {
                realizarTurno();
                Main.barrera.await(); // Espera a que todos los jugadores terminen su turno
            } catch (InterruptedException | BrokenBarrierException e) {
                System.err.println("Error en la barrera: " + e.getMessage());
                return;
            }
        }
    }

    /**
     * Realiza un turno del jugador, incluyendo movimiento y acciones según el tipo de casilla.
     */
    private void realizarTurno() {

        int JugX = posicion.getX();
        int JugY = posicion.getY();
        Random random = new Random();
        int movimiento = random.nextInt(8) + 1;

        // Movimiento aleatorio
        switch (movimiento) {
            case 1 -> JugX++; // Derecha
            case 2 -> JugX--; // Izquierda
            case 3 -> JugY++; // Abajo
            case 4 -> JugY--; // Arriba
            case 5 -> { JugX++; JugY--; } // Derecha arriba
            case 6 -> { JugX++; JugY++; } // Derecha abajo
            case 7 -> { JugX--; JugY--; } // Izquierda arriba
            case 8 -> { JugX--; JugY++; } // Izquierda abajo
        }

        // Validar límites del mapa
        if (JugX >= 0 && JugX < Main.X && JugY >= 0 && JugY < Main.Y) {
            Posicion nuevaPos = new Posicion(JugX, JugY);

            synchronized (Main.campo) {
                TipoElemento elemento = Main.campo.get(nuevaPos);
                switch (elemento) {
                    case PEPITA -> {
                        fortuna++;
                        System.out.println(name + " recoge una pepita. Fortuna: " + fortuna);
                        Main.campo.put(posicion, TipoElemento.VACIO); // Limpia posición anterior
                        Main.campo.put(nuevaPos, TipoElemento.JUGADOR); // Mover jugador
                        posicion = nuevaPos;
                        if (fortuna >= PEPITAS_GANAR) {
                            Main.finalizarJuego(name); // Finalizar el juego si gana
                        }
                    }
                    case MINA -> {
                        System.out.println(name + " pisa una mina y queda fuera!");
                        Main.campo.put(posicion, TipoElemento.VACIO); // Limpia posición anterior
                        vivo = false; // Jugador muere
                    }
                    case JUGADOR -> {
                        System.out.println(name + " choca con otro jugador. Movimiento anulado.");
                    }
                    default -> {
                        Main.campo.put(posicion, TipoElemento.VACIO); // Limpia posición anterior
                        Main.campo.put(nuevaPos, TipoElemento.JUGADOR); // Mover jugador
                        posicion = nuevaPos;
                        System.out.println(name + " se movió a una posición vacía.");
                    }
                }
            }
        } else {
            System.out.println(name + " intenta moverse fuera del mapa. Movimiento inválido.");
        }
    }

    /**
     * Representación en cadena del jugador.
     * @return Cadena que describe al jugador.
     */
    @Override
    public String toString() {
        return "Jugador [name=" + name + ", id=" + id + "]";
    }
}
