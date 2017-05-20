import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLSocket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.sun.net.httpserver.HttpServer;

/**
 * The Server.
 */
public class Server {
	/** Maps the clients by their username to the names of the files they like */
	static Map<String, ArrayList<String>> client_mapping = new ConcurrentHashMap<String, ArrayList<String>>();
	
	/** Maps the location to the corresponding filename and number of chunks*/
	static Map<String, SimpleEntry<String, Integer>> file_mapping = new ConcurrentHashMap<String, SimpleEntry<String, Integer>> ();
	
	/** Maps the filename to the corresponding file's chunks */
	static Map<String, ArrayList<Chunk>> chunks_mapping = new ConcurrentHashMap<String, ArrayList<Chunk>>();
	
	/** Keeps the clients' sockets for notifications */
	static ArrayList<SSLSocket> clients;
	
	/**
	 * The main method.
	 * Receives the ip and port for the HTTP connection.
	 * Gives information to clients about its location and notifies if some user likes it's location
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		if(args.length < 2){
			System.out.println("Usage Server <ip address> <port>");
			return;
		}
		clients = new ArrayList<SSLSocket>();
		TCPServer tcpserver = new TCPServer();
		new Thread(tcpserver).start();
		
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
	
	/**
	 * Sets up the mapping of clients and locations
	 */
	public static void setup(){
		loadFiles();
		loadClients();
	}

	/**
	 * Loads information regarding the tastes of the clients
	 */
	private static void loadClients() {
		File json = new File("./database/server_clients.json");

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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads information mapping the location to it's file
	 */
	private static void loadFiles() {
		File json = new File("./database/server_files.json");

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
				
				SimpleEntry<String,Integer> entry = new SimpleEntry<String,Integer>(filename, numChunks);			
				
				file_mapping.put(fileLocation, entry);
			}	
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a like to non-volatile memory.
	 *
	 * @param username name of the user who liked 
	 * @param filename name of file liked 
	 */
	public static void addLike(String username, String filename){
		client_mapping.get(username).add(filename);
		
		File json = new File("./database/server_clients.json");
		
		try{
			byte[] data = Files.readAllBytes(json.toPath());
			String file_string = new String(data);
			
			JSONObject json_obj = new JSONObject(file_string);
			
			JSONObject server_clients = json_obj.getJSONObject("server_clients");
			JSONArray clients = server_clients.getJSONArray("clients");
			
			for(int i = 0; i < clients.length(); i++){
				JSONObject info = clients.getJSONObject(i);
				if(info.get("username").equals(username)){
					JSONArray arr = info.getJSONArray("likes");
					boolean contains = false;
					
					for(int j = 0; j < arr.length(); j++){
						if(arr.get(j).equals(filename)){
							contains = true;
						}
					}
					
					if(!contains){
						arr.put(filename);
					}
				}
			}
			
			FileWriter json_file = new FileWriter(json);
			json_obj.write(json_file);
			json_file.close();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds new client socket for notification uses.
	 *
	 * @param socket the client socket
	 */
	public static void addClient(SSLSocket socket) {
		clients.add(socket);
	}
}
