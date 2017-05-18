import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.sun.net.httpserver.HttpServer;

public class Server {
	//username = array_likes
	static Map<String, ArrayList<String>> client_mapping = new ConcurrentHashMap<String, ArrayList<String>>();
	
	//location = <filename, numChunks>
	static Map<String, Map<String, Integer>> file_mapping = new ConcurrentHashMap<String, Map<String, Integer>> ();

	public static void main(String[] args) {
		setup();
				Server s = new Server();		//HTTP Server
		final String IP = "127.0.0.1";		final int PORT = 8000;
		try {
			InetSocketAddress inet = new InetSocketAddress(IP, PORT);
			HttpServer server = HttpServer.create(inet, 0);			server.createContext("/SDIS", new ServerHandler());
			server.setExecutor(Executors.newCachedThreadPool());
		    server.start();
		} catch (IOException e) {	
			e.printStackTrace();
		}
	}
	
	public static void setup(){
		loadFiles();
		loadClients();
	}

	private static void loadClients() {
		File json = new File("../database/server_clients.json");

		try {
			byte[] data = Files.readAllBytes(json.toPath());
			String file_string = new String(data);
			
			JSONObject json_obj = new JSONObject(file_string);
			
			//Start to parse the file
			JSONObject server_clients = json_obj.getJSONObject("server_clients");
			JSONArray clients = server_clients.getJSONArray("clients");
			
			for(int i = 0; i < clients.length(); i++){
				JSONObject child = (JSONObject) clients.get(i);
				
				String username = child.getString("username");
				JSONArray arr = child.getJSONArray("likes");
				
				client_mapping.put(username, new ArrayList<String>());
				
				for(int j = 0; j < arr.length(); j++){
					String filename = arr.get(j).toString();
					client_mapping.get(username).add(filename);
				}	
				
			}				
			System.out.println(client_mapping.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private static void loadFiles() {
		File json = new File("../database/server_files.json");

		try {
			byte[] data = Files.readAllBytes(json.toPath());
			String file_string = new String(data);
			
			JSONObject json_obj = new JSONObject(file_string);
			
			//Start to parse the file
			JSONObject server_files = json_obj.getJSONObject("server_files");
			JSONArray files = server_files.getJSONArray("files");
			
			for(int i = 0; i < files.length(); i++){
				JSONObject child = (JSONObject) files.get(i);
				
				String filename = child.getString("name");
				String fileLocation = child.getString("location");
				int numChunks = child.getInt("numChunks");
				
				Map<String,Integer> map = new ConcurrentHashMap<String,Integer>();
				map.put(filename, numChunks);			
				
				file_mapping.put(fileLocation, map);
			}	
			
			System.out.println(file_mapping.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
