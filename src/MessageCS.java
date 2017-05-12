import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


//para localização Client -> Server com Ação (LIKE ou null) 
public class MessageCS extends Message{
	private final ScheduledExecutorService scheduler =	Executors.newScheduledThreadPool(1);

	public MessageCS(String location, String action) {
		super();
		sender();
	}

	public void sender() {
		final Runnable sender = new Runnable() {
			public void run() { 
				
				}
		};
		
		scheduler.scheduleAtFixedRate(sender, 0, 1, TimeUnit.SECONDS);

	}


}
