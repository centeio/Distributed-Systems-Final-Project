
public class GetChunk {
	int chunkNo;
	String filename;
	int noChunks;
	
	public GetChunk(int chunkNo, String filename, int noChunks) {
		super();
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
}
