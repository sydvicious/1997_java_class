package com.polk.appointment;

import com.holub.tools.Identity;
import com.polk.utils.Component_Interface;
import com.polk.utils.Hashtable_Utils;
import com.polk.utils.Sort;
import java.awt.Choice;
import java.awt.Component;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * A collection of time slots. Slots can be added, removed, and checked to see
 * if they overlap another.
 */
class Calendar implements Serializable, Component_Interface
{
    Hashtable time_slots = new Hashtable();
    Choice slot_choice = null;
	
    /**
     * If the given time slot does not overlap with any in the given calendar,
     * this will return true.
     */
    public boolean confirm_slot(Identity time_slot)
    {
	Enumeration keys = time_slots.keys();

	while (keys.hasMoreElements())
	{
	    Time_Identity slot = (Time_Identity) keys.nextElement();
	    
	    if (((Time_Identity)time_slot).overlaps(slot))
	    {
		return false;
	    }
	}
	return true;
    }
	
    /**
     * Go ahead and add the time slot to the calendar.
     */
    public void add_slot(Identity time_slot)
    {
	time_slots.put(time_slot, time_slot.item());
    }
	
    /**
     * Remove this time_slot.
     */
    public void remove_slot(Identity time_slot)
    {
	time_slots.remove(time_slot);
    }

    /**
     * Prints today's schedule.
     */
    public String toString()
    {
	Identity today = Time_Slot.get_today();
	StringBuffer buf = new StringBuffer("");

	Identity[] array = new Identity[time_slots.size()];
	Enumeration list = time_slots.keys();
	int i = 0;
	while (list.hasMoreElements()) {
	    array[i] = (Identity) list.nextElement();
	    i++;
	}
	try
	{
	    Sort.shell(array, Sort.DESCENDING);

	    for (i = 0; i < time_slots.size(); i++)
	    {
		if (array[i].compare(today) > 0)
		{
		    buf.append(((Time_Identity)array[i]).toString());
		    buf.append("\n");
		}    
	    }
	}
	catch (Exception e)
	{
	    System.err.println(e);
	}

	return buf.toString();
    }

    /**
     * Returns a component that represents this component. For the calendar, 
     * this is a choice box.
     */
    public Component proxy()
    {
	try
	{
	    slot_choice = Hashtable_Utils.get_chooser(time_slots);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    slot_choice = null;
	}
	return slot_choice;
    }

    /**
     * Returns the id of the item selected above.
     */
    public Identity confirm_ui()
    {
	Identity id = null;

	try
	{
	    id = Hashtable_Utils.get_selection_from_chooser(time_slots, slot_choice);
	    System.out.println(id);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return id;
    }
}
