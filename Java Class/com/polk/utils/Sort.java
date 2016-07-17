package com.polk.utils;

import com.holub.tools.Comparable;

/**
 * Some sort utilities. Currenlty, only shell sort implemented. Sorts in either
 * direction.
 */
public class Sort
{
    /**
     * Sort the array with smallest element first.
     */
    public static final int ASCENDING = 0;

    /**
     * Sort the array with largest element first.
     */
    public static final int DESCENDING = 1;


    /**
     * A standard shell sort.
     */
    public static void shell(Comparable[] array, int direction)
	    throws Comparable.Not_comparable
    {
	int i,j;
	int gap;

	for (gap = 1; gap <= array.length; gap = 3*gap + 1)
	    ;

	for (gap /= 3; gap > 0; gap /= 3)
	    for (i = gap; i < array.length; i++)
	    forloop3:
		for (j = i - gap; j >= 0; j -= gap)
		{
		    if (direction == DESCENDING) 
		    {
			if (array[j].compare(array[j+gap]) <= 0)
			    break forloop3;
		    }
		    else if (direction == ASCENDING) 
		    {
			if (array[j].compare(array[j+gap]) >= 0)
			    break forloop3;
		    }

		    Comparable tmp = array[j];
		    array[j] = array[j + gap];
		    array[j + gap] = tmp;
		}
    }
}