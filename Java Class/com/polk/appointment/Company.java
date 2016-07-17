package com.polk.appointment;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import com.polk.utils.*;
import com.holub.tools.*;

/**
 *	Company <<singleton>> - this class does the bookeeping of keeping track of employees
 *		and rooms.
 */
public class Company implements Serializable
{
    private static final String APPOINTMENT_FILE = "Appointment.ser";
    private static Company instance = null;
    private static String appointment_file = null;
	
    private Hashtable employee_list = new Hashtable();
    private Hashtable room_list = new Hashtable();
    private Admin_Dialog admin_dialog = null;
    private String name_entered = null;

    /**
     *	The constructor is private. Use get_instance() to get the one Company object.
     */	
    private Company()
    {
    }
	
    /**
     * Use this to get the company object to request or cancel a meeting.
     */
    public static final Company get_instance()
    {
	if (appointment_file == null)
	    appointment_file = APPOINTMENT_FILE;

	if (instance == null) 
	{
	    try 
	    {
		FileInputStream input = new FileInputStream(appointment_file);
		ObjectInputStream objects = new ObjectInputStream(input);
		try 
		{
		    instance = (Company) objects.readObject();
		}
		catch (Exception e) {
		    System.err.println("The file format has changed.\nAll employees and rooms will have to be re-entered.");
		    File file = new File(appointment_file);
		    file.delete();
		    objects.close();
		    throw e;
		}
		objects.close();
	    }
	    catch (Exception e)
	    {
		instance = new Company();
	    }
	}
	return instance;
    }

    /**
     * For the given time slot, get a list of employees to attend the meeting.
     * Returns null if the user cancels the request.
     */
    public Vector input_employee_list(Schedulable originator, Time_Slot slot)
    {
	return null;
    }
	
    /**
     * Return a room that is free for the given time slot. Returns null if the
     * user cancels.
     */
    public Schedulable input_room(Time_Slot slot)
    {
	return null;
    }

    /**
     * Cancels the time slot from each employee.
     */
    public void cancel_schedule(Vector employees, Time_Slot slot)
    {
    }
	
    /**
     * Cancels the time slot from the room.
     */
    public void cancel_room_schedule(Schedulable room, Time_Slot slot)
    {
    }


    /**
     * Finds the employee indicated by the name key.
     */

    public Employee find_employee(Identity id)
    {
	return (Employee) employee_list.get(id);
    }

    /*
     * The following classes are user interface elements.
     /

    /**
     * A dialog to get a new employee.
     */

    private class Member_Entry_Dialog extends General_Dialog
    {
	Schedulable attendee = null;
	Hashtable list = null;

	public Member_Entry_Dialog(Hashtable list, Schedulable attendee)
	{
	    super(attendee.proxy(), "Appointment");

	    this.attendee = attendee;
	    this.list = list;
	    super.show_dialog();
	}

	public void handle_ok()
	{
	    Identity key = attendee.confirm_ui();
	    if (key == null) {
		Error_Alert error_alert =
			new Error_Alert("You must enter a valid name.");
	    } else if (list.containsKey(key)) {
		StringBuffer buf = new StringBuffer("\"");
		buf.append(key.toString());
		buf.append("\" is not unique.");
		Error_Alert error_alert =
			new Error_Alert(buf.toString());
	    } else {
		list.put(key, attendee);
		dispose();
	    }
	}
    }

