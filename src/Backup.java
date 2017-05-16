//por chunk
public class Backup {
    private Chunk chunk;

    public Backup(){}

    public Backup(Chunk c){
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


}
