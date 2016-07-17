package com.polk.appointment;

import com.holub.tools.Comparable;
import com.holub.tools.Identifiable;
import com.holub.tools.Identity;
import com.polk.utils.Component_Interface;
import com.polk.utils.Error_Alert;
import java.awt.Choice;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * A Time Slot is a piece of a schedule. Since it extends date, it can be compared
 * like one. It also has a duration, in minutes, and two time slots can be
 * checked to see if they overlap.
 */
class Time_Slot extends Date implements Serializable, Identifiable, Component_Interface
{
    private int duration;		    // In minutes
    private TextField day_entry = null;	    // These should be localized of course.
    private TextField month_entry = null;   // They won't be in DD/MM/YY everywhere.
    private TextField year_entry = null;    // But for now...
    private TextField hour_entry = null;
    private TextField minute_entry = null;
    private Choice am_pm_choice = null;
    private TextField duration_entry = null;
    private static DateFormat df = DateFormat.getInstance();
	
    /**
     * Creates a new blank time slot.
     */
    public Time_Slot()
    {
	super();
	duration = 0;
    }
   
   /**
    * Not implemented. Called by an interator when a meeting is cancelled. All
    * of the attendees must be notified.
    */
    public void cancel_slot(Employee originator)
    {
    }

    /**
     * Retrieves midnight of today.
     */
    static Identity get_today()
    {
	// Need to get midnight of today's date.
	Time_Slot today = new Time_Slot();
	StringBuffer buf = new StringBuffer();
	buf = df.format(today, buf, new FieldPosition(DateFormat.DATE_FIELD));
	try
	{
	    today = (Time_Slot) df.parse(buf.toString());
	}
	catch (Exception e)
	{
	}
	today.duration = 0;
	return today.key();
    }

    /**
     * Returns an interface element that represents this time slot.
     */
    public Component proxy()
    {
	GridBagLayout layout = new GridBagLayout();
	Panel panel = new Panel(layout);
	GridBagConstraints constraints = new GridBagConstraints();

	constraints.fill = GridBagConstraints.BOTH;
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.gridwidth = 1;
	constraints.gridheight = 1;
	constraints.weightx = 0.0;
	constraints.weighty = 0.0;
	constraints.anchor = GridBagConstraints.EAST;
	Label label = new Label("Date:");
	label.setAlignment(Label.RIGHT);
	panel.add(label);
	layout.setConstraints(label, constraints);

	constraints.gridy = 1;
	label = new Label("Time:");
	label.setAlignment(Label.RIGHT);
	panel.add(label);
	layout.setConstraints(label, constraints);

	constraints.gridy = 2;
	label = new Label("Duration:");
	label.setAlignment(Label.RIGHT);
	panel.add(label);
	layout.setConstraints(label, constraints);

	FlowLayout flow_layout = new FlowLayout();
	Panel sub_panel = new Panel(flow_layout);
	flow_layout.setAlignment(FlowLayout.LEFT);
	day_entry = new TextField("", 2);
	sub_panel.add(day_entry);

	label = new Label("/");
	sub_panel.add(label);
	
	month_entry = new TextField("", 2);
	sub_panel.add(month_entry);
    
	label = new Label("/");
	sub_panel.add(label);
    
	year_entry = new TextField("", 2);
	sub_panel.add(year_entry);

	constraints.gridx = 1;
	constraints.gridy = 0;
	constraints.fill = GridBagConstraints.NONE;
	constraints.anchor = GridBagConstraints.WEST;
	panel.add(sub_panel);
	layout.setConstraints(sub_panel, constraints);

	flow_layout = new FlowLayout();
	sub_panel = new Panel(flow_layout);
	flow_layout.setAlignment(FlowLayout.LEFT);

	hour_entry = new TextField("", 2);
	sub_panel.add(hour_entry);

	label = new Label(":");
	sub_panel.add(label);

	minute_entry = new TextField("", 2);
	sub_panel.add(minute_entry);

	am_pm_choice = new Choice();
	am_pm_choice.add("AM");
	am_pm_choice.add("PM");
	sub_panel.add(am_pm_choice);

	constraints.gridy = 1;
	panel.add(sub_panel);
	layout.setConstraints(sub_panel, constraints);

	flow_layout = new FlowLayout();
	sub_panel = new Panel(flow_layout);
	flow_layout.setAlignment(FlowLayout.LEFT);

	duration_entry = new TextField("", 3);
	sub_panel.add(duration_entry);

	label = new Label("minutes");
	sub_panel.add(label);

	constraints.gridy = 2;
	panel.add(sub_panel);
	layout.setConstraints(sub_panel, constraints);

	return panel;
    }

