import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NewLike implements Runnable{
	private Client client;

	public NewLike(Client c){
		this.client = c;

	}

	@Override
	public void run() {
		while(true){
			BufferedReader inFromServer;
			try {
				inFromServer = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
				String sentence = inFromServer.readLine();
				System.out.println(sentence);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
	}

}
