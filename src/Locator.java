
public class Locator extends Thread {
	private Client client;

	public Locator(Client client) {
		this.client = client;	
	}

	public void run() {
		boolean closed = false;
		//TODO verificar se novo like
		do{
			System.out.println("Para sair escreva \"sair\" \n Localizacao: ");
			String input = System.console().readLine();
			if(input.toLowerCase().equals("sair")){
				closed = true;

			}else{
				client.setlocation(input.toLowerCase());
			}
		}while(!closed);
	}


}
