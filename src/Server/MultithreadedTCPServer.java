package Server;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultithreadedTCPServer implements Runnable {

	private ServerSocket socket;
	private ExecutorService threadPool;
	private ArrayList<User> users = new ArrayList<User>();
	private ArrayList<ArrayList<String>> userOptions = new ArrayList<ArrayList<String>>();
	private ArrayList<Boolean> usersAreReady = new ArrayList<Boolean>();
	private PokemonDraft draft;
	private String pokemonFile;
	private int portNum;
	private int numberOfUsers;
	private int draftSize;
	
	public MultithreadedTCPServer(int portNum, int numberOfUsers, int draftSize, String pokemonFile) throws IOException {
		socket = new ServerSocket(portNum);
		threadPool = Executors.newFixedThreadPool(numberOfUsers);
		this.portNum = portNum;
		this.numberOfUsers = numberOfUsers;
		this.pokemonFile = pokemonFile;
		this.draftSize = draftSize;
	}
	
	private void waitForUsers() {
		int numberOfReadyUsers = 0;		
		while (numberOfReadyUsers != numberOfUsers) {
			numberOfReadyUsers = 0;
			for (Boolean ready : usersAreReady) {
				if (ready == true) {
					numberOfReadyUsers ++;
				}
			}
		}
	}
	
	private void clearReadyStatus() {
		for (int j = 0; j < numberOfUsers; j++) {
			usersAreReady.set(j, false);
		}
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
		
		for (int i = 0; i < numberOfUsers; i++) {
			try {
				Socket clientSocket = socket.accept();
				ArrayList<String> pokemonChoices = new ArrayList<String>();
				for (int j = 0; j < draftSize; j++) {
					pokemonChoices.add(draft.getPokemon());
				}
				userOptions.add(pokemonChoices);
				usersAreReady.add(false);
				threadPool.execute(new ClientHandler(clientSocket, i));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			} 
		}
		waitForUsers();
		
		for (int i = 0; i < 6; i++) {
			clearReadyStatus();
			for (int j = 0; j < numberOfUsers; j++) {
				users.get(j).setCanProceed(true);
			}
			waitForUsers();
			
			System.out.println("All players have made their draft picks");
			
			ArrayList<String> tempOptions = new ArrayList<String>();
			tempOptions.addAll(userOptions.get(0));
			for (int j = 0; j < numberOfUsers - 1; j++) {
				userOptions.set(j, userOptions.get(j + 1));
			}
			userOptions.set(numberOfUsers - 1, tempOptions);
		}	
	}
	
	class ClientHandler implements Runnable {

		private Socket client;
		private int userId;
		private Scanner input;
		private OutputStream output;
		private ArrayList<String> draftedPokemon = new ArrayList<String>();
		
		public ClientHandler(Socket client, int userId) {
			this.client = client;
			this.userId = userId;
		}
		
		public void run() {
			try {
				input = new Scanner(client.getInputStream());
				output = client.getOutputStream();
				
				output.write("Who are you? ".getBytes());
				String username = input.nextLine();
				User user = new User(username, client);
				users.add(user);
				System.out.println(username + " joined the server.");
				usersAreReady.set(userId, true);
				
				for (int i = 0; i < 6; i++) {
					while (!user.canProceed()) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					draft();
					user.setCanProceed(false);
				}
				
				output.write("Your team is: \n".getBytes());
				for (String draftee : draftedPokemon) {
					output.write((draftee + "\n").getBytes());
				}
				
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
		
		private void draft() {
			try{
				ArrayList<String> pokemonOptions = userOptions.get(userId);
				for (String pokemonOption : pokemonOptions) {
					output.write((pokemonOption + "\n").getBytes());
				}
				
				boolean choiceMade = false;
				String choice = "";
				while (!choiceMade) {
					output.write("Please choose one of the Pokemon: ".getBytes());
					choice = input.nextLine();				
					choiceMade = pokemonOptions.remove(choice);
				}
				draftedPokemon.add(choice);
				userOptions.set(userId, pokemonOptions);
				usersAreReady.set(userId, true);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
}
