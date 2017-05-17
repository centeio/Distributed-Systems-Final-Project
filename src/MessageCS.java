import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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
						message = getString(actionname,fileid);
						response = Unicast.sendPOST(message);

					}else{
						if(!client.locationupdated()){
							message = getString(client.getlocation());
							response = Unicast.sendPOST(message);
							//TODO INES response gives file information
							fileid = response.getString("fileid");
							int noChunk = response.getInt("noChunk");
							client.queue.add(new Download(/*fileid,filelength, etc*/));
							client.setLocationupdated(true);
						}
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
	public String getString(String action, String fileid) throws JSONException{
		//{"type":"request","location":"value","action":"value"}
		JSONObject info = new JSONObject();

		info.put("type", action);
		info.put("fileid", fileid);

		return info.toString();

	}


}
