
public class Operator implements Runnable{
	private Client c;

	public Operator(Client c) {
		super();
		this.setC(c);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	public Client getC() {
		return c;
	}

	public void setC(Client c) {
		this.c = c;
	}
	
	

}
