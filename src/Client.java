import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Client {
	private String location = null;
	private Locator locator;
	private String serverip;
	private Unicast unicast;
	public BlockingQueue<Object> queue;

	public static void main(String[] args){
		Client c = new Client();
		c.setServerip(args[0]);
	}

	public Client() {
		super();
		locator = new Locator(this);
		unicast = new Unicast(this);


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

	//TODO Thread para ler sempre localização e enviar

	//TODO Thread para receber notificações e ficheiros	



	//TODO RESTORE para Client reconstruir ficheiro a partir dos chunks recebidos

}
