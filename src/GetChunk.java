import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class GetChunk extends Protocol{
	int chunkNo;
	String filename;
	int noChunks;
	
	public GetChunk(Client client, int chunkNo, String filename, int noChunks) {
		super(client);
		this.chunkNo = chunkNo;
		this.filename = filename;
		this.noChunks = noChunks;
	}

	public int getChunkNo() {
		return chunkNo;
	}

	public String getFilename() {
		return filename;
	}

	public int getNoChunks() {
		return noChunks;
	}

	@Override
	public void run() {
		//cria pedido para servidor com chunk no x
		JSONObject info = new JSONObject();

		try {
			info.put("type", "getChunk");
			info.put("filename", filename);
			info.put("chunkNo", chunkNo);
		} catch (JSONException e) {
			System.err.println(e.getMessage());
		}
		

		JSONObject response;
		try {
			response = Unicast.sendPOST(info.toString());
		} catch (IOException | JSONException e) {
			System.err.println(e.getMessage());
			return;
		}
		
		// quando recebe, SE NAO EXISTIR coloca em array de ficheiro filename ConcurrentHashMap<String filename,Map<int no, Chunks>>
		HashMap<Integer, Chunk> chunks = client.files.get(filename);
		
		if(chunks != null){
			if(chunks.get(chunkNo) == null){						
				try {
					if(response.has("error")){
						System.err.println(response.getString("error"));
						return;
					}
					else{
						byte data[] = response.getString("data").trim().getBytes("ISO-8859-1");
						Chunk c = new Chunk(chunkNo, filename, data);
						
						chunks.put(chunkNo, c);
					}
				} catch (Exception e) {
					System.err.println(e.getMessage());
					return;
				}
			}
		
			// se length do Map do filename == a nochunks cria new Restore(filename)
			if(chunks.size() == noChunks){
				client.executor.execute(new Restore(client, filename));
			}
		}
		else{
			System.err.println("Error file does not exist");
		}		
	}	
}
