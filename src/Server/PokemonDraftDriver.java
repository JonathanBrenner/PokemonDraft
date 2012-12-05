package Server;
import java.io.IOException;


public class PokemonDraftDriver {

	public static void main(String[] args) {
		try {
			MultithreadedTCPServer server = new MultithreadedTCPServer(8006, 3, "PokemonData.txt");
			new Thread(server).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
