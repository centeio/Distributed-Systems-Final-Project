
public class Client {
	private String localization = null;
	private Locator locator;

	public Client() {
		super();
		locator = new Locator(this);
	}
	
	

	public String getLocalization() {
		return localization;
	}

	public void setLocalization(String localization) {
		this.localization = localization;
	}
	
	//TODO main with args: ServerIP 
	
	//TODO BlockingQueue para: guardar chunks, fazer restore do ficheiro 
	
	//TODO Thread para ler sempre localização e enviar
	
	//TODO Thread para receber notificações e ficheiros
	
	//TODO Thread para 
	
	//
	
	
	
	//TODO RESTORE para Client reconstruir ficheiro a partir dos chunks recebidos

}
