import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
public class Server {
	class MyHandler implements HttpHandler 	{	    public void handle(HttpExchange t) throws IOException 
	    {
	    	String response = "This is response";    	    
	        t.sendResponseHeaders(200, response.length());
	        OutputStream os = t.getResponseBody();
	        os.write(response.getBytes());
	        os.close();	    }
	}
	public static void main(String[] args) {		Server s = new Server();		//HTTP Server
		final String IP = "127.0.0.1";		final int PORT = 8000;
		try {
			InetSocketAddress inet = new InetSocketAddress(IP, PORT);
			HttpServer server = HttpServer.create(inet, 0);			server.createContext("/SDIS", s.new MyHandler());
			server.setExecutor(Executors.newCachedThreadPool());
		    server.start();    
		} catch (IOException e) {	
			e.printStackTrace();
		}
	}
}
