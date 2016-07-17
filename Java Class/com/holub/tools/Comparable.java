package com.holub.tools;

public interface Comparable
{
    public class Not_comparable extends Exception
    {
	public Not_comparable()
	{
	    super("tried to compare incompatible types");
	}
	
	public Not_comparable(String s)
	{
	    super(s);
	}
    }

    int compare(Comparable other) throws Not_comparable;
}
