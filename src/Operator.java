import java.io.*;
import java.net.MulticastSocket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;


public class Operator implements Runnable{
	private Client c;
	private ArrayList<Chunk> chunks;
	private ConcurrentHashMap<String, ArrayList<Chunk>> files;

	public Operator(Client c) {
		super();
		this.chunks = new ArrayList<>();
		this.setC(c);
	}

	@Override
	public void run() {
		while(true) {
			try {
				Object protocol = this.c.queue.take();

				if(protocol instanceof Download){
					Download d = (Download) protocol;
					
					/*
					 * Download initiator
					 * 
					 * If Download protocol chunk is null, then create GetChunk protocol for each of the chunks
					 * and put it in the queue
					 */
					for(int i = 0; i < d.getNumChunks(); i++){
						this.c.queue.put(new GetChunk(i, d.getFilename(), d.getNumChunks()));
					}
					
					c.files.put(d.getFilename(), new HashMap<Integer,Chunk>());
				}else if(protocol instanceof GetChunk){
					GetChunk gc = (GetChunk) protocol;
					//NO CLIENTE
					// TODO INES cria pedido para servidor com chunk no x
					JSONObject info = new JSONObject();

					info.put("type", "getChunk");
					info.put("filename", gc.getFilename());
					info.put("chunkNo", gc.getChunkNo());

					JSONObject response = Unicast.sendPOST(info.toString());
					
					// timeout se nao receber (e enviar do server)
					
					// quando recebe, SE NAO EXISTIR coloca em array de ficheiro filename ConcurrentHashMap<String filename,Map<int no, Chunks>>
					HashMap<Integer, Chunk> chunks = c.files.get(gc.getFilename());
					
					if(chunks != null){
						if(chunks.get(gc.getChunkNo()) == null){	
							String strData = response.getString("data");
							System.out.println("Operator line 63: " + strData);
							
							byte data[] = strData.trim().getBytes("ISO-8859-1");
							Chunk c = new Chunk(gc.getChunkNo(), gc.getFilename(), data);
							
							System.out.println("Operator line 66: " + data);
							System.out.println("");
							
							chunks.put(gc.getChunkNo(), c);
						}
					
						// se length do Map do filename == a nochunks cria new Restore(filename)
						if(chunks.size() == gc.getNoChunks()){
							c.queue.put(new Restore(gc.getFilename()));
						}
					}
					else{
						System.out.println("Error Operator line 67");
					}
					
				}else if(protocol instanceof Restore){ //Client receiving file from client
					Restore r = (Restore) protocol;
					
					restoreFile(r.getFilename());
				}


			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Client getC() {
		return c;
	}

	public void setC(Client c) {
		this.c = c;
	}

	/**
	 * Restores a file from an array of chunks.
	 * Algorithm based on the following StackOverflow question/answer.
	 * http://stackoverflow.com/questions/4431945/split-and-join-back-a-binary-file-in-java
	 */
	public void restoreFile(String filename){
		String name = filename;
		File file = new File(name);

		FileOutputStream restoredFile;

		try{
			restoredFile = new FileOutputStream(file, true);
			for(int i = 0; i < this.c.files.get(filename).size(); i++){
				System.out.println("Operator lin 107: " + this.c.files.get(filename).get(i).getData());
				
				byte[] fileData = this.c.files.get(filename).get(i).getData();
	
				restoredFile.write(fileData);
				restoredFile.flush();
			}

			restoredFile.close();
		}catch(FileNotFoundException e){
			System.out.println("File " + name + " not found");
			return;
		}catch(SecurityException e){
			System.out.println("Denied reading file " + name);
			return;
		} catch (IOException e) {
			System.out.println("Error closing stream of file " + name);
			e.printStackTrace();
		}
	}
}
