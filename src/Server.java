import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;
public class Server {
	//TODO Hashtable with clients NUNO
	//TODO Hashtable mapping locations with files NUNO
	public static void main(String[] args) {		Server s = new Server();		//HTTP Server
		final String IP = "127.0.0.1";		final int PORT = 8000;
		try {
			InetSocketAddress inet = new InetSocketAddress(IP, PORT);
			HttpServer server = HttpServer.create(inet, 0);			server.createContext("/SDIS", new ServerHandler());
			server.setExecutor(Executors.newCachedThreadPool());
		    server.start();    
		} catch (IOException e) {	
			e.printStackTrace();
		}
	}
}
