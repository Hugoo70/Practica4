package Pr2;

public class Jugador implements Runnable {
	private String name;
	private int id;
	private String inicial;
	

	public Jugador(String name, int id, String inicial) {
		//super();
		this.name = name;
		this.id = id;
		this.inicial=inicial;
	}
	
	
	

    public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public int getId() {
		return id;
	}




	public void setId(int id) {
		this.id = id;
	}




	public String getInicial() {
		return inicial;
	}




	public void setInicial(String inicial) {
		this.inicial = inicial;
	}




	public void interactuar() {
        System.out.println("Soy el jugador " + name + ".");
    }

	@Override
	public String toString() {
		return "jugador [name=" + name + ", id=" + id + "]";
	}




	@Override
	public void run() {
		// TODO Auto-generated method stub
		Main.Game(Main.posicionesJugadores);
	}
	
	
	
	
}
