package Pr2;

public class Objetos {
    enum TipoElemento {
        VACIO("*", null),
        MINA("M", null),
        PEPITA("P", null),
        JUGADOR("J", Jugador.class);

        private final String simbolo;
        private final Class<?> claseAsociada;
        TipoElemento(String simbolo, Class<?> claseAsociada) {
            this.simbolo = simbolo;
            this.claseAsociada = claseAsociada;
        }

        public String getSimbolo() {
            return simbolo;
        }

        public Class<?> getClaseAsociada() {
            return claseAsociada;
        }

        @Override
        public String toString() {
            return simbolo;
        }
}
}
