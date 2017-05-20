
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

/**
 * Unicast channel sends POST requests from client.
 */
public class Unicast {
	
	/** The main server URL. */
	private static URL mainServerURL = null;
	
	/** The backup server URL. */
	private static URL backupServerURL = null;

	/**
	 * Sends POST request with message and expects JSON response.
	 *
	 * @param message the message to be sent
	 * @return response as a JSON object
	 * @throws MalformedURLException the malformed URL exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ProtocolException the protocol exception
	 * @throws JSONException the JSON exception
	 */
	public static JSONObject sendPOST(String message) throws MalformedURLException, IOException, ProtocolException, JSONException {
		if(mainServerURL == null){
			mainServerURL = new URL ("http://127.0.0.1:8000/SDIS");
		}
		if(backupServerURL == null){
			backupServerURL = new URL ("http://127.0.0.1:8080/SDIS");
		}
		
		HttpURLConnection con = getValidConnection(message, mainServerURL);
		
		if(con == null){
			//Try backup
	
			con = getValidConnection(message, backupServerURL);
		}
		
		if(con == null){
			return new JSONObject("{error: Server not reachable}");
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

	/**
	 * Validates that the message is sent to url specified as returns the connection otherwise returns null.
	 *
	 * @param message the message to be sent
	 * @param url the url where to send
	 * @return a valid connection or null
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ProtocolException the protocol exception
	 * @throws JSONException the JSON exception
	 */
	private static HttpURLConnection getValidConnection(String message, URL url)
			throws IOException, ProtocolException, JSONException {
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		con.setRequestMethod("POST");
		con.setConnectTimeout(5000); //set timeout to 5 seconds
		
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
	
	/**
	 * Sets the main server URL.
	 *
	 * @param url the new main server URL
	 * @throws MalformedURLException the malformed URL exception
	 */
	public static void setMainServerURL(String url) throws MalformedURLException {
		Unicast.mainServerURL = new URL(url);
	}

	/**
	 * Sets the backup server URL.
	 *
	 * @param url the new backup server URL
	 * @throws MalformedURLException the malformed URL exception
	 */
	public static void setBackupServerURL(String url) throws MalformedURLException {
		Unicast.backupServerURL = new URL(url);
	}
}
