import java.sql.Array;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;
//para localiza��o Client -> Server com A��o (LIKE ou null) 
public class MessageCS implements Message{
	private final ScheduledExecutorService scheduler =	Executors.newScheduledThreadPool(1);	private Client client = null;

	public MessageCS(Client client) {
		super();
		this.client = client;		
		sender();	}
	public void sender() {		final Runnable sender = new Runnable() {
			public void run() { 
				System.out.println(client.getlocation());
				ArrayList<String> action;
				String actionname = null, fileid = null;
				if((action = client.getNextAction()) != null){
					actionname = action.get(0);
					fileid = action.get(1);
				}
				try {
					String message = getString(client.getlocation(),actionname,client.getId(),fileid);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		scheduler.scheduleAtFixedRate(sender, 0, 1, TimeUnit.SECONDS);
	}
	@Override
	public String getString(String location, String action, String clientid, String fileid) throws JSONException{		//{"type":"request","location":"value","action":"value"}		JSONObject info   = new JSONObject();
		info.put("type", "request");
		info.put("location", location);
		info.put("action", action);	
		info.put("file",fileid);
		info.put("clientid", clientid);	
		return info.toString();
	}

	@Override
	public String getString() throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

}
