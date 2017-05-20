import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;
//para localizao Client -> Server com Ao (LIKE ou null) 
public class MessageCS implements Message{
	private final ScheduledExecutorService scheduler =	Executors.newScheduledThreadPool(1);	private Client client = null;

	public MessageCS(Client client) {
		super();
		this.client = client;		
		sender();	}
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
						response = Unicast.sendPOST(message);

					}else{
						if(!client.locationupdated()){
							message = getString(client.getlocation());
							response = Unicast.sendPOST(message);
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
		System.out.println("before sender");
		scheduler.scheduleAtFixedRate(sender, 0, 1, TimeUnit.SECONDS);
		System.out.println("after sender");

	}
	@Override
	public String getString(String location) throws JSONException{		//{"type":"request","location":"value","action":"value"}		JSONObject info   = new JSONObject();
		info.put("type", "locate");
		info.put("location", location);

		return info.toString();
	}

	@Override
	public String getString(String action, String location, String username) throws JSONException{
		//{"type":action,"location":location,"username":username}
		JSONObject info = new JSONObject();

		info.put("type", action);
		info.put("location", location);
		info.put("username", username);

		return info.toString();

	}


}
