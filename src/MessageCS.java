import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;
// TODO: Auto-generated Javadoc
//para localizao Client -> Server com Ao (LIKE ou null) 
/**
 * Message from Client to Server.
 */
public class MessageCS{
	
	/** The scheduler. */
	private final ScheduledExecutorService scheduler =	Executors.newScheduledThreadPool(1);	
	/** The client. */
	private Client client = null;

	/**
	 * Instantiates a new message from Client to Server.
	 *
	 * @param client the client
	 */
	public MessageCS(Client client) {
		super();
		this.client = client;		
		sender();	}
	/**
	 * Checks every second if client changed location or liked something
	 * In that case, sends a HTTP POST message to server
	 */
	public void sender() {		final Runnable sender = new Runnable() {
			public void run() { 
				ArrayList<String> action;
				JSONObject response;
				String actionname = null, fileid = null, message = null;
				try {
					if((action = client.getNextAction()) != null){
						actionname = action.get(0);
						fileid = action.get(1);
						message = getString(actionname, client.getlocation(), client.getUsername());
						response = Unicast.sendPOST(message, client.getServerip());

					}else{
						if(!client.locationupdated()){
							message = getString(client.getlocation(), client.getUsername());
							response = Unicast.sendPOST(message, client.getServerip());
							//TODO INES response gives file information
							fileid = response.getString("filename");
							int noChunks = response.getInt("noChunk");
							client.executor.execute(new Download(client, fileid, noChunks));
							client.setLocationupdated(true);
						}
					}
					if(client.getLocator().isClosed()){
						scheduler.shutdown();
					}

				} catch (JSONException | IOException e) {
					e.printStackTrace();
				}
			}
		};		/*runs sender every second*/
		scheduler.scheduleAtFixedRate(sender, 0, 1, TimeUnit.SECONDS);
	}
	/**
	 * Gets the locate message with format {"type":"locate", "location":location,"username":username}.
	 *
	 * @param location client's location
	 * @param username client's username
	 * @return JSON message in string format
	 * @throws JSONException the JSON exception
	 */
	public String getString(String location, String username) throws JSONException{		JSONObject info   = new JSONObject();
		info.put("type", "locate");
		info.put("location", location);
		info.put("username", username);

		return info.toString();
	}

	/**
	 * Gets the action message with format {"type":action, "location":location,"username":username}
	 *
	 * @param action action to be preformed (like)
	 * @param location client's location
	 * @param username client's username
	 * @return the string
	 * @throws JSONException the JSON exception
	 */
	public String getString(String action, String location, String username) throws JSONException{
		JSONObject info = new JSONObject();

		info.put("type", action);
		info.put("location", location);
		info.put("username", username);

		return info.toString();
	}
}
