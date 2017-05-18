import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer implements Runnable {

	public TCPServer() {
		super();

	}

	@Override
	public void run() {
		ServerSocket serversocket;
		try {
			serversocket = new ServerSocket(6458);
			System.out.println("Server tcp socket started " + serversocket);

			while(true){	
				Socket socket = serversocket.accept();

				BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));	

				Server.addClient(socket);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
