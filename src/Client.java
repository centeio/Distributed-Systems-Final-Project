import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * The Client
 */
public class Client {
	
	/** client's location. */
	private String location = null;
	
	/** is the location updated? */
	private boolean locationupdated = true;
	
	/** The locator. */
	private Locator locator;
	
	/** The server's ip address. */
	private String serverip;
	
	/** client's username. */
	private String username;
	
	/** client's actions (likes)*/
	private Queue<ArrayList<String>> actions;
	
	/** executs the protocols necessary */
	public ExecutorService executor;
	
	/** the files being downloaded */
	public ConcurrentHashMap<String, HashMap<Integer, Chunk>> files;
	
	/** a socket for notifications */
	private SSLSocket socket;

	/**
	 * Gets the socket.
	 *
	 * @return the socket
	 */
	public SSLSocket getSocket() {
		return socket;
	}

	/**
	 * Sets the socket.
	 *
	 * @param socket the new socket
	 */
	public void setSocket(SSLSocket socket) {
		this.socket = socket;
	}

	/**
	 * Receives the server's ip and client's username and iniciates the client
	 *
	 * @param args server's ip address and client's username
	 * @throws UnknownHostException the unknown host exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws UnknownHostException, IOException{
		if(args.length < 2){
			System.out.println("Usage: Client <server ip> <username>");
			return;
		}
		Client c = new Client(args[0], args[1]);
	}

	/**
	 * Instantiates a new client.
	 * Initializes a locator and starts connection with server for notifications
	 * Also, creates thread to request services from Server
	 *
	 * @param Ip the server's ip address
	 * @param username the username chosen
	 * @throws UnknownHostException the unknown host exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Client(String Ip, String username) throws UnknownHostException, IOException {
		super();

		this.username = username;
		locator = new Locator(this);
		actions = new PriorityQueue<ArrayList<String>>();
		files = new ConcurrentHashMap<String, HashMap<Integer, Chunk>>();
		serverip = Ip;
		
		startConnection();
		
		locator.start();
		new MessageCS(this);

		File dir = new File(username);
		dir.mkdir();
				
		executor = Executors.newFixedThreadPool(5);
	}


	/**
	 * Start connection for notifications.
	 *
	 * @throws UnknownHostException the unknown host exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void startConnection() throws UnknownHostException, IOException {
		System.setProperty("javax.net.ssl.keyStore", "../files/client.keys");
		System.setProperty("javax.net.ssl.keyStorePassword", "123456");
		System.setProperty("javax.net.ssl.trustStore", "../files/truststore");
		System.setProperty("javax.net.ssl.trustStorePassword", "123456");

		//Create socket
		SSLSocket s = null;  
		SSLSocketFactory ssf = null;  

		ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();  

		try {
			s = (SSLSocket) ssf.createSocket(serverip, 6458);  
		}  
		catch( IOException e) {  
			System.out.println("Client - Failed to create SSLSocket");  
			e.getMessage();
			e.printStackTrace();
			return;  
		} 

		//Start handshake
		try {
			PrintWriter printer = new PrintWriter(s.getOutputStream(),true);
			printer.println("Connect");
		} catch (IOException e) {
			System.out.println("Client - Failed to handshake");
			e.getMessage();
		}
		
		socket = s;
		socket.setKeepAlive(true);

		NewLike newLike = new NewLike(this);
		new Thread(newLike).start();
	}



	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public String getlocation() {
		return location;
	}

	/**
	 * Sets the location.
	 *
	 * @param location the new location
	 */
	public void setlocation(String location) {
		this.location = location;
		this.locationupdated = false;
	}

	/**
	 * Gets the serverip.
	 *
	 * @return the serverip
	 */
	public String getServerip() {
		return serverip;
	}

	/**
	 * Sets the serverip.
	 *
	 * @param serverip the new serverip
	 */
	public void setServerip(String serverip) {
		this.serverip = serverip;
	}

	/**
	 * Gets the next action.
	 *
	 * @return the next action
	 */
	public ArrayList<String> getNextAction() {		
		return actions.poll();
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Locationupdated.
	 *
	 * @return true, if successful
	 */
	public boolean locationupdated() {
		return locationupdated;
	}

	/**
	 * Sets the locationupdated.
	 *
	 * @param locationupdated the new locationupdated
	 */
	public void setLocationupdated(boolean locationupdated) {
		this.locationupdated = locationupdated;
	}

	/**
	 * Adds the action.
	 *
	 * @param action the action
	 */
	public void addAction(ArrayList<String> action) {
		actions.add(action);

	}

	/**
	 * Gets the locator.
	 *
	 * @return the locator
	 */
	public Locator getLocator() {
		return locator;
	}

}
