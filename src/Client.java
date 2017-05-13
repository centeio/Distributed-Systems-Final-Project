import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.management.Notification;
import javax.management.NotificationListener;


public class Client implements NotificationListener {
	private String location = null;
	private Locator locator;
	private String serverip;
	private Unicast unicast;
	private String id;
	private Queue<ArrayList<String>> actions;
	public BlockingQueue<Object> queue;

	public static void main(String[] args){
		Client c = new Client();
		c.setServerip(args[0]);
	}

	public Client() {
		super();
		locator = new Locator(this);
		setUnicast(new Unicast(this));


		ExecutorService executor = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 1; i++) {
			Runnable worker = new Operator(this);
			executor.execute(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {}
	}


	public String getlocation() {
		return location;
	}

	public void setlocation(String location) {
		this.location = location;
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

	//TODO Thread para receber notificações e ficheiros	

}
