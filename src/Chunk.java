public class Chunk {
    /**
     * Chunk number
     */
    private int number;

    /**
     * Name of the file that originated the chunk
     */
    private String filename;

    /**
     * Chunk information
     */
    private byte[] data;

    /**
     * Empty Chunk constructor;
     */
    public Chunk(){}

    /**
     * Initializes Chunk with a number and the name of the file
     * that originated it.
     * @param number Number of the chunk
     * @param filename Name of the file that originated the chunk
     */
    public Chunk(int number, String filename, byte[] data){
        this.number = number;
        this.filename = filename;
        this.data = data;
    }

    /**
     * Changes the number of the Chunk
     * @param number New Chunk number
     */
    public void setNumber(int number){
        this.number = number;
    }

    /**
     * Returns the number of the Chunk
     * @return Chunk number
     */
    public int getNumber(){ return this.number;}

    /**
     * Changes the name of the file that originated the Chunk
     * @param filename New filename
     */
    public void setFilename(String filename){ this.filename = filename;}

    /**
     * Returns the name of the file that originated the Chunk
     * @return Chunk filename
     */
    public String getFilename(){ return this.filename;}

    /**
     * Changes the data of the chunk
     * @param data New data
     */
    public void setData(byte[] data){ this.data = data;}

    /**
     * Returns the chunk data
     * @return Chunk data
     */
    public byte[] getData(){ return this.data;}

    /**
     * Textual representation of the Chunk
     * @return Textual representation
     */
    @Override
    public String toString() {
        return "Chunk number " + this.number + " from file " + this.filename + " with " + this.data.length + " bytes of information.";
    }
}

