import org.json.JSONException;
import org.json.JSONObject;

// Notificaes Server -> Client
public class MessageSC implements Message {
	String message;

	public MessageSC(String message) {
		super();
		this.message = message;
	}

	@Override
	public String getString(String location) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getString(String action, String fileid) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

}
