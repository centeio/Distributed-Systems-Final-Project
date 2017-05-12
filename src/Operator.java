
public class Operator implements Runnable{
	private Client c;

	public Operator(Client c) {
		super();
		this.setC(c);
	}

	@Override
	public void run() {
		/*while(true){
			try {
				Object protocol = peer.queue.take();
				
				//work 
				if(protocol instanceof Delete){*/		
	}

	public Client getC() {
		return c;
	}

	public void setC(Client c) {
		this.c = c;
	}
	
	

}
