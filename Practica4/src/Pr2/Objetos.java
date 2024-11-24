package Pr2;

/**
 * Clase Objetos.
 * Cada tipo de elemento tiene un símbolo asociado y una clase relacionada.
 */
public class Objetos {

    /**
     * Enum que define los tipos de elementos del juego.
     */
    enum TipoElemento {
        /** Celda vacía. */
        VACIO("*", null),

        /** Celda con una mina. Si un jugador pisa una mina, queda eliminado. */
        MINA("M", null),

        /** Celda con una pepita. Los jugadores deben recolectar pepitas para ganar. */
        PEPITA("P", null),

        /** Celda con un jugador */
        JUGADOR("J", Jugador.class);

        /** Símbolo utilizado para representar un elemento en el tablero. */
        private final String simbolo;

        /** Clase asociada al elemento, si tiene una clase. */
        private final Class<?> claseAsociada;

        /**
         * Constructor del tipo de elemento.
         * @param simbolo Símbolo que representa el elemento.
         * @param claseAsociada Clase asociada al elemento, o null si no tiene.
         */
        TipoElemento(String simbolo, Class<?> claseAsociada) {
            this.simbolo = simbolo;
            this.claseAsociada = claseAsociada;
        }

        /**
         * Obtiene el símbolo asociado al elemento.
         * @return Símbolo del elemento.
         */
        public String getSimbolo() {
            return simbolo;
        }

        /**
         * Obtiene la clase asociada al elemento.
         * @return Clase asociada al elemento, o null si no tiene.
         */
        public Class<?> getClaseAsociada() {
            return claseAsociada;
        }

        /**
         * Devuelve la representación en cadena del elemento.
         * @return Símbolo asociado al elemento.
         */
        @Override
        public String toString() {
            return simbolo;
        }
    }
}
