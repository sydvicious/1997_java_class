package com.polk.utils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.util.StringTokenizer;

/**
 * A set of useful utilities for creating dialogs.
 */

public class Dialog_Utils
{
    /**
     * Centers the given frame in the current display.
     */
    static public void center(Frame frame)
    {
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	Dimension screen = toolkit.getScreenSize();
	frame.setLocation((screen.width - frame.getBounds().width) / 2,
		(screen.height - frame.getBounds().height) / 2);
    }

    /**
     * Returns an interface component made of several labels, each of which
     * contains one line of text. This works around the fact that
     * java.awt.Label does not support multi-line text.
     */
    static public Component get_multiline_component(String string)
    {
	StringTokenizer tokenizer = new StringTokenizer(string, "\n");
	int num_lines = tokenizer.countTokens();
	GridLayout grid_layout = new GridLayout(num_lines, 1);
	Panel panel = new Panel(grid_layout);
	while (tokenizer.hasMoreElements())
	{
	    Label label = new Label((String)tokenizer.nextElement());
	    panel.add(label);
	}
	return panel;
    }
}