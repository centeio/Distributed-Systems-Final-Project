import org.json.JSONException;

public interface Message {
	abstract String getString()  throws JSONException ;


	String getString(String location, String action, String clientid, String fileid) throws JSONException;
}
