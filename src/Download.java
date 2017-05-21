import java.util.HashMap;

/**
 * The Protocol Download.
 */
public class Download extends Protocol{
    
    /** The file's name. */
    private String filename;
    
    /** File's total number of chunks. */
    private int numChunks;

    /**
     * Instantiates a new download.
     *
     * @param client the client
     * @param filename the file's name
     * @param numChunks the total number of chunks
     */
    public Download(Client client, String filename, int numChunks){
    	super(client);
    	
        this.filename = filename;
        this.numChunks = numChunks;
    }
    
    /* For debug purposes
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Download{" +
                "filename=" + this.filename.toString() +
                '}';
    }

	/**
	 * Gets the number of chunks.
	 *
	 * @return the number of chunks
	 */
	public int getNumChunks() {
		return numChunks;
	}

	/**
	 * Gets the filename.
	 *
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/* Executs the protocol by creating a GetChunk protocol for each of the chunks
	 * and sending it for execution
	 * @see java.lang.Runnable#run()
	 */
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
