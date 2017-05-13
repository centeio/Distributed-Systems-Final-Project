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


	public Unicast(Object peer) {


	}
	
/*	private static void sendGET(String plate) throws MalformedURLException, IOException, ProtocolException, JSONException {
		URL url = new URL ("http://127.0.0.1:8000/SDIS?plate=" + plate);

		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");
		//Send JSON
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", "charset=UTF-8");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//print result
		JSONObject info = new JSONObject(response.toString());
		try {
			info = info.getJSONObject("register");
			System.out.println(info.getString("plate") + " belongs to " + info.getString("owner"));				
				
		} catch (JSONException e) {
			try {
				info = info.getJSONObject("error");
				System.out.println("ERROR: " + info.getString("msg"));
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
*/
	public static void sendPOST(String message) throws MalformedURLException, IOException, ProtocolException, JSONException {
		URL url = new URL ("http://127.0.0.1:8000/SDIS");

		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("POST");
		//Send JSON
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", "charset=UTF-8");
	
		
		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());

		wr.write(message);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println("Got here");
		
		JSONObject info = new JSONObject(response.toString());
		try {
			info = info.getJSONObject("success");
			System.out.println(info.getString("plate") + " now belongs to " + info.getString("owner"));				
				
		} catch (JSONException e) {
			try {
				info = info.getJSONObject("error");
				System.out.println("ERROR: " + info.getString("msg"));
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
/*	private static void sendPUT(String plate, String owner) throws MalformedURLException, IOException, ProtocolException, JSONException {
		URL url = new URL ("http://127.0.0.1:8000/SDIS");

		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("PUT");
		//Send JSON
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", "charset=UTF-8");
		
		//Putting JSON on it {"register":{"plate":"value","owner":"owner name"}}
		JSONObject info   = new JSONObject();
		JSONObject param   = new JSONObject();
		info.put("plate", plate);
		info.put("owner", owner);
		param.put("register", info);
		
		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());

		wr.write(param.toString());
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'PUT' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		info = new JSONObject(response.toString());
		info = info.getJSONObject("success");
		System.out.println("Created register: " + info.getString("plate") + " belongs to " + info.getString("owner"));
	}
	
	private static void sendDELETE(String plate) throws MalformedURLException, IOException, ProtocolException, JSONException {
		URL url = new URL ("http://127.0.0.1:8000/SDIS?plate=" + plate);
		
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		// optional default is GET
		con.setRequestMethod("DELETE");
		//Send JSON
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", "charset=UTF-8");
		
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'DELETE' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//print result
		JSONObject info = new JSONObject(response.toString());
		try {
			info = info.getJSONObject("register");
			System.out.println("Deleted " + info.getString("plate") + " which belonged to " + info.getString("owner"));				
				
		} catch (JSONException e) {
			try {
				info = info.getJSONObject("error");
				System.out.println("ERROR: " + info.getString("msg"));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}*/

}
