package com.polk.utils;

import com.holub.tools.Identity;
import java.awt.Component;

/**
 * When a class extends this interface, it is required to suppy a component with
 * interface elements that apply to itself. Then it will be given a chance to
 * poll the component, storing off any data and doing data validation.
 */

public interface Component_Interface
{
    /**
     * Gets a proxy to the user interface for this object.
     */
    public Component proxy();

    /**
     * The ui is complete, now poll ui to fill in object specific data.
     * Return a key to the object, suitable for hashing. Returns
     * null if there is data validation error.
     */
    public Identity confirm_ui();
}
