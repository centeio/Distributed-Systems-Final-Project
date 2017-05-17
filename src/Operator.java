import java.io.*;
import java.net.MulticastSocket;
import java.util.ArrayList;

public class Operator implements Runnable{
	private Client c;
	private ArrayList<Chunk> chunks;

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
					System.out.println(((Download) protocol).getMessage());
					//NO CLIENTE
					/*if(protocol.chunk() == null) NUNO
					 * ciclo com até i=nochunks
					 *   novo processo de 0 a nochunks para pedir chunk a Server (com filename e filelength)
					 *   colocar processo em client.queue
					 */
				}else if(protocol instanceof GetChunk){
					//NO CLIENTE
					/*cria pedido para servidor com chunk no x
					 *timeout se nao receber INES (e enviar do server)
					 *quando recebe, SE NÃO EXISTIR coloca em array de ficheiro filename ConcurrentHashMap<String filename,Map<int no, Chunks>>
					 *se length do Map do filename == a nochunks cria new Restore(filename)
					 **/
					
				}else if(protocol instanceof Restore){ //Client receiving file from client
					//NO CLIENTE 
					//TODO alterar funcao para map NUNO

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
	 * Divides a file into an array of chunks with 64KB of size.
	 * Algorithm based on the following StackOverflow question/answer.
	 * http://stackoverflow.com/questions/4431945/split-and-join-back-a-binary-file-in-java
	 * @param name Name of the file
	 */
	public void divideFileIntoChunks(String name){
		try{
			File file = new File(name);

			this.chunks = new ArrayList<Chunk>();

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
				this.chunks.add(c);
			}

			stream.close();
			socket.close();
		}catch(FileNotFoundException e){
			System.out.println("File " + name + " not found");
			return;
		}catch(SecurityException e){
			System.out.println("Denied reading file " + name);
			return;
		} catch (IOException e) {
			System.out.println("Error closing stream of file " + name);
			return;
		}
	}

	/**
	 * Restores a file from an array of chunks.
	 * Algorithm based on the following StackOverflow question/answer.
	 * http://stackoverflow.com/questions/4431945/split-and-join-back-a-binary-file-in-java
	 */
	public void restoreFile(){
		String name = this.chunks.get(0).getFilename();
		File file = new File(name);

		FileOutputStream restoredFile;

		try{
			restoredFile = new FileOutputStream(file, true);
			for(Chunk c: this.chunks){
				byte[] fileData = c.getData();

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
