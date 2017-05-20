import java.util.HashMap;

//por chunk
public class Download extends Protocol{
    private String filename;
    private int numChunks;

    public Download(Client client, String filename, int numChunks){
    	super(client);
    	
        this.filename = filename;
        this.numChunks = numChunks;
    }
    
    @Override
    public String toString() {
        return "Download{" +
                "filename=" + this.filename.toString() +
                '}';
    }

	public int getNumChunks() {
		return numChunks;
	}

	public String getFilename() {
		return filename;
	}

	@Override
	public void run() {

		/*
		 * Download initiator
		 * 
		 * If Download protocol chunk is null, then create GetChunk protocol for each of the chunks
		 * and put it in the queue
		 */
		client.files.put(filename, new HashMap<Integer,Chunk>());
		
		for(int i = 0; i < numChunks; i++){
			this.client.executor.execute(new GetChunk(client, i, filename, numChunks));
		}
	}


}
