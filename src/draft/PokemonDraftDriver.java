package draft;
import java.io.IOException;

import pokemon.Pokemon;

import server.MultithreadedTCPServer;


public class PokemonDraftDriver
{

	public static void main(String[] args)
	{
		int portNum = 8000;
		boolean serverStarted = false;
		
		do 
		{
			try
			{
				MultithreadedTCPServer<Pokemon> server = new MultithreadedTCPServer<Pokemon>(portNum, 3, 7, "PokemonData.txt");
				serverStarted = true;
				new Thread(server).start();
			}
			catch (IOException e)
			{
				portNum ++;
			}
		} while (!serverStarted);
	}
}
