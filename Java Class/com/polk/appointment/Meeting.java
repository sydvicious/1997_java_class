package com.polk.appointment;

/**
 * A Meeting must get a list of participants as well as a room. This class
 * has not been implemented.
 */
class Meeting extends Time_Slot implements Serializable
{
    private String agenda;
    private Employee originator;
    private Vector participants;
    private Room room;
	
    public Meeting(Employee originator) throws Exception
    {
	this.originator = originator;
	
	slot = new Time_Slot();
	Company company = Company.get_instance();
		
	room = company.input_room(slot);
	if (room != null) {
	    participants = company.input_employee_list(originator, slot);
	    if (participants == null) {
		company.cancel_room_schedule(room, slot);
		room = null;
	    }
	}
		
	if (room == null) {
	    throw new Exception("User cancelled action");
	}
    }
	
    public void cancel_slot(Employee originator)
    {
	if (!this.originator.equals(originator))
	{
	    // Interface to complain to user
	}
	
	Company company = Company.get_instance();
	company.cancel_room_schedule(room, slot);
	company.cancel_schedule(participants, slot);
    }
}

