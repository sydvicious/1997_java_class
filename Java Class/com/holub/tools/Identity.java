package com.holub.tools;

import java.io.*;
import com.holub.tools.Comparable;

public interface Identity extends Serializable, Comparable
{
    /** Return a reference to the object that is associated with
     *  this identity (typically the creating object).
     */
    Identifiable item();

    /** Returns a represenetation suitable for display in a list box,
     *  choice, pick-list, ir equivalent.
     */
    String toString();

    boolean equals(Object other);

    int hashCode();

    int compare(Comparable other) throws Comparable.Not_comparable;
}
