import java.io.*;
import java.util.ArrayList;

public class Restore {
    private ArrayList<Chunk> chunks;

    public Restore(){}

    public Restore(ArrayList<Chunk> chunks){
        this.chunks = chunks;
    }

    public ArrayList<Chunk> getChunk() {
        return chunks;
    }

    public void setChunk(ArrayList<Chunk> chunks) {
        this.chunks = chunks;
    }

    @Override
    public String toString() {
        return "Restore{" +
                "chunk=" + this.chunks.toString() +
                '}';
    }
}