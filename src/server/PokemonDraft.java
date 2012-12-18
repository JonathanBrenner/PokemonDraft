package server;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class PokemonDraft {
	private int numberOfPokemon = 0;
	private String fileName;
	private ArrayList<String> pokemonList = new ArrayList<String>();
	private Random random = new Random();
	
	public PokemonDraft(String fileName) throws FileNotFoundException {
		this.fileName =  fileName;
		readData();
	}
	
	public void readData() throws FileNotFoundException {
		File dataFile = new File(fileName);
		Scanner fileReader = new Scanner(dataFile);
		
		while (fileReader.hasNext()) {
			String pokemon = fileReader.nextLine();
			pokemonList.add(pokemon);
			numberOfPokemon ++;
		}
	}
	
	public String getPokemon() {
		int randomIndex = random.nextInt(numberOfPokemon);
		String pokemon = pokemonList.remove(randomIndex);
		numberOfPokemon --;
		return pokemon;
	}
}
