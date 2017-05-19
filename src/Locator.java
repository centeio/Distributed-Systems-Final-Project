import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Locator extends Thread {
	private Client client;

	public Locator(Client client) {
		this.client = client;	
	}

	public void run() {
		boolean closed = false;
		do{
			System.out.println("Para sair escreva \"sair\" \n Localizacao: ");
			String input = System.console().readLine();
			if(input.toLowerCase().equals("sair")){
				closed = true;
				try {
					client.getSocket().close();
				} catch (IOException e) {
					System.out.println("Could not close socket from " + client.getUsername());
					e.printStackTrace();
				}

			}else if(input.toLowerCase().equals("like")){
				ArrayList<String> action = new ArrayList<String>();
				action.addAll(Arrays.asList("like","filename"));
				client.addAction(action);				
			}else{
				client.setlocation(input.toLowerCase());
			}
		}while(!closed);
	}


}
