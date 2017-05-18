//por chunk
public class Download {
    private String filename;
    private int numChunks;
    private String message = "check";

    public Download(){
    	
    }

    public Download(String filename, int numChunks){
        this.filename = filename;
        this.numChunks = numChunks;
    }
    
    @Override
    public String toString() {
        return "Download{" +
                "filename=" + this.filename.toString() +
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

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}


}
