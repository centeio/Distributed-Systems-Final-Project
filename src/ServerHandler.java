import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MulticastSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;


import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ServerHandler implements HttpHandler 
{
	public void handle(HttpExchange t) throws IOException 
	{
		//TODO 
		String response;

		String method = t.getRequestMethod();
		System.out.println(method);

		//Parse JSON {"type":"request", ...}
		try {
			JSONObject info = parseIntoJSON(t);
			JSONObject json = new JSONObject();
			String filename, username;
			
			System.out.println(info.get("type"));

			switch((String) info.get("type")){
			case "like":
				/*guardar informacao de like e iniciar notificacao para restantes clientes NUNO
				 * alterar ficheiro JSON do servidor NUNO*/ 
				username = info.getString("username");
				filename = info.getString("filename");
				
				json = new JSONObject();
				
				if(Server.client_mapping.get(username).contains(filename)){
					//already has a like
					json.put("type", "like");
					json.put("value", false);
				}else{
					//new like
					json.put("type", "like");
					json.put("value", true);
					
					Server.addLike(username, filename);
				}

				response = json.toString();
				
				String message = username + " likes spot " + filename;
				for(Socket s : Server.clients){
					TCPSender sender = new TCPSender(s,message);
					new Thread(sender).start();
				}
				break;
			case "locate":
				filename = ((SimpleEntry<String, Integer>) Server.file_mapping.get(info.get("location"))).getKey();
				if(Server.chunks_mapping.get(filename) == null){
					ArrayList<Chunk> chunks = divideFileIntoChunks(filename);
					Server.chunks_mapping.put(filename, chunks);
				}
				int noChunks = Server.chunks_mapping.get(filename).size();
				
				//format response JSON {"type":"getInfo", "filename":value, "noChunk":value}
				json = new JSONObject();
				json.put("type", "getInfo");
				json.put("filename", filename);
				json.put("noChunk", noChunks);
				
				response = json.toString();
				break;
			case "getChunk":
				filename = info.getString("filename");
				int chunkNo = info.getInt("chunkNo");
				
				//TODO INES get actual chunk data
				byte data[] = Server.chunks_mapping.get(filename).get(chunkNo).getData();
				System.out.println(data);
				
				//format response JSON {"type":"getInfo", "filename":value, "chunkNo":value, "data":value}
				json = new JSONObject();
				json.put("type", "returnChunk");
				json.put("filename", filename);
				json.put("chunkNo", chunkNo);
				json.put("data", data.toString());
				
				response = json.toString();
				break;
			default:
				response = "{error : Message not understood}";  
			}
		}catch (JSONException e) {
			e.printStackTrace();
			//System.err.println("Expected JSON and it was not found");
			response = "{error : JSON expected}";  
		}
		
		System.out.println(response);

		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}

	private JSONObject parseIntoJSON(HttpExchange t) throws IOException, JSONException {

		byte[] rbuf = new byte[(int) Math.pow(2,16)];
		InputStream is = t.getRequestBody();
		is.read(rbuf);

		String result = new String(rbuf, StandardCharsets.ISO_8859_1).trim();
		
		System.out.println(result);

		return new JSONObject(result);
	}
	
	/**
	 * Divides a file into an array of chunks with 64KB of size.
	 * Algorithm based on the following StackOverflow question/answer.
	 * http://stackoverflow.com/questions/4431945/split-and-join-back-a-binary-file-in-java
	 * @param name Name of the file
	 */
	public ArrayList<Chunk> divideFileIntoChunks(String name){

		ArrayList<Chunk> chunks = new ArrayList<Chunk>();
		
		try{
			File file = new File("../database/files/" + name);

			FileInputStream stream = new FileInputStream(file);
			MulticastSocket socket = new MulticastSocket();

			byte[] chunkData;
			long filelength = file.length();
			int chunkMaxSize = 1000 * 64;
			int readLength = chunkMaxSize;
			int counter = 1;

			while(filelength > 0){
				if(filelength < chunkMaxSize){
					readLength = (int)filelength;
				}

				chunkData = new byte[readLength];

				int bytesRead = stream.read(chunkData, 0, readLength);
				filelength -= bytesRead;

				if(chunkData.length != bytesRead){
					System.out.println("Error reading chunk");
					break;
				}

				Chunk c = new Chunk(counter, name, chunkData);

				counter++;
				chunks.add(c);
			}

			stream.close();
			socket.close();
		}catch(FileNotFoundException e){
			System.out.println("File " + name + " not found");
			return null;
		}catch(SecurityException e){
			System.out.println("Denied reading file " + name);
			return null;
		} catch (IOException e) {
			System.out.println("Error closing stream of file " + name);
			return null;
		}
		
		return chunks;
	}
}
