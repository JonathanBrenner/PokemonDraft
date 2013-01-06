package pokemon;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class PokemonDraft
{
	private String fileName;
	private ArrayList<Pokemon> pokemonList = new ArrayList<Pokemon>();
	private ArrayList<Tier> tierList = new ArrayList<Tier>();
	private ArrayList<Pokemon> validPokemon = new ArrayList<Pokemon>();
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
			String line = fileReader.nextLine();
			String[] data = line.split(" ");
			String name = data[0];		
			String[] stringTiers = data[1].split(",");
			ArrayList<Tier> tiers = new ArrayList<Tier>();
			
			for (String tier : stringTiers)
			{
				tiers.add(Tier.valueOf(tier));
			}
			
			Pokemon pokemon = new Pokemon(name, tiers);
			pokemonList.add(pokemon);
			validPokemon.add(pokemon);
		}
	}
	
	public void setTiers(ArrayList<Tier> newTiers)
	{
		tierList.clear();
		tierList.addAll(newTiers);
		
		validPokemon.clear();
		
		ArrayList<Tier> pokemonTiers;
		
		for (Pokemon pokemon : pokemonList)
		{
			pokemonTiers = pokemon.getTiers();
			for (Tier tier : pokemonTiers)
			{
				if (newTiers.contains(tier))
				{
					validPokemon.add(pokemon);
					break;
				}
			}
		}
	}
	
	public Pokemon getPokemon()
	{
		int randomIndex = random.nextInt(validPokemon.size());
		Pokemon pokemon = validPokemon.remove(randomIndex);
		return pokemon;
	}
}
