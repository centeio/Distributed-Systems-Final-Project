import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.management.Notification;
import javax.management.NotificationListener;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;


public class Client implements NotificationListener {
	private String location = null;
	private boolean locationupdated = true;
	private Locator locator;
	private String serverip;
	private Unicast unicast;
	private String id;
	private Queue<ArrayList<String>> actions;
	public BlockingQueue<Object> queue;
	public ConcurrentHashMap<String, HashMap<Integer, Chunk>> files;
	private SSLSocket socket;

	public SSLSocket getSocket() {
		return socket;
	}

	public void setSocket(SSLSocket socket) {
		this.socket = socket;
	}

	public static void main(String[] args) throws UnknownHostException, IOException{
		Client c = new Client();
		c.setServerip(args[0]);
	}

	public Client() throws UnknownHostException, IOException {
		super();

		locator = new Locator(this);
		setUnicast(new Unicast(this));
		actions = new PriorityQueue<ArrayList<String>>();
		queue = new LinkedBlockingQueue<Object>();
		files = new ConcurrentHashMap<String, HashMap<Integer, Chunk>>();

		startConnection();

		locator.start();
		new MessageCS(this);

		ExecutorService executor = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 1; i++) {
			Runnable worker = new Operator(this);
			executor.execute(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {}
	}


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
			s = (SSLSocket) ssf.createSocket("127.0.0.1", 6458);  
		}  
		catch( IOException e) {  
			System.out.println("Client - Failed to create SSLSocket");  
			e.getMessage();  
			return;  
		} 

		//Start handshake
		try {
			s.startHandshake();
		} catch (IOException e) {
			System.out.println("Client - Failed to handshake");
			e.getMessage();
		}
		
		PrintWriter printer = new PrintWriter(s.getOutputStream(),true);
		printer.println("Connect");
		
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Unicast getUnicast() {
		return unicast;
	}

	public void setUnicast(Unicast unicast) {
		this.unicast = unicast;
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

	//TODO Thread para receber notificaes e ficheiros	

}
