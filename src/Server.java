import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
public class Server {
	class MyHandler implements HttpHandler 	{	    public void handle(HttpExchange t) throws IOException 
	    {
	    	String response;
	    	
	    	String method = t.getRequestMethod();
	    	System.out.println(method);
	    	
	    	//Parse JSON {"type":"request","location":"value","action":"value", "file":id, "clientid":id}
	    	try {
	    		JSONObject info = parseIntoJSON(t);
	    		
	    		switch((String) info.get("type")){
	    		case "request":
	    			
	    			if(info.get("action") != null){
	    				//TODO update file
	    			}
	    			
	    			//TODO get file chunks for location
	    			Chunk fileChunk = new Chunk(); 
	    			
	    			//{"type":"getInfo", "chunk":value}
	    			JSONObject json = new JSONObject();
	    			json.put("type", "getInfo");
	    			json.put("chunck", fileChunk);
	    			
	    			response = json.toString();
	    			break;
    			default:
    				response = "Message not understood";  
		    	}
			} catch (JSONException e) {
				//e.printStackTrace();
				System.err.println("Expected JSON and it was not found");
				response = "JSON expected";  
			}
	        t.sendResponseHeaders(200, response.length());
	        OutputStream os = t.getResponseBody();
	        os.write(response.getBytes());
	        os.close();	    }
	    
	    private JSONObject parseIntoJSON(HttpExchange t) throws IOException, JSONException {
			
			byte[] rbuf = new byte[(int) Math.pow(2,16)];
			InputStream is = t.getRequestBody();
			is.read(rbuf);
			
			String result = new String(rbuf, StandardCharsets.UTF_8).trim();
			
			JSONObject info   = new JSONObject(result);
			return info.getJSONObject("register");
		}
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
