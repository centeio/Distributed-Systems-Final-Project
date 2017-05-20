
public abstract class Protocol implements Runnable{
	Client client;
	
	Protocol(Client client){
		this.client = client;
	}
}
