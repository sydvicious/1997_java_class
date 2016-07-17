package com.polk.utils;

import java.awt.*;
import java.awt.event.*;

/**
 * A simple one-line error alert used for reporting simple errors.
 */
public class Error_Alert extends Frame
{
    public Error_Alert(String message)
    {
	super("Error");

	GridBagLayout layout = new GridBagLayout();
	this.setLayout(layout);
	GridBagConstraints constraints = new GridBagConstraints();

	constraints.fill = GridBagConstraints.BOTH;
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.gridwidth = 1;
	constraints.gridheight = 1;
	constraints.weightx = 0.0;
	constraints.weighty = 0.0;
	constraints.anchor = GridBagConstraints.CENTER;
    
	Label label = new Label(message);
	label.setAlignment(Label.CENTER);
	this.add(label);
	layout.setConstraints(label, constraints);

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
	this.add(button);
	constraints.gridy = 1;
	constraints.fill = GridBagConstraints.NONE;
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