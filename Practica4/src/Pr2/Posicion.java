package Pr2;

public class Posicion {
    private final int x;
    private final int y;

   

    public Posicion(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
    
    

	public int getX() {
		return x;
	}



	public int getY() {
		return y;
	}



	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Posicion that = (Posicion) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
	
}
