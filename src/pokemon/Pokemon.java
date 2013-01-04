package pokemon;

import draft.Draftee;

public class Pokemon extends Draftee
{
	Tier tier;
	
	public Pokemon(String name)
	{
		super(name);
	}
	
	public Pokemon(String name, Tier tier)
	{
		super(name);
		this.tier = tier;
	}
	
	public String toString()
	{
		return super.getName();
	}
	
	public Tier getTier()
	{
		return tier;
	}

	public void setTier(Tier tier)
	{
		this.tier = tier;
	}
}
