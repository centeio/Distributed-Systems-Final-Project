import java.io.*;
import java.util.ArrayList;

public class Restore {
	private String filename;

    public Restore(){}

    public Restore(String filename){
        this.setFilename(filename);
    }
    
    public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
 
    @Override
    public String toString() {
        return "Restore{" +
                "filename=" + this.filename.toString() +
                '}';
    }

	
}