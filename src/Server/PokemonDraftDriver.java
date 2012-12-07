package Server;
import java.io.IOException;

public class PokemonDraftDriver {

	public static void main(String[] args) {
		int portNum = 8000;
		boolean serverStarted = false;
		
		do {
			try {
				MultithreadedTCPServer server = new MultithreadedTCPServer(portNum, 3, "PokemonData.txt");
				serverStarted = true;
				new Thread(server).start();
			} catch (IOException e) {
				portNum ++;
			}
		} while (!serverStarted);
	}
}
