import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Day View to display the events on a certain day
 */
public class DayView extends JPanel {
	
	enum DAY_NAMES {
		Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday;
	}
	private GregorianCalendar myCalendar;
	private DAY_NAMES[] arrayOfDayNames;
	private JPanel[] hours;
	private JPanel[] events;
	private JLabel dayLabel;
	private ArrayList<EventView> arrEv;
	private Model model;
	JPanel listEvents;
	JPanel holdAllHours;
	JPanel holdAllEvents;
	JScrollPane scrollBar;
	
	/**
	 * Day view constructor 
	 * @param m model
	 */
	public DayView(Model m) {
		myCalendar = new GregorianCalendar();
		arrayOfDayNames = DAY_NAMES.values();
		hours = new JPanel[24];
		events= new JPanel[48];
		dayLabel = new JLabel();
		arrEv = new ArrayList<>();
		model = m;
		
		//initialize JPanels for hours 
		JLabel time;
		for(int j = 0; j < hours.length; j++)
		{
			if(j==0)
				time = new JLabel("12am");
			else if(j<12)
				time = new JLabel(Integer.toString(j)+"am");
			else if(j==12)
				time = new JLabel("12pm");
			else
				time = new JLabel(Integer.toString(j-12)+"pm");
			
			hours[j] = new JPanel();
			hours[j].add(time);
			hours[j].setBackground(Color.WHITE);
		}
		
		//Initialize JPanels for events
		for(int k = 0; k < events.length; k++) {
			events[k] = new JPanel();
			events[k].setBackground(new Color(204,204,204));
		}
		
		//JPanels to hold the hours and events JPanels and make the day view look nice
		listEvents = new JPanel(new BorderLayout());
		holdAllHours = new JPanel(new GridLayout(0,1));
		holdAllEvents = new JPanel(new GridLayout(0,1));
		scrollBar = new JScrollPane();
		
		displayView();
	}
	
	public void displayView() {
		//Label to display the name of day, number of month, and number of day
		dayLabel.setText(arrayOfDayNames[myCalendar.get(Calendar.DAY_OF_WEEK)-1] + " " + (myCalendar.get(Calendar.MONTH)+1) 
				+ "/" + myCalendar.get(Calendar.DAY_OF_MONTH));
		dayLabel.setHorizontalAlignment(JLabel.CENTER);
		
		//add hours panels to holdallhours panel
		for(int j = 0; j < hours.length; j++)
		{
			holdAllHours.add(hours[j]);
			hours[j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}
		
		//add events panels to holdallevents panel
		for(int k = 0; k < events.length; k++) {
			holdAllEvents.add(events[k]);
			events[k].setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}
		
		//add both holdallhours and holdallevents to the listevents panel
		listEvents.add(holdAllHours, BorderLayout.WEST);
		listEvents.add(holdAllEvents, BorderLayout.CENTER);
		
		this.setLayout(new BorderLayout());
		this.add(dayLabel, BorderLayout.NORTH);
		this.setBackground(Color.WHITE);
		
		scrollBar.setViewportView(listEvents);   //Add scroll bar to the speaker text area
		this.add(scrollBar, BorderLayout.CENTER);
		
		//get all events of the day being displayed
		arrEv = model.getEventsOnCertainDay(getDate());

		//get events on the day being displayed
		if(arrEv.isEmpty() == false) 
			addEventToView();
	
	}

	/**
	 * If the previous button is clicked, go back one day on the day view and display its events
	 */
	public void previous()
	{
		myCalendar.add(Calendar.DATE, -1);
		for(JPanel v : events)
		{
			v.removeAll();
		}
		displayView();
	}
	
	/**
	 * If the next button is clicked, go forward one day on the day view and display its events
	 */
	public void next()
	{
		myCalendar.add(Calendar.DATE, 1);
		for(JPanel v : events)
		{
			v.removeAll();
		}
		displayView();
	}
	
	/**
	 * get date of the day being displayed
	 * @return the date
	 */
	private String getDate()
	{
		DateFormat dateFormat = new SimpleDateFormat("M/d/yyyy"); //Date format to save in Day
		Date date = myCalendar.getTime();
		String date1 = dateFormat.format(date);
		
		return date1;
	}
	
	/**
	 * add all events pertaining to the day view showing 
	 */
	private void addEventToView() {
		
		for(EventView event : arrEv) {		
			String s = event.getStartingTime();
			String e = event.getEndingTime();
			
			String shour = s.substring( 0, 2);
			String sminutes = s.substring(3, 5);
			String stype = s.substring( 5, 7);
			
			String ehour = e.substring( 0, 2);
			String eminutes = e.substring(3, 5);
			String etype = e.substring( 5, 7);
			
			int start = Integer.valueOf(shour);
			int end =  Integer.valueOf(ehour);
			
			//if starting time is am
			if(stype.equals("am")) {
				if(shour.equals("12") && sminutes.equals("00")) {
					events[0].add(event);
				}
				else if(shour.equals("12") && sminutes.equals("30")) { 
					events[1].add(event);
				}
				else if(sminutes.equals("00")) {
					events[Integer.valueOf(shour)*2].add(event);
				}
				else if(sminutes.equals("30")) {
					events[(Integer.valueOf(shour)*2)+1].add(event);
				}
			}
			else if(stype.equals("pm"))  //if starting time is pm
			{
				if(shour.equals("12") && sminutes.equals("00")) {
					events[24].add(event);
				}
				else if (shour.equals("12") && sminutes.equals("30")) {
					events[25].add(event);
				}
				else if(sminutes.equals("00")) {
					events[(12+Integer.valueOf(shour))*2].add(event);
				}
				else if(sminutes.equals("30")) {
					events[(12+Integer.valueOf(shour))*2+1].add(event);
				}
			}
	
			EventView placeholder = new EventView("End of event");
			
			if(etype.equals("am")) { //if ending time is am
				if(ehour.equals("12") && eminutes.equals("00"))  
					events[48].add(placeholder);
				else if(ehour.equals("12") && eminutes.equals("30")) 
					events[0].add(placeholder);
				else if(eminutes.equals("00"))
					events[Integer.valueOf(ehour)*2-1].add(placeholder);
				else if(eminutes.equals("30"))
					events[(Integer.valueOf(ehour)*2)].add(placeholder);
			}
			else if(etype.equals("pm"))  //if ending time is pm
			{
				if(ehour.equals("12") && eminutes.equals("00")) 
					events[23].add(placeholder);
				else if (ehour.equals("12") && eminutes.equals("30"))
					events[24].add(placeholder);
				else if(eminutes.equals("00"))
					events[(12+Integer.valueOf(ehour))*2-1].add(placeholder);
				else if(eminutes.equals("30"))
					events[(12+Integer.valueOf(ehour))*2].add(placeholder);
			}
	
		}
		arrEv.clear();  
		validate();
	}
	
}
