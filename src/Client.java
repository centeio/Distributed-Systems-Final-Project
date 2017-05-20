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

import javax.management.Notification;
import javax.management.NotificationListener;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;


public class Client implements NotificationListener {
	private String location = null;
	private boolean locationupdated = true;
	private Locator locator;
	private String serverip;
	private String username;
	private Queue<ArrayList<String>> actions;
	public ExecutorService executor;
	public ConcurrentHashMap<String, HashMap<Integer, Chunk>> files;
	private SSLSocket socket;

	public SSLSocket getSocket() {
		return socket;
	}

	public void setSocket(SSLSocket socket) {
		this.socket = socket;
	}

	public static void main(String[] args) throws UnknownHostException, IOException{
		if(args.length < 2){
			System.out.println("Usage: Client <server ip> <username>");
			return;
		}
		Client c = new Client(args[1]);
		c.setServerip(args[0]);
	}

	public Client(String username) throws UnknownHostException, IOException {
		super();

		this.username = username;
		locator = new Locator(this);
		actions = new PriorityQueue<ArrayList<String>>();
		files = new ConcurrentHashMap<String, HashMap<Integer, Chunk>>();

		startConnection();
		
		locator.start();
		new MessageCS(this);

		executor = Executors.newFixedThreadPool(5);
		
		
	}


	private void startConnection() throws UnknownHostException, IOException {
		System.setProperty("javax.net.ssl.keyStore", "./files/client.keys");
		System.setProperty("javax.net.ssl.keyStorePassword", "123456");
		System.setProperty("javax.net.ssl.trustStore", "./files/truststore");
		System.setProperty("javax.net.ssl.trustStorePassword", "123456");

		//Create socket
		SSLSocket s = null;  
		SSLSocketFactory ssf = null;  

		ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();  

		try {
			s = (SSLSocket) ssf.createSocket("127.0.0.1", 6458);  
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



	public String getlocation() {
		return location;
	}

	public void setlocation(String location) {
		this.location = location;
		this.locationupdated = false;
	}

	public String getServerip() {
		return serverip;
	}

	public void setServerip(String serverip) {
		this.serverip = serverip;
	}

	public ArrayList<String> getNextAction() {		
		return actions.poll();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public void handleNotification(Notification not, Object obj) {
		// TODO Auto-generated method stub

	}

	public boolean locationupdated() {
		return locationupdated;
	}

	public void setLocationupdated(boolean locationupdated) {
		this.locationupdated = locationupdated;
	}

	public void addAction(ArrayList<String> action) {
		actions.add(action);

	}

	public Locator getLocator() {
		return locator;
	}

	//TODO Thread para receber notificaes e ficheiros	

}
