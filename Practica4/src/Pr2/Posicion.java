package Pr2;

/**
 * Clase Posición.
 * Cada posición está definida por coordenadas X e Y.
 */
public class Posicion {

    /** Coordenada X */
    private final int x;

    /** Coordenada Y */
    private final int y;

    /**
     * Constructor de Posicion.
     * @param x Coordenada X de la posición.
     * @param y Coordenada Y de la posición.
     */
    public Posicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Devuelve la coordenada X.
     * @return Coordenada X.
     */
    public int getX() {
        return x;
    }

    /**
     * Devuelve la coordenada Y.
     * @return Coordenada Y.
     */
    public int getY() {
        return y;
    }

    /**
     * Comprueba si esta posición es igual a otro objeto.
     * Dos posiciones son iguales si sus coordenadas X e Y son idénticas.
     * @param o Objeto con el que se compara.
     * @return true si las posiciones son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Posicion that = (Posicion) o;
        return x == that.x && y == that.y;
    }

    /**
     * Calcula el código hash para esta posición.
     * @return Código hash basado en las coordenadas X e Y.
     */
    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    /**
     * Devuelve una representación en cadena de la posición.
     * @return Cadena en el formato "(x, y)".
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
