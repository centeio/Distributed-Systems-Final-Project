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
	private Socket socket;
	private String message;
	public TCPSender(Socket socket, String message){
		super();
		this.socket = socket;
	}
	
	
	@Override
	public void run() {
		try {
				DataOutputStream output = new DataOutputStream(socket.getOutputStream());
				output.writeBytes(message);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
