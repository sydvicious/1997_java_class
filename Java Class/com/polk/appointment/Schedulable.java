package com.polk.appointment;

import com.holub.tools.Identity;
import com.polk.appointment.Time_Slot;
import com.polk.utils.Component_Interface;
import java.io.Serializable;

/**
 * Things that are attendees can add and delete time slots from their calendars.
 */
interface Schedulable extends Serializable, Component_Interface
{
    /**
     * Returns a token for this object from which hash table keys and interface
     * elements can be displayed.
     */
    public Identity key();

    /**
     * Given a time slot, checks to see if it conflicts with existing time slots.   
     */
    public boolean confirms_time_slot(Time_Slot slot);

    /**
     * Takes a time slot out of the calendar. Not an error if the time slot is not
     * already in the calendar.
     */
    public void cancels_time_slot(Time_Slot slot);

}
