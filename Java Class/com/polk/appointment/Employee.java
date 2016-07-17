package com.polk.appointment;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.text.*;
import com.holub.tools.*;
import com.polk.utils.*;

/**
 * The person doing the scheduling. Also, can be a schedulee.
 */
public class Employee implements Serializable, Schedulable, Identifiable,
	Component_Interface
{
    private String first_name = null;
    private String last_name = null;
    private TextField first_name_box = null;
    private TextField last_name_box = null;
    private Calendar calendar = new Calendar();

    /**
     * Used to keep track of the employee elsewhere.
     * @see Identity
     */
    public Identity key()
    {
	return new Id();
    }
    
    /**
     * This routine tells the object to get all of the information from the ui
     * elements it gave the controller, verify it, and return an Identity.
     * Returns null if there is invalid data.
     * @see ComponentInterface
     */
    public Identity confirm_ui()
    {
	Identity key;

	if ((last_name_box.getText().equals("")) || (first_name_box.getText().equals("")))
	{
	    key = null;
	}
	else 
	{
	    last_name = last_name_box.getText();
	    first_name = first_name_box.getText();

	    key = new Id();
	}
	return key;
    }

    /**
     * Displays the employee's name and schedule.
     */
    public String toString()
    {
    	StringBuffer buf = new StringBuffer(first_name);
	buf.append(" ");
	buf.append(last_name);
	buf.append(" - ");
	buf.append(DateFormat.getDateInstance().format(new Date()));
	buf.append("\n");
	buf.append(calendar.toString());
	return buf.toString();
    }

    /**
     * Given a time slot, is the employee free?
     */
    public boolean confirms_time_slot(Time_Slot slot)
    {
	return true;
    }

    /**
     * Remove this time slot from the employee's list.
     */
    public void cancels_time_slot(Time_Slot slot)
    {
    }

    /*
     * Creating a new time_slot with an interface.
     */
    private class Time_Slot_Entry_Dialog extends General_Dialog
    {
	Time_Slot slot = null;

	public Time_Slot_Entry_Dialog(Time_Slot slot)
	{
	    super(slot.proxy(), "Schedule Activity");
	    this.slot = slot;
	    super.show_dialog();
	}

	public void handle_ok()
	{
	    Identity key = Time_Slot_Entry_Dialog.this.slot.confirm_ui();
	    if (Employee.this.calendar.confirm_slot(key))
	    {
		Employee.this.calendar.add_slot(key);
		dispose();
	    }
	    else
	    {
		Error_Alert alert = 
			new Error_Alert("This date conflicts with an existing meeting/activity.");
	    }
	}
    }

    /*
     * Chooses a time slot to cancel and cancels it.
     */
    
    private class Time_Slot_Delete_Dialog extends General_Dialog
    {
	public Time_Slot_Delete_Dialog(String title)
	{
	    super(Employee.this.calendar.proxy(), title);
	    super.show_dialog();
	}

	public void handle_ok()
	{
	    Identity key = Employee.this.calendar.confirm_ui();
	    System.out.println(key);
	    Employee.this.calendar.remove_slot(key);
	    dispose();
	}
    }	 

    /**
     * An employee can create a new activity, which is just a block of time in which
     * he is not available for other activities or meetings.
     */
    private void new_activity()
    {
	Activity activity = new Activity();
	Time_Slot_Entry_Dialog dialog = new Time_Slot_Entry_Dialog(activity);
    }

    /**
     * Cancel an existing activity.
     */
    private void cancel_activity()
    {
	Time_Slot_Delete_Dialog dialog = new Time_Slot_Delete_Dialog("Cancel activity");
    }
	
    /**
     * An employee can create a new meeting, which will ask the user for a list of
     * attendees.
     **/
    public void new_meeting()
    {
    }

    /**
     * A user interface component to add to a dialog. Has components to enter
     * relevant data. In this case, first and last name.
     *
     * @see Component_Interface
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
	Label label = new Label("Please enter first and last name:");
	label.setAlignment(Label.CENTER);
	entry_panel.add(label);
	layout.setConstraints(label, constraints);

	constraints.gridy = 1;
	constraints.gridwidth = 1;
	constraints.anchor = GridBagConstraints.EAST;
	constraints.insets = new Insets(3, 3, 3, 3);
	label = new Label("First Name:");
	entry_panel.add(label);
	label.setAlignment(Label.RIGHT);
	layout.setConstraints(label, constraints);
	
	constraints.gridy = 2;
	label = new Label("Last Name:");
	entry_panel.add(label);
	label.setAlignment(Label.RIGHT);
	layout.setConstraints(label, constraints);

	constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.gridx = 1;
	constraints.gridy = 1;
	constraints.weightx = 1.0;
	constraints.anchor = GridBagConstraints.CENTER;
	first_name_box = new TextField("", 40);
	entry_panel.add(first_name_box);
	layout.setConstraints(first_name_box, constraints);
	
	constraints.gridy = 2;
	last_name_box = new TextField("", 40);
	entry_panel.add(last_name_box);
	layout.setConstraints(last_name_box, constraints);
	return entry_panel;
    }

    /** 
     * Represents the Employee's eternal identity. Used by the Company as the key
     * that uniquely identifies a specific attendee.
     */
    private class Id implements Identity
    {

	/**
	 * Get the actual employee from this id.
	 */
	public Identifiable item()
	{
	    return Employee.this;
	}

	/**
	 * Suitable for displaying in an UI that needs a short version of the
	 * name.
	 */
	public String toString()
	{
	    StringBuffer buf = new StringBuffer(first_name);
	    buf.append(" ");
	    buf.append(last_name);
	    return buf.toString();
	}

	/**
	 * Is this the same employee as another one?
	 */
	public boolean equals(Object other)
	{
	    if (!(other instanceof Id))
		return false;

	    Id id = (Id) other;
	    Employee employee = (Employee) (id.item());
	    return ((employee.first_name.equals(first_name)) 
		    && (employee.last_name.equals(last_name)));
	}

	/**
	 * A better hashCode. Hashes on both first and last names.
	 */
	public int hashCode()
	{
	    int hash_value = 0;
	    int i;
	    
	    for (i = last_name.length(); --i > 0; )
	    {
		hash_value ^= last_name.charAt(i);
		if (i % 3 == 0)
		    hash_value <<= 1;
	    }
	    for (i = first_name.length(); --i > 0; )
	    {
		hash_value ^= first_name.charAt(i);
		if (i % 3 == 0)
		    hash_value <<= 1;
	    }
	    return hash_value;
	}

	/**
	 * Compare the value of this employee with another.
	 */
	public int compare(Comparable other) throws Comparable.Not_comparable
	{
	    if (!(other instanceof Id))
		throw new Comparable.Not_comparable();

	    Id id = (Id) other;
	    Employee employee = (Employee) (id.item());
	    int result = employee.last_name.compareTo(last_name);
	    if (result == 0)
		result = employee.first_name.compareTo(first_name);
	    return result;
	}
    }

    /**
     * Allows the employee to schedule and cancel activities and meetings.
     * Really should be redone to use the General_Dialog interface.
     */
    private class Appointment_Dialog extends Frame
    {
	public Appointment_Dialog()
	{
	    super("Appointment Calendar");

	    this.setLayout(new BorderLayout());

	    Component splash_panel = 
		    Dialog_Utils.get_multiline_component(Employee.this.toString());
	    this.add(splash_panel, "North");

	    GridBagLayout panel_layout = new GridBagLayout();
	    GridBagConstraints constraints = new GridBagConstraints();
	    Panel panel = new Panel(panel_layout);

	    constraints.fill = GridBagConstraints.BOTH;
	    constraints.gridx = 0;
	    constraints.gridy = 0;
	    constraints.gridwidth = 1;
	    constraints.gridheight = 1;
	    constraints.weightx = 0.0;
	    constraints.weighty = 0.0;
	    constraints.insets = new Insets(2, 2, 2, 2);
	    constraints.anchor = GridBagConstraints.CENTER;

	    Button button = new Button("Schedule an activity");
	    button.addActionListener
	    (
		new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
			Employee.this.new_activity();
		    }
		}
	    );
	    panel.add(button);
	    panel_layout.setConstraints(button, constraints);

	    constraints.gridx = 1;
	    button = new Button("Cancel an activity");
	    button.addActionListener
	    (
		new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
			Employee.this.cancel_activity();
		    }
		}
	    );
	    panel.add(button);
	    panel_layout.setConstraints(button, constraints);

	    constraints.gridx = 0;
	    constraints.gridy = 1;
	    button = new Button("Schedule a meeting");
	    button.addActionListener
	    (
		new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    }
		}
	    );
	    panel.add(button);
	    panel_layout.setConstraints(button, constraints);

	    constraints.gridx = 1;
	    button = new Button("Cancel a meeting");
	    button.addActionListener
	    (
		new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    }
		}
	    );
	    panel.add(button);
	    panel_layout.setConstraints(button, constraints);

	    constraints.gridx = 0;
	    constraints.gridy = 2;
	    button = new Button("Quit");
	    button.addActionListener
	    (
		new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
			try
			{
			    Company.get_instance().update_master_file();
			}
			catch (Exception exception)
			{
			    exception.printStackTrace();
			}
			dispose();
			System.exit(0);
		    }
		}
	    );
	    panel.add(button);
	    panel_layout.setConstraints(button, constraints);

	    constraints.gridx = 1;
	    button = new Button("Logout");
	    button.addActionListener
	    (
		new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
			try
			{
			    Company.get_instance().update_master_file();
			}
			catch (Exception exception)
			{
			    exception.printStackTrace();
			}
			dispose();
			Employee.login();
		    }
		}
	    );
	    panel.add(button);
	    panel_layout.setConstraints(button, constraints);
	
	    this.add(panel, "South");
	    		
	    this.addWindowListener
	    (
		new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
			try
			{
			    Company.get_instance().update_master_file();
			}
			catch (Exception exception)
			{
			    exception.printStackTrace();
			}
			System.exit(0);
		    }
		}
	    );

	    this.pack();
	    Dialog_Utils.center(this);
	    this.show();
	}	
    }

    /**
     * Puts up the appointment dialog so that the employee can actually use this
     * package.
     */

    private void run_appointment_dialog()
    {
	Appointment_Dialog dialog = new Appointment_Dialog();
    }

    /**
     * Ask the user who he is, so we can muck with his schedule.
     */
    private class Login_Dialog extends General_Dialog
    {
	public Login_Dialog()
	{
	    super(Employee.this.proxy(), "Appointment Login");
	    super.show_dialog();
	}

	public void handle_cancel()
	{
	    dispose();
	    System.exit(0);
	}

	public void handle_close()
	{
	    handle_cancel();
	}

	public void handle_ok()
	{
	    Identity key = Employee.this.confirm_ui();
	    if (key == null)
	    {
		Error_Alert error_alert =
			new Error_Alert("Could not log in");
	    } else {
		Employee employee = Company.get_instance().find_employee(key);
		if (employee == null)
		{
		    StringBuffer buf = new StringBuffer(key.toString());
		    buf.append(" is not a valid employee.");
		    Error_Alert error_alert=
			    new Error_Alert(buf.toString());
		}
		else
		{
		    dispose();
		    employee.run_appointment_dialog();
		}
	    }
	}
    }

    /*
     * Actually puts up the login dialog.
     */
    private void do_login()
    {
	Login_Dialog dialog = new Login_Dialog();
    }

    /*
     * Provided so that we do not have to allocate another Employee internally
     * to login again.
     */
    private static void login()
    {
	Employee employee = new Employee();
	employee.do_login();
    }

    /**
     * Main puts up the login dialog, which is the first dialog the user sees.
     * He must input his first and last name.
     * If he is not in the database, the dialog complains, and he can try again.
     * Cancel quits the program.
     */
    public static void main(String args[])
    {
	Employee.login();
    }
}


