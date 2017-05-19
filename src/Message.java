import org.json.JSONException;

public interface Message {

	String getString(String location) throws JSONException;
	String getString(String action, String location, String username) throws JSONException;
}
