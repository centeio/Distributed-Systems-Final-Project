import java.io.File;
import java.io.FileWriter;
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
	
	//filename = ArrayList<Chunk>
	static Map<String, ArrayList<Chunk>> chunks_mapping = new ConcurrentHashMap<String, ArrayList<Chunk>>();
	
	public static void main(String[] args) {
		if(args.length < 2){
			System.out.println("Usage Server <ip address> <port>");
			return;
		}
		setup();		
		//HTTP Server
		final String IP = args[0];		final int PORT = Integer.parseInt(args[1]);
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
	
	public static void addLike(String username, String filename){
		client_mapping.get(username).add(filename);
		
		File json = new File("../database/server_clients.json");
		
		try{
			byte[] data = Files.readAllBytes(json.toPath());
			String file_string = new String(data);
			
			JSONObject server_clients = new JSONObject(file_string);
			JSONArray clients = server_clients.getJSONArray("clients");
			
			for(int i = 0; i < clients.length(); i++){
				JSONObject info = clients.getJSONObject(i);
				if(info.get("username").equals(username)){
					info.getJSONArray(username).put(filename);
				}
			}
			
			FileWriter json_file = new FileWriter(json);
			json_file.write(server_clients.toString());
			json_file.close();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
