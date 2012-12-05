package Server;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultithreadedTCPServer implements Runnable {

	private ServerSocket socket;
	private ExecutorService threadPool;
	private Hashtable<String, Socket> users = new Hashtable<String, Socket>();
	private PokemonDraft draft;
	private String pokemonFile;
	private int port;
	
	public MultithreadedTCPServer(int port, int poolSize, String pokemonFile) throws IOException {
		socket = new ServerSocket(port);
		threadPool = Executors.newFixedThreadPool(poolSize);
		this.port = port;
		this.pokemonFile = pokemonFile;
	}
	
	@Override
	public void run() {
		System.out.println("Server started");
		System.out.println("Listening for requests on port " + port);
		
		try {
			draft = new PokemonDraft(pokemonFile);
		} catch (FileNotFoundException exception) {
			exception.printStackTrace();
			System.exit(1);
		}
		System.out.println("PokemonDraft database created");
		
		while(true) {
			try {
				Socket client = socket.accept();
				threadPool.execute(new ClientHandler(client));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			} 
		}
	}
	
	class ClientHandler implements Runnable {

		private Socket client;		
		private Scanner input;
		private OutputStream output;
		
		public ClientHandler(Socket client) {
			this.client = client;
		}
		
		@Override
		public void run() {
			try {
				input = new Scanner(client.getInputStream());
				output = client.getOutputStream();
				
				output.write("Who are you?\n".getBytes());
				String user = input.next();
				users.put(user, client);
				System.out.println(user + " joined the server.");
				
				for (int i = 0; i < 6; i++) {
					String pokemon = draft.getPokemon();
					output.write(pokemon.getBytes());
					output.write("\n".getBytes());
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}		
	}
}
