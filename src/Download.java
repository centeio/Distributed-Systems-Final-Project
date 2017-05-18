//por chunk
public class Download {
    private Chunk chunk;
    private int numChunks;
    private String message = "check";

    public Download(){
    	
    }

    public Download(Chunk c, int numChunks){
        this.chunk = c;
        this.numChunks = numChunks;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void setChunk(Chunk chunks) {
        this.chunk = chunks;
    }

    @Override
    public String toString() {
        return "Backup{" +
                "chunk=" + this.chunk.toString() +
                '}';
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getNumChunks() {
		return numChunks;
	}

	public void setNumChunks(int numChunks) {
		this.numChunks = numChunks;
	}


}
