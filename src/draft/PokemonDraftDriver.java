package draft;
import java.io.IOException;
import java.util.ArrayList;

import pokemon.Tier;

import server.MultithreadedTCPServer;

public class PokemonDraftDriver
{
	public static void main(String[] args)
	{
		int portNum = 8000;
		int numberOfDrafters = 3;
		int packSize = 6;
		String dataLocation = "PokemonData.txt";
		ArrayList<Tier> tiers = new ArrayList<Tier>();
				
		for (int i = 0; i < args.length; i++)
		{
			try
			{
				if (args[i].equals("-p"))
				{
					portNum = Integer.parseInt(args[i + 1]);
				}
				if (args[i].equals("-n"))
				{
					numberOfDrafters = Integer.parseInt(args[i + 1]);
				}
				else if (args[i].equals("-s"))
				{
					packSize = Integer.parseInt(args[i + 1]);
				}
				else if (args[i].equals("-d"))
				{
					dataLocation = args[i + 1];
				}
				else if (args[i].equals("-t"))
				{
					for (int j = i + 1; j < args.length; j++)
					{
						tiers.add(Tier.valueOf(args[j]));
					}
				}
			}
			catch (Exception exception)
			{
				System.err.println("Error with argument at index " + i + ".");
			}
			
		}
		
		try
		{
			MultithreadedTCPServer server =
					new MultithreadedTCPServer(portNum, numberOfDrafters, packSize, dataLocation, tiers);
			new Thread(server).start();
		}
		catch (IOException e)
		{
			System.err.println("Error: Port " + portNum + " is in use.");
		}
	}
}
