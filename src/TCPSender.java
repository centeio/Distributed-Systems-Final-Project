import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPSender implements Runnable{
	private Socket socket;
	private String message;
	public TCPSender(Socket socket, String message){
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
