import org.json.JSONException;
import org.json.JSONObject;

// Notificações Server -> Client
public class MessageSC implements Message {
	String message;

	public MessageSC(String message) {
		super();
		this.message = message;
	}


	@Override
	public String getString() throws JSONException {
		//{"type":"notification","message":"value"}
		JSONObject info   = new JSONObject();
		info.put("type", "notification");
		info.put("message", this.message);
		
		return info.toString();
	}


	@Override
	public String getString(String location, String action, String clientid, String fileid) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

}
