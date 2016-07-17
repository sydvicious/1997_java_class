package com.polk.appointment;

import com.holub.tools.Identity;
import com.polk.utils.Component_Interface;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.io.Serializable;

/**
 * An activity is simply a time slot that has a description. This is suitable
 * for things that an employee might do in his/her own with no requrements
 * of other employees.
 */
class Activity extends Time_Slot implements Serializable, Component_Interface
{
    private String description = null;
    private TextArea description_entry = null;

    /**
     * Create a blank activity.
     */
    public Activity()
    {
	super();
    }

    /**
     * Get a readable form of this activity.
     */
    public String toString()
    {
	StringBuffer buf = new StringBuffer(super.toString());
	buf.append(" - ");
	buf.append(description);
	return buf.toString();
    }

    /**
     * Return an interface element for activities.
     */
    public Component proxy()
    {
	Component core = super.proxy();

	BorderLayout outer_layout = new BorderLayout();
	Panel outer_panel = new Panel(outer_layout);
	outer_panel.add(core, "North");

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
	Label label = new Label("Description:");
	label.setAlignment(Label.RIGHT);
	panel.add(label);
	layout.setConstraints(label, constraints);

	constraints.gridy = 1;
	constraints.gridheight = 3;
	constraints.gridwidth = 3;
	constraints.weightx = 1.0;
	constraints.weighty = 1.0;
	constraints.anchor = GridBagConstraints.CENTER;
	description_entry = new TextArea("", 3, 40, TextArea.SCROLLBARS_NONE);
	panel.add(description_entry);
	layout.setConstraints(description_entry, constraints);

	outer_panel.add(panel, "South");

	return outer_panel;
    }

    /**
     * Verify data in the interface elements. Return an id which is a token
     * for this activity. Returns null of there was a data validation error.
     */
    public Identity confirm_ui()
    {
	Identity key = super.confirm_ui();

	if (key != null)
	{
	    description = description_entry.getText();
	}

	return key;
    }
}
