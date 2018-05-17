import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * model to hold all the events. It loads and saves them using serialization.
 */
public class Model implements Serializable {
	private String selectedDate;
	private ArrayList<EventView> allEvents;
	
	/**
	 * Model constructor
	 */
	public Model(){
		allEvents = new ArrayList<EventView>();
	}

	/**
	 * sets the date of the day selected
	 * @param s date selected
	 */
	public void setSelectedDate(String s) {
		selectedDate = s;
	}
	
	/**
	 * returns the date of the day selected
	 * @return date of selected day
	 */
	public String getSelectedDate() {
		return selectedDate;
	}
	
	/**
	 * add new event to array list
	 * @param newEvent new event created by the user
	 */
	public void setNewEvent(EventView newEvent) {
			allEvents.add(newEvent);
	}
	
	/**
	 * returns all the events on a specific date
	 * @param date date of the day selected
	 * @return onDay the array of all the events happening on the selected date
	 */
	public ArrayList<EventView> getEventsOnCertainDay(String date) {
		ArrayList<EventView> onDay = new ArrayList<>();
		
		if(allEvents.isEmpty() == false) {
			for(EventView e: allEvents) {
				if(e.getDate().equals(date)) {
					onDay.add(e);
				}
			}
		}
		
		return onDay;
	}
	
	/**
	 * return all the events
	 * @return allEvents all the events
	 */
	 public ArrayList<EventView> getAllEvents() {
		 return allEvents;
	 }
	 
	 /**
	  * Saves the events in a file using serialization when the user presses quit
	  * @throws FileNotFoundException
	  * @throws IOException
	  */
	 public void save() throws FileNotFoundException, IOException {
		 ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("events.dat"));
		 out.writeObject(allEvents);
		 out.close();
	 }
	 
	 /**
	  * loads all the events from the file (if it exists) and adds them to the array
	  * so that the view day displays them
	  * @throws ClassNotFoundException
	  * @throws IOException
	  */
	 public void load() throws ClassNotFoundException, IOException {
		 File f = new File("events.dat");
			
		if(f.exists() && !f.isDirectory()) { 
			
		 ObjectInputStream in = new ObjectInputStream(new FileInputStream("events.dat"));
		 allEvents = (ArrayList<EventView>) in.readObject();
		 in.close();
		}
	 }
}
