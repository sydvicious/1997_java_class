package com.polk.utils;

import java.awt.Choice;
import java.util.*;
import com.holub.tools.*;

public class Hashtable_Utils
{
    /**
     * Returns a sorted list of comparables for use in interface elements
     * and other things. All elements must have implemented
     * com.holub.tools.Comparable.
     */
    public static Comparable[] get_sorted_elements(Hashtable table, int direction) 
	    throws Comparable.Not_comparable
    {
	Enumeration list = table.keys();

	int size = 0;

	while (list.hasMoreElements())
	{
	    size++;
	    list.nextElement();
	}

	Comparable[] array = new Comparable[size];

	list = table.keys();

	for (int i = 0; i < size; i++)
	{
	    array[i] = (Comparable) list.nextElement();
	    if (!(array[i] instanceof Comparable))
	    {
		throw new Comparable.Not_comparable();
	    }
	}

	try
	{
	    Sort.shell(array, direction);
	}
	catch (Comparable.Not_comparable e)
	{
	    throw e;
	}

	return array;
    }

    /**
     * Returns an AWT widget with all of the keys in the table in the list.
     */

    public static Choice get_chooser(Hashtable table)
	    throws Identifiable.Not_identifiable, Comparable.Not_comparable
    {
	Choice choice = new Choice();
	Enumeration list = table.keys();
	while (list.hasMoreElements())
	{
	    choice.add(((Identity) list.nextElement()).toString());
	}
	return choice;
    }

    /**
     * Returns the Identity of the object returned from the choice created above.
     */

    public static Identity get_selection_from_chooser(Hashtable table, Choice choice)
	    throws Identifiable.Not_identifiable, Comparable.Not_comparable
    {
	int index = choice.getSelectedIndex();
	Enumeration list = table.keys();
	Identity id = null;
	int i = 0;

	while (list.hasMoreElements() && (i <= index))
	    id = ((Identity) list.nextElement());
	    
	return id;
    }
}