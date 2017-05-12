import org.json.JSONException;
import org.json.JSONObject;

//para localiza��o Client -> Server com A��o (LIKE ou null) 
public class MessageCS implements Message{
	String location;
	String action;

	public MessageCS(String location, String action) {
		super();
		
		this.location = location;
		this.action = action;
	}

	@Override
	public String getString() throws JSONException{
		//{"type":"request","location":"value","action":"value"}
		JSONObject info   = new JSONObject();
		info.put("type", "request");
		info.put("location", this.location);
		info.put("action", this.action);
		
		return info.toString();
	}
	
}
