import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class Restore extends Protocol{
	private String filename;

    public Restore(Client client, String filename){
    	super(client);
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
    
	/**
	 * Restores a file from an array of chunks.
	 * Algorithm based on the following StackOverflow question/answer.
	 * http://stackoverflow.com/questions/4431945/split-and-join-back-a-binary-file-in-java
	 */
	@Override
	public void run() {
		File file = new File(filename);
		try {
			Files.deleteIfExists(file.toPath());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		FileOutputStream restoredFile;

		try{
			restoredFile = new FileOutputStream(file, true);
			for(int i = 0; i < client.files.get(filename).size(); i++){
				
				byte[] fileData = client.files.get(filename).get(i).getData();
	
				restoredFile.write(fileData);
				restoredFile.flush();
			}

			restoredFile.close();
		}catch(FileNotFoundException e){
			System.out.println("File " + filename + " not found");
			return;
		}catch(SecurityException e){
			System.out.println("Denied reading file " + filename);
			return;
		} catch (IOException e) {
			System.out.println("Error closing stream of file " + filename);
			e.printStackTrace();
		}
	}

	
}