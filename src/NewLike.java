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
		try {
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));

			while(true){
				String sentence = inFromServer.readLine();
				System.out.println(sentence + "from " + client.getUsername());
			}
		} catch (IOException e1) {
			System.out.println("Conection refused");
			//e1.printStackTrace();
			return;
		}
	}

}