    /**
     * Asks the time slot to verify the data input by the user and to return
     * an id to itself.
     */
    public Identity confirm_ui()
    {
	Identity key = null;

	StringBuffer buf = new StringBuffer(day_entry.getText());
	buf.append("/");
	buf.append(month_entry.getText());
	buf.append("/");
	buf.append(year_entry.getText());
	buf.append(" ");
	buf.append(hour_entry.getText());
	buf.append(":");
	buf.append(minute_entry.getText());
	buf.append(" ");
	buf.append(am_pm_choice.getSelectedItem());
	DateFormat df = DateFormat.getInstance();
	Date new_date = null;
	try
	{
	    new_date = df.parse(buf.toString());
	}
	catch (ParseException e)
	{
	    Error_Alert alert = new Error_Alert("A valid date and time was not entered.");
	}
    
	if (new_date != null) {
	    NumberFormat nf = NumberFormat.getInstance();
	    nf.setParseIntegerOnly(true);
	    try
	    {
		duration = nf.parse(duration_entry.getText()).intValue();
	    }
	    catch (ParseException e)
	    {
		Error_Alert alert = new Error_Alert("A valid duration was not entered.");
	    }
	    this.setTime(new_date.getTime());
	    key = new Id();
	}

	return key;
    }

    /** 
     * Represents the Time_Slot's eternal identity. Used by the Company as the key
     * that uniquely identifies a specific attendee.
     */
    class Id implements Time_Identity
    {
	/**
	 * Returns this identity.
	 */
	public Identifiable item()
	{
	    return Time_Slot.this;
	}

	/**
	 * Suitable for displaying in an UI that needs a short version of the
	 * name.
	 */
	public String toString()
	{
	    StringBuffer buf = new StringBuffer(Time_Slot.this.toString());
	    return buf.toString();
	}

	/**
	 * Compare the identity to another for equality.
	 */
	public boolean equals(Object other)
	{
	    if (!(other instanceof Id))
		return false;

	    Id id = (Id) other;
	    Time_Slot slot = (Time_Slot) (id.item());
	    return (this.equals(other) && (slot.duration == duration));
	}

	/**
	 * Slots are sorted using the start date (which has the time)
	 */

	public int compare(Comparable other) throws Comparable.Not_comparable
	{
	    if (!(other instanceof Id))
		throw new Comparable.Not_comparable();

	    Id id = (Id) other;
	    Time_Slot slot = (Time_Slot) (id.item());
	    int result;

	    if (before(slot))
	    {
		result = -1;
	    } else if (after(slot))
	    {
		result = 1;
	    } else
	    {
		result = 0;
	    }
	    return result;
	}

	/**
	 * Check to see if this identity overlaps another.
	 */
	public boolean overlaps(Time_Identity other)
	{
	    Id id = (Id) other;
	    Time_Slot slot = (Time_Slot) (id.item());

	    Date this_end = new Date(getTime() + duration * 60 * 1000);
	    Date other_end = new Date(slot.getTime() + slot.duration * 60 * 1000);

	    if (before(slot) && this_end.before(slot))
		return false;
	    if (after(other_end) && this_end.after(other_end))
		return false;
	    if (after(slot) && after(other_end))
		return false;
	    if (before(slot) && before(other_end))
		return false;
	    return true;
	}
    }

    /**
     * Get an identity to represent this time slot.
     */
    public Identity key()
    {
	return new Id();
    }

    /**
     * Get a string rep for this time slot.
     */
    public String toString()
    {
	StringBuffer buf = new StringBuffer(super.toString());
	buf.append(" - ");
	Date end = new Date(getTime() + duration * 60 * 1000);
	buf.append(end.toString());
	return buf.toString();
    }
}
