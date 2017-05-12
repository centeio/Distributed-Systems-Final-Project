
public class Locator extends Thread {
	private Client client;

	public Locator() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Locator(Client client) {
		this.client = client;	
	}

	public void run() {
		boolean closed = false;

		do{
			System.out.println("Para sair escreva \"sair\" \n Localizacao: ");
			String input = System.console().readLine();
			if(input.toLowerCase() == "sair"){
				closed = true;
			}else{
				client.setlocation(input.toLowerCase());
			}
		}while(!closed);
	}


}
