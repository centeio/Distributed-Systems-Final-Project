import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Locator extends Thread {
	private Client client;

	public Locator(Client client) {
		this.client = client;	
	}

	public void run() {
		boolean closed = false;
		Scanner in = new Scanner(System.in);
		
		do{
			System.out.println("Para sair escreva \"sair\" \n Para dar like escreva \"like\" \n Localizacao: ");

			String input = in.nextLine();
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
		
		in.close();
	}


}
