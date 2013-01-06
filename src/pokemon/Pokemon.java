package pokemon;

import java.util.ArrayList;

public class Pokemon
{
	private String name;
	private ArrayList<Tier> tiers = new ArrayList<Tier>();
	
	public Pokemon(String name)
	{
		this.name = name;
	}
	
	public Pokemon(String name, ArrayList<Tier> tiers)
	{
		this.name = name;
		this.tiers.addAll(tiers);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pokemon other = (Pokemon) obj;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equalsIgnoreCase(other.name))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public ArrayList<Tier> getTiers()
	{
		return tiers;
	}
}
