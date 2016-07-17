package com.polk.appointment;

import com.holub.tools.Comparable;
import com.holub.tools.Identity;
import java.io.Serializable;

/**
 * Used to be able to tell when two time slots overlap with each other from
 * outside of the Time_Slot.
 */

interface Time_Identity extends Identity, Serializable
{
    /**
     * Two time slots overlap if the start time or end time of one is between
     * the start and end times of the other.
     */
    boolean overlaps(Time_Identity other);
}
