import java.io.IOException;
import java.io.PrintWriter;

import javax.net.ssl.SSLSocket;

/**
 * TCP Sender.
 */
public class TCPSender implements Runnable{
	
	/** The client's socket. */
	private SSLSocket socket;
	
	/** The message. */
	private String message;
	
	/**
	 * Instantiates a new TCP sender.
	 *
	 * @param socket the client's socket
	 * @param message the message
	 */
	public TCPSender(SSLSocket socket, String message){
		super();
		this.socket = socket;
		this.message = message;
	}


	/* 
	 * Sends notification to the client
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			PrintWriter printer = new PrintWriter(socket.getOutputStream(),true);
			printer.println(message);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
