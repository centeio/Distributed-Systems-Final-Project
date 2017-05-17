//por chunk
public class Download {
    private Chunk chunk;
    private String message = "check";

    public Download(){
    	
    }

    public Download(Chunk c){
        this.chunk = c;
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


}
