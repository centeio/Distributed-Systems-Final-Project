import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class TCPSender implements Runnable{
	
	@Override
	public void run() {
		ServerSocket welcomeSocket;
		try {
			welcomeSocket = new ServerSocket(6789);

			Socket socket = welcomeSocket.accept();
			//from Client
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			

			while (true) {
				DataOutputStream output = new DataOutputStream(socket.getOutputStream());
				output.writeBytes("oi");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
