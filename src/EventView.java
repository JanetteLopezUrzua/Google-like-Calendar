import java.awt.Font;

import javax.swing.JLabel;

/**
 * Event view to show an event as a label
 */
public class EventView  extends JLabel {
	
	private String text;
	private String date;
	private String startingTime;
	private String endingTime;
	
	/**
	 * Constructor with no parameters.
	 */
	public EventView(String t) {
		text = t;
	}

	/**
	 * Constructor with 4 parameters.
	 * @param d date of event
	 * @param st starting time of event
	 * @param et ending time of event
	 * @param t title of event
	 */
	public EventView(String d, String st, String et, String t) {
		date = d;
		text = t;
		startingTime = st;
		endingTime = et;
	}
	
	/**
	 * get date of the event
	 * @return date of event
	 */
	public String getDate() {
		return date;
	}
	
	/**
	 * get text of the event
	 * @return the text of the event
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * get starting time of the event
	 * @return starting time of the event
	 */
	public String getStartingTime() {
		return startingTime;
	}
	
	/**
	 * get ending time of the event
	 * @return ending time of the event
	 */
	public String getEndingTime() {
		return endingTime;
	}

}
