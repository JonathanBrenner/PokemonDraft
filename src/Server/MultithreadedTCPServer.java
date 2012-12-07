package Server;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
	private int portNum;
	private int numberOfPlayers;
	
	public MultithreadedTCPServer(int portNum, int numberOfPlayers, String pokemonFile) throws IOException {
		socket = new ServerSocket(portNum);
		threadPool = Executors.newFixedThreadPool(numberOfPlayers);
		this.portNum = portNum;
		this.numberOfPlayers = numberOfPlayers;
		this.pokemonFile = pokemonFile;
	}
	
	public void run() {
		System.out.println("Server started");
		System.out.println("Listening for requests on port " + portNum);
		
		try {
			draft = new PokemonDraft(pokemonFile);
		} catch (FileNotFoundException exception) {
			exception.printStackTrace();
			System.exit(1);
		}
		System.out.println("PokemonDraft database created");
		
		for (int i = 0; i < numberOfPlayers; i++) {
			try {
				Socket client = socket.accept();
				threadPool.execute(new ClientHandler(client));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			} 
		}
		
		System.out.println("Everyone is here.");
	}
	
	class ClientHandler implements Runnable {

		private Socket client;		
		private Scanner input;
		private OutputStream output;
		private ArrayList<String> pokemonOptions = new ArrayList<String>();
		private ArrayList<String> pokemonChoices = new ArrayList<String>();
		
		public ClientHandler(Socket client) {
			this.client = client;
		}
		
		public void run() {
			try {
				input = new Scanner(client.getInputStream());
				output = client.getOutputStream();
				
				output.write("Who are you? ".getBytes());
				String user = input.next();
				users.put(user, client);
				System.out.println(user + " joined the server.");
				
				for (int i = 0; i < 6; i++) {
					String pokemon = draft.getPokemon();
					pokemonOptions.add(pokemon);
					output.write(pokemon.getBytes());
					output.write("\n".getBytes());
				}
				
				boolean choiceMade = false;
				while (choiceMade) {
					output.write("Please choose one of the Pokemon.\n".getBytes());
					String choice = input.nextLine();
					
					choiceMade = pokemonOptions.remove(choice);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}		
	}
}
