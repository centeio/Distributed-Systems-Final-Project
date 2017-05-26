import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 * The TCP Server.
 */
public class TCPServer implements Runnable {
	int port;

	/**
	 * Instantiates a new TCP server.
	 */
	public TCPServer(int port) {
		super();
		this.port = port;
	}

	/* Creates a TCP Server and registers new clients for notification
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			System.setProperty("javax.net.ssl.keyStore", "../files/server.keys");
			System.setProperty("javax.net.ssl.keyStorePassword", "123456");
			System.setProperty("javax.net.ssl.trustStore", "../files/truststore");
			System.setProperty("javax.net.ssl.trustStorePassword", "123456");
			
			//Create server socket
			SSLServerSocket s = null;  
			SSLServerSocketFactory ssf = null;  
			 
			ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			
			try {  
			    s = (SSLServerSocket) ssf.createServerSocket(port);  
			}  
			catch( IOException e) {  
			    System.out.println("Server - Failed to create SSLServerSocket");  
			    e.getMessage();
			    e.printStackTrace();
			    return;  
			} 
			
			// Require client authentication  
			s.setNeedClientAuth(true);
			
			SSLSocket socket;

			System.out.println("Server IP: " + InetAddress.getLocalHost());
			while(true){	 
				socket = (SSLSocket) s.accept();
				System.out.println("Accepted new Client.");

				BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));	
				
				char buffer[] = new char[8000];
				input.read(buffer);
				System.out.println(new String(buffer).trim());
				
				Server.addClient(socket);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
