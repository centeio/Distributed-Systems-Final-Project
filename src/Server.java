import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {
	
	
	class MyHandler implements HttpHandler 
	{
	    public void handle(HttpExchange t) throws IOException 
	    {
	    	String response = "";
	    	
	    	String method = t.getRequestMethod();
	    	
	    	switch(method){
	    	case "GET":
	    	/*	try {
					response = respondGET(t);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
	    		break;
	    	case "POST":
		        try {
					response = respondPOST(t);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		break;
	    	case "PUT":
		      /*  try {
					response = respondPUT(t);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
	    		break;
	    	case "DELETE":
		      /*  try {
					response = respondDELETE(t);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
	    		break;
	    	default:
	    		response =  "UNFAMILIAR MESSAGE";
	    		System.out.println("Error");
	    	}
    	    
	        t.sendResponseHeaders(200, response.length());
	        OutputStream os = t.getResponseBody();
	        os.write(response.getBytes());
	        os.close();
	    }

	/*	private String respondGET(HttpExchange t) throws JSONException {
			String response;
			//Map query
			HashMap<String, String> query = mapQuery(t.getRequestURI().getQuery());
			
			//Show message received
			System.out.println("GET plate=" + query.get("plate"));
			
			Vehicle v = vehicles.get(query.get("plate"));
			if(v == null){
				JSONObject msg   = new JSONObject();
				JSONObject param   = new JSONObject();

				param.put("oper", "GET");
				param.put("plate", query.get("plate"));
				param.put("msg", "Register not found");
				msg.put("error", param);
				
				response =  msg.toString();
			}
			else{
				JSONObject msg   = new JSONObject();
				JSONObject param   = new JSONObject();

				param.put("plate", v.getPlate());
				param.put("owner", v.getOwner());
				msg.put("register", param);
				
				response =  msg.toString();
			}
			return response;
		}*/
		
		private String respondPOST(HttpExchange t) throws IOException, JSONException {
			
			String response = "";
			
			JSONObject info = parseIntoJSON(t);
			
			//Show message received
			System.out.println("POST plate=" + info.getString("plate") + " owner=" + info.getString("owner"));
			
			/*Vehicle v = vehicles.get(info.getString("plate"));
			if(v == null){
				JSONObject msg   = new JSONObject();
				JSONObject param   = new JSONObject();

				param.put("oper", "POST");
				param.put("plate", info.get("plate"));
				param.put("msg", "Register not found");
				msg.put("error", param);
				
				response =  msg.toString();
			}
			else{
				v.setOwner(info.getString("owner"));
				JSONObject msg   = new JSONObject();
				JSONObject param   = new JSONObject();

				param.put("plate", v.getPlate());
				param.put("owner", v.getOwner());
				msg.put("success", param);
				
				response =  msg.toString();
			}*/
			return response;
		}
		
	/*	private String respondPUT(HttpExchange t) throws IOException, JSONException {

			JSONObject info = parseIntoJSON(t);
			
			//Show message received
			System.out.println("PUT plate=" + info.getString("plate") + " owner=" + info.getString("owner"));
			
			Vehicle v = new Vehicle(info.getString("owner"), info.getString("plate"));
			vehicles.put(v.getPlate(), v);

			JSONObject msg   = new JSONObject();
			JSONObject param   = new JSONObject();

			param.put("plate", v.getPlate());
			param.put("owner", v.getOwner());
			msg.put("success", param);
				
			return msg.toString();
		}

		private String respondDELETE(HttpExchange t) throws JSONException {
			String response;
			//Map query
			HashMap<String, String> query = mapQuery(t.getRequestURI().getQuery());
			
			//Show message received
			System.out.println("POST plate=" + query.get("plate"));
			
			Vehicle v = vehicles.get(query.get("plate"));
			if(v == null){
				JSONObject msg   = new JSONObject();
				JSONObject param   = new JSONObject();

				param.put("oper", "GET");
				param.put("plate", query.get("plate"));
				param.put("msg", "Register not found");
				msg.put("error", param);
				
				response =  msg.toString();
			}
			else{
				vehicles.remove(v.getPlate());
				
				JSONObject msg   = new JSONObject();
				JSONObject param   = new JSONObject();

				param.put("plate", v.getPlate());
				param.put("owner", v.getOwner());
				msg.put("register", param);
				
				response =  msg.toString();
			}
			return response;
		}
		*/
		private JSONObject parseIntoJSON(HttpExchange t) throws IOException, JSONException {
			
			byte[] rbuf = new byte[(int) Math.pow(2,16)];
			InputStream is = t.getRequestBody();
			is.read(rbuf);
			
			String result = new String(rbuf, StandardCharsets.UTF_8).trim();
			
			//Parse JSON {"register":{"plate":"value","owner":"owner name"}}
			JSONObject info   = new JSONObject(result);
			return info.getJSONObject("register");
		}

		private HashMap<String, String> mapQuery(String query) {
			HashMap<String, String> result = new HashMap<String, String>();
    	    for (String param : query.split("&")) {
    	        String pair[] = param.split("=");
    	        if (pair.length>1) {
    	            result.put(pair[0], pair[1]);
    	        }else{
    	            result.put(pair[0], "");
    	        }
    	    }
    	    
    	    return result;
		}
	}

	public static void main(String[] args) {
		Server s = new Server();
		
		try {
			InetSocketAddress inet = new InetSocketAddress("127.0.0.1", 8000);
			HttpServer server = HttpServer.create(inet, 0);
			server.createContext("/projeto", s.new MyHandler());
		    server.setExecutor(null); // creates a default executor
		    server.start();
		    
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
	}

}
