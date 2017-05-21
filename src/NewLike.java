import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Receives notifications from server regarding new likes
 */
public class NewLike implements Runnable{
	
	/** The client. */
	private Client client;

	/**
	 * Instantiates a new NewLike.
	 *
	 * @param c the client
	 */
	public NewLike(Client c){
		this.client = c;

	}

	/* 
	 * Receives notifications from server and prints them in the console
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));

			while(true){
				String sentence = inFromServer.readLine();
				System.out.println(sentence);
			}
		} catch (IOException e1) {
			System.out.println("Conection refused");
			//e1.printStackTrace();
			return;
		}
	}

}
