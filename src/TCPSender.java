import java.io.IOException;
import java.io.PrintWriter;

import javax.net.ssl.SSLSocket;

public class TCPSender implements Runnable{
	private SSLSocket socket;
	private String message;
	public TCPSender(SSLSocket socket, String message){
		super();
		this.socket = socket;
	}


	@Override
	public void run() {
		try {
			PrintWriter printer = new PrintWriter(socket.getOutputStream(),true);
			printer.println(message);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
