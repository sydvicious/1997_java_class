package com.polk.utils;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Frame;
import java.awt.Panel;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Puts up a dialog with the component in the top half of the dialog
 * and a set of buttons in the bottom half. There are four standard
 * buttons that can be added, Yes, No, Cancel, OK. The constants
 * for them should be orred together, i.e. GeneralDialog.OK | GeneralDialog.CANCEL.
 * The caller should call show_dialog() after he or she is done
 * adding buttons.
 * The predefined buttons and the close box both call Frame.dispose().
 * There are methods to override to customize the behavior. Any
 * buttons created by the caller will have no action listeners defined.
 */

public abstract class General_Dialog extends Frame
{
    public static final int OK = 1;
    public static final int CANCEL = 2;
    public static final int YES = 4;
    public static final int NO = 8;
    private Vector user_buttons = null;	// User added buttons
    private Button yes_button = null;
    private Button no_button = null;
    private Button cancel_button = null;
    private Button ok_button = null;
    private Panel button_panel = null;

    /*
     * Creates a dialog with the specified panel, title and buttons.
     */ 
    public General_Dialog(Component panel, String title, int buttons)
    {
	super(title);
	fill_out_dialog(panel, buttons);
    }

    /*
     * An OK|Cancel version of the dialog.
     */
    public General_Dialog(Component panel, String title)
    {
	super(title);
	fill_out_dialog(panel, OK | CANCEL);
    }

    private void fill_out_dialog(Component panel, int buttons)
    {
	this.setLayout(new BorderLayout());
	this.add(panel, "North");

	button_panel = new Panel();
	user_buttons = new Vector(0);

	if ((buttons & YES) != 0)
	{
	    yes_button = new Button("Yes");
	    yes_button.addActionListener
	    (
		new ActionListener()
		{
		    public void actionPerformed(ActionEvent event)
		    {
			General_Dialog.this.handle_yes();
		    }
		}
	    );
	    button_panel.add(yes_button);
	}

	if ((buttons & NO) != 0)
	{
	    no_button = new Button("No");
	    no_button.addActionListener
	    (
		new ActionListener()
		{
		    public void actionPerformed(ActionEvent event)
		    {
			General_Dialog.this.handle_no();
		    }
		}
	    );
	    button_panel.add(no_button);
	}

	if ((buttons & CANCEL) != 0)
	{
	    cancel_button = new Button("Cancel");
	    cancel_button.addActionListener
	    (
		new ActionListener()
		{
		    public void actionPerformed(ActionEvent event)
		    {
			General_Dialog.this.handle_cancel();
		    }
		}
	    );
	    button_panel.add(cancel_button);
	}

	if ((buttons & OK) != 0)
	{
	    ok_button = new Button("OK");
	    ok_button.addActionListener
	    (
		new ActionListener()
		{
		    public void actionPerformed(ActionEvent event)
		    {
			General_Dialog.this.handle_ok();
		    }
		}
	    );
	    button_panel.add(ok_button);
	}

	this.add(button_panel, "South");

	this.addWindowListener
	(
	    new WindowAdapter()
	    {
		public void windowClosing(WindowEvent e)
		{
		    General_Dialog.this.handle_close();
		}
	    }
	);

	this.pack();
	Dialog_Utils.center(this);
	this.show();
    }

    /**
     * Puts a button into the row of buttons at the bottom of this dialog.
     */
    public void add_button(Button button)
    {
	user_buttons.addElement(button);

	button_panel.removeAll();

	Enumeration list = user_buttons.elements();

	while (list.hasMoreElements())
	{
	    button_panel.add((Component) list.nextElement());
	}

	if (yes_button != null)
	{
	    button_panel.add(yes_button);
	}

	if (no_button != null)
	{
	    button_panel.add(no_button);
	}

	if (cancel_button != null)
	{
	    button_panel.add(cancel_button);
	}

	if (cancel_button != null)
	{
	    button_panel.add(ok_button);
	}
    }

    /**
     * Should be called after the constructor is called, and all of the buttons
     * have been added.
     */
    public void show_dialog()
    {
	this.pack();
	Dialog_Utils.center(this);
	this.show();
    }

    /**
     * Default action for the OK button.
     */
    public void handle_ok()
    {
	dispose();
    }

    /**
     * Default action for the Cancel button.
     */
    public void handle_cancel()
    {
	dispose();
    }

    /**
     * Default action when the user chooses to close the dialog.
     */
    public void handle_close()
    {
	dispose();
    }

    /**
     * Default action for the yes button.
     */
    public void handle_yes()
    {
	dispose();
    }

    /**
     * Default action for the no button.
     */
    public void handle_no()
    {
	dispose();
    }
}

