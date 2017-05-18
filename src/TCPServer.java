import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer implements Runnable {

	private Server server;
	public TCPServer(Server server) {
		super();
		this.server = server;
	}

	@Override
	public void run() {
		ServerSocket welcomeSocket;
		try {
			welcomeSocket = new ServerSocket(6789);

		Socket socket = welcomeSocket.accept();
			//from Client
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));	
		
		server.addClient(socket);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
