import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ServerHandler implements HttpHandler 
{
	public void handle(HttpExchange t) throws IOException 
	{
		//TODO 
		String response;

		String method = t.getRequestMethod();
		System.out.println(method);

		//Parse JSON {"type":"request","location":"value","action":"value", "file":id, "clientid":id}
		try {
			JSONObject info = parseIntoJSON(t);
			JSONObject json = new JSONObject();

			switch((String) info.get("type")){
			case "like":
				/*guardar informacao de like e iniciar notificacao para restantes clientes NUNO
				 * alterar ficheiro JSON do servidor NUNO*/ 
				System.out.println(info.get("type"));
				json = new JSONObject();
				json.put("type", "registered");
				json.put("file", info.get("fileid"));

				response = json.toString();

				break;
			case "locate":
				System.out.println(info.get("type"));

				//{"type":"getInfo", "fileid":value}
				json = new JSONObject();
				json.put("type", "getInfo");
				//TODO fileid in response INES
				response = json.toString();
				break;
			default:
				response = "{error : Message not understood}";  
			}
		} catch (JSONException e) {
			e.printStackTrace();
			System.err.println("Expected JSON and it was not found");
			response = "{error : JSON expected}";  
		}
		
		System.out.println(response);

		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}

	private JSONObject parseIntoJSON(HttpExchange t) throws IOException, JSONException {

		byte[] rbuf = new byte[(int) Math.pow(2,16)];
		InputStream is = t.getRequestBody();
		is.read(rbuf);

		String result = new String(rbuf, StandardCharsets.UTF_8).trim();
		
		System.out.println(result);

		return new JSONObject(result);
	}
}
