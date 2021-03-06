package server;

import java.net.Socket;
import java.util.ArrayList;

public class User
{
	private String name;
	private Socket socket;
	private boolean canProceed = false;

	private ArrayList<String> pokemonChoices = new ArrayList<String>();
	
	public User (String name, Socket socket)
	{
		this.name = name;
		this.socket = socket;
	}

	public void addPokemon(String pokemon)
	{
		pokemonChoices.add(pokemon);
	}
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Socket getSocket()
	{
		return socket;
	}

	public void setSocket(Socket socket)
	{
		this.socket = socket;
	}
	
	public boolean canProceed()
	{
		return canProceed;
	}

	public void setCanProceed(boolean canProceed)
	{
		this.canProceed = canProceed;
	}
}