    /**
     * Display all employees or rooms.
     */
    private class Member_Display_Dialog extends Frame
    {
	public Member_Display_Dialog(Hashtable list) throws Exception
	{
	    super("Appointment");
	    
	    // Create the text to display based on the keys associated with
	    // the given objects.

	    StringBuffer buffer = new StringBuffer();
	    Comparable[] elements = null;
	    
	    try
	    {
		elements = Hashtable_Utils.get_sorted_elements(list,
			Sort.ASCENDING);
	    }
	    catch (Comparable.Not_comparable e)
	    {
		throw e;
	    }

	    for (int i = 0 ; i < elements.length; i++)
	    {
		buffer.append(elements[i].toString());
		buffer.append("\n");
	    }

	    GridBagLayout layout = new GridBagLayout();
	    this.setLayout(layout);
	    GridBagConstraints constraints = new GridBagConstraints();

	    constraints.fill = GridBagConstraints.BOTH;
	    constraints.gridx = 0;
	    constraints.gridy = 0;
	    constraints.gridwidth = 1;
	    constraints.gridheight = 1;
	    constraints.weightx = 1.0;
	    constraints.weighty = 1.0;
	    constraints.anchor = GridBagConstraints.CENTER;
	    constraints.insets = new Insets(3, 3, 3, 3);
	    TextArea text_area = new TextArea(buffer.toString(),
		    10, 40, TextArea.SCROLLBARS_VERTICAL_ONLY);
	    text_area.setEditable(false);
	    this.add(text_area);
	    layout.setConstraints(text_area, constraints);

	    Button button = new Button("OK");
	    button.addActionListener
	    (
		new ActionListener()
		{
		    public void actionPerformed(ActionEvent event)
		    {
			dispose();
		    }
		}
	    );
	    constraints.gridy = 1;
	    constraints.fill = GridBagConstraints.NONE;
	    this.add(button);
	    layout.setConstraints(button, constraints);

	    this.addWindowListener
	    (
		new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
			dispose();
		    }
		}
	    );

