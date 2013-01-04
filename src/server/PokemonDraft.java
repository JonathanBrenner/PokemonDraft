package server;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import pokemon.Pokemon;

public class PokemonDraft
{
	private int numberOfPokemon = 0;
	private String fileName;
	private ArrayList<Pokemon> pokemonList = new ArrayList<Pokemon>();
	private Random random = new Random();
	
	public PokemonDraft(String fileName) throws FileNotFoundException
	{
		this.fileName =  fileName;
		readData();
	}
	
	public void readData() throws FileNotFoundException
	{
		File dataFile = new File(fileName);
		Scanner fileReader = new Scanner(dataFile);
		
		while (fileReader.hasNext())
		{
			String data = fileReader.nextLine();
			Pokemon pokemon = new Pokemon(fileReader.nextLine());
			pokemonList.add(pokemon);
			numberOfPokemon ++;
		}
	}
	
	public Pokemon getPokemon()
	{
		int randomIndex = random.nextInt(numberOfPokemon);
		Pokemon pokemon = pokemonList.remove(randomIndex);
		numberOfPokemon --;
		return pokemon;
	}
}
