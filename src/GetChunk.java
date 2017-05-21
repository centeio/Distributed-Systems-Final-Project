import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * the protocol GetChunk
 *
 * Requests chunks from server
 */
public class GetChunk extends Protocol{
	
	/** chunk identifier */
	int chunkNo;
	
	/** name of the file */
	String filename;
	
	/** number of files's chunks */
	int noChunks;
	
	/**
	 * Instantiates a new protocol which get a chunk from server.
	 *
	 * @param client the client
	 * @param chunkNo the chunk identifier
	 * @param filename the filename
	 * @param noChunks the number of file's chunks
	 */
	public GetChunk(Client client, int chunkNo, String filename, int noChunks) {
		super(client);
		this.chunkNo = chunkNo;
		this.filename = filename;
		this.noChunks = noChunks;
	}

	/**
	 * Gets the chunk identifier.
	 *
	 * @return the chunk identifier
	 */
	public int getChunkNo() {
		return chunkNo;
	}

	/**
	 * Gets the filename.
	 *
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Gets the number chunks.
	 *
	 * @return the number chunks
	 */
	public int getNoChunks() {
		return noChunks;
	}

	/* Requests server for chunk data and places it on clients's files.
	 * If all chunks have been collected requests the execution of a Restore protocol
	 * @see java.lang.Runnable#run()
	 */
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
			response = Unicast.sendPOST(info.toString(), client.getServerip());
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