	    this.pack();
	    Dialog_Utils.center(this);
	    this.show();
	}
    }

    /**
     * An interface to delete rooms or employees.
     */
    private class Member_Delete_Dialog extends Frame
    {
	Hashtable list = null;
	Choice choice = null;
	Comparable[] elements = null;

	public Member_Delete_Dialog(Hashtable list) throws Exception
	{
	    super("Appointment");

	    this.list = list;	    

	    GridBagLayout layout = new GridBagLayout();
	    this.setLayout(layout);
	    GridBagConstraints constraints = new GridBagConstraints();

	    constraints.fill = GridBagConstraints.BOTH;
	    constraints.gridx = 0;
	    constraints.gridy = 0;
	    constraints.gridwidth = 2;
	    constraints.gridheight = 1;
	    constraints.weightx = 0.0;
	    constraints.weighty = 0.0;
	    constraints.insets = new Insets(10, 10, 3, 10);
	    constraints.anchor = GridBagConstraints.CENTER;
	    choice = new Choice();
	    
	    try
	    {
		elements = Hashtable_Utils.get_sorted_elements(list,
			Sort.ASCENDING);
	    }
	    catch (Comparable.Not_comparable e)
	    {
		throw e;
	    }

	    for (int i = 0; i < elements.length; i++)
	    {
		choice.add(elements[i].toString());
	    }
	    this.add(choice);
	    layout.setConstraints(choice, constraints);

	    Panel panel = new Panel();
	    Button button = new Button("Cancel");
	    button.addActionListener
	    (
		new ActionListener()
		{
		    public void actionPerformed(ActionEvent event)
		    {
			dispose();
		    }
		}
	    );
	    panel.add(button);
	    
	    button = new Button("OK");
	    button.addActionListener
	    (
		new ActionListener()
		{
		    public void actionPerformed(ActionEvent event)
		    {
			Member_Delete_Dialog.this.list.remove(
				Member_Delete_Dialog.this.elements[
    				Member_Delete_Dialog.this.choice.getSelectedIndex()]);
			dispose();
		    }
		}
	    );
	    panel.add(button);

	    constraints.gridy = 1;
	    constraints.insets = new Insets(3, 3, 3, 3);
	    
	    this.add(panel);
	    layout.setConstraints(panel, constraints);
			
	    this.addWindowListener
	    (
		new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
			dispose();
		    }
		}
	    );

	    this.pack();
	    Dialog_Utils.center(this);
	    this.show();
	}
    }

    /**
     * Save off the data for the company.
     */
    public void update_master_file() throws Exception
    {
	try
	{
	    FileOutputStream file_out = new FileOutputStream(appointment_file);
	    ObjectOutputStream object_out = new ObjectOutputStream(file_out);
	    object_out.writeObject(Company.this);
	    object_out.flush();
	    object_out.close();
	}
	catch (Exception exception)
	{
	    throw exception;
	}
    }


    /**
     * Used to present the administrator with his options.
     */
    private class Admin_Dialog extends Frame
    {
	public Admin_Dialog ()
	{
	    super("Appointment Administrator");

	    this.setLayout(new GridLayout(4, 2));
	    
	    Button button = new Button("Add an employee");
	    button.addActionListener
	    (
		new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
			Member_Entry_Dialog entry_dialog = 
				new Member_Entry_Dialog(employee_list, new Employee());
		    }
		}
	    );
	    this.add(button);
	    
	    button = new Button("Delete an employee");
	    button.addActionListener
	    (
		new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
			try
			{
			    Member_Delete_Dialog dialog =
				    new Member_Delete_Dialog(employee_list);
			}
			catch (Exception exception)
			{
			    Error_Alert dialog = new Error_Alert(exception.toString());
			}  
		    }
		}
	    );
	    this.add(button);
	    
	    button = new Button("Add a room");
	    button.addActionListener
	    (
		new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
			Member_Entry_Dialog entry_dialog =
				new Member_Entry_Dialog(room_list, 
				(Schedulable) new Room());
		    }
		}
	    );
	    this.add(button);
	    
	    button = new Button("Delete a room");
	    button.addActionListener
	    (
		new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
			try
			{
			    Member_Delete_Dialog dialog =
				    new Member_Delete_Dialog(room_list);
			}
			catch (Exception exception)
			{
			    Error_Alert dialog = new Error_Alert(exception.toString());
			}  
		    }
		}
	    );
	    this.add(button);
	    
	    button = new Button("Display list of employees");
	    button.addActionListener
	    (
		new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
			try
			{
			    Member_Display_Dialog dialog =
				    new Member_Display_Dialog(employee_list);
			}
			catch (Exception exception)
			{
			    Error_Alert dialog = new Error_Alert(exception.toString());
			}  
		    }
		}
	    );
	    this.add(button);
	    
	    button = new Button("Display list of rooms");
	    button.addActionListener
	    (
		new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
			try
			{
			    Member_Display_Dialog dialog =
				    new Member_Display_Dialog(room_list);
			}
			catch (Exception exception)
			{
			}
		    }
		}
	    );
	    this.add(button);
	    
	    button = new Button("Quit");
	    button.addActionListener
	    (
		new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
			try
			{
			    Company.this.update_master_file();
			}
			catch (Exception exception)
			{
			    exception.printStackTrace();
			}
			System.exit(0);
		    }
		}
	    );
	    this.add(button);
	    
	    button = new Button("Quit without saving");
	    button.addActionListener
	    (
		new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
			System.exit(0);
		    }
		}
	    );
	    this.add(button);

	    this.addWindowListener
	    (
		new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
			System.exit(0);
		    }
		}
	    );

	    this.pack();
	    Dialog_Utils.center(this);
	    this.show();
	}
    }

    private void run_admin_dialog()
    {
	admin_dialog = new Admin_Dialog();
    }

    /**
     * Used by the company administrator. Used to add and delete rooms. Also used to create
     * a new company file. A person who wants to schedule a meeting should run Employee instead.
     */
    public static void main(String args[])
    {
	appointment_file = (args.length == 0) ? APPOINTMENT_FILE : args[0];
	Company company = null;
	try
	{
	    company = Company.get_instance();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    System.exit(-1);
	}

	company.run_admin_dialog();
    }
}

