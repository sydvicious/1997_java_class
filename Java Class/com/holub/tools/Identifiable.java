package com.holub.tools;

import java.io.*;
import com.holub.tools.Identity;

/**
 * Should be implemented by all objects that can provide an
 * Identity.
 *
 * @see Identity
 * @see Selection
 */

public interface Identifiable extends Serializable
{
    Identity key();

    public class Not_identifiable extends Exception
    {
	public Not_identifiable()
	{
	    super("this element is not of class Identifiable");
	}
	
	public Not_identifiable(String s)
	{
	    super(s);
	}
    }
}