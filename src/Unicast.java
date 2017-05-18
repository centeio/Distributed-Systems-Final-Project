import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class Unicast {
	
	private Client client;

	public Unicast(Client client) {
		this.client = client;

	}

	public static JSONObject sendPOST(String message) throws MalformedURLException, IOException, ProtocolException, JSONException {
		
		URL url = new URL ("http://127.0.0.1:8000/SDIS");
		
		HttpURLConnection con = getValidConnection(message, url);
		
		if(con == null){
			//Try backup
			url = new URL ("http://127.0.0.1:8080/SDIS");
			
			con = getValidConnection(message, url);
		}
		
		if(con == null){
			return new JSONObject("{error: Server not reachable");
		}

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		JSONObject info = new JSONObject(response.toString());
		
		return info;
	}

	private static HttpURLConnection getValidConnection(String message, URL url)
			throws IOException, ProtocolException, JSONException {
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		con.setRequestMethod("POST");
		//Send JSON
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", "charset=ISO-8859-1");
	
		try{
		
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
	
			wr.write(message);
			wr.flush();
			wr.close();
	
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
		} catch (java.net.SocketTimeoutException e) {
			System.out.println("Timeout!!!!!");
			return null;
		} catch (java.io.IOException e) {
			System.out.println("Server Down!!!!!");
			return null;
		}
		return con;
	}
}
