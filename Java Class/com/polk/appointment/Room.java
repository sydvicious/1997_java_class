package com.polk.appointment;

import java.io.*;
import java.awt.*;
import com.holub.tools.*;

/**
 * A place to hold a meeting.
 */
class Room implements Serializable, Schedulable, Identifiable
{
    String name;
    private TextField name_box = null;

    /**
     * Returns an interface element which is used to get the room name.
     */
    public Component proxy() {
	GridBagLayout layout = new GridBagLayout();
	Panel entry_panel = new Panel(layout);
	GridBagConstraints constraints = new GridBagConstraints();

	constraints.fill = GridBagConstraints.BOTH;
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.gridwidth = 2;
	constraints.gridheight = 1;
	constraints.weightx = 0.0;
	constraints.weighty = 0.0;
	constraints.anchor = GridBagConstraints.CENTER;
	Label label = new Label("Please insert the name of the new room:");
	label.setAlignment(Label.CENTER);
	entry_panel.add(label);
	layout.setConstraints(label, constraints);


	constraints.insets = new Insets(3, 3, 3, 3);
	constraints.gridwidth = 1;
	constraints.gridy = 1;
	label = new Label("Room Name:");
	entry_panel.add(label);
	layout.setConstraints(label, constraints);
	
	constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.gridx = 1;
	constraints.gridy = 1;
	constraints.weightx = 1.0;
	constraints.anchor = GridBagConstraints.CENTER;
	name_box = new TextField("", 40);
	entry_panel.add(name_box);
	layout.setConstraints(name_box, constraints);
	
	return entry_panel;
    }

    /** 
     * Represents the Rooms's eternal identity. Used by the Company as the key
     * that uniquely identifies a specific room.
     */
    private class Id implements Identity
    {
	public Identifiable item()
	{
	    return Room.this;
	}

	/**
	 * Suitable for displaying in an UI that needs a short version of the
	 * name.
	 */
	public String toString()
	{
	    return name;
	}

	/**
	 * Is this room the same as another?
	 */
	public boolean equals(Object other)
	{
	    if (!(other instanceof Id))
		return false;

	    Id id = (Id) other;
	    Room room = (Room) (id.item());
	    return room.name.equals(name);
	}

	/**
	 * A slightly better hash function than the one Java uses by default.
	 */
	public int hashCode()
	{
	    int hash_value = 0;
	    
	    for (int i = name.length(); --i > 0; )
	    {
		hash_value ^= name.charAt(i);
		if (i % 3 == 0)
		    hash_value <<= 1;
	    }
	    return hash_value;
	}

	/**
	 * Is this room lexigraphically less than, equal to, or greater than another?
	 */
	public int compare(Comparable other) throws Comparable.Not_comparable
	{
	    if (!(other instanceof Id))
		throw new Comparable.Not_comparable();

	    Id id = (Id) other;
	    Room room = (Room) (id.item());
	    return room.name.compareTo(name);
	}
    }
	
    /**
     * A token for this room.
     */
    public Identity key()
    {
	return new Id();
    }

    /**
     * Validate the data and return a token for this room.
     */    
    public Identity confirm_ui()
    {
	Identity key;

	if (name_box.getText().equals(""))
	    key = null;
	else
	{
	    name = name_box.getText();
	    key = new Id();
	}
	return key;
    }

    /**
     * Is this room available at this time?
     */
    public boolean confirms_time_slot(Time_Slot slot)
    {
	return true;
    }

    /**
     * Somebody cancelled this meeting. Remove the slot from this list.
     */
    public void cancels_time_slot(Time_Slot slot)
    {
    }
}

