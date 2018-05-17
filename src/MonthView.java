import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class to hold the month view of the calendar. It shows the calendar by month and
 * allows the user to click on the previous and next buttons and it changes between days and months
 * The user can also select a specific day to then create an event on that day. 
 */
public class MonthView extends JPanel {
	enum MONTHS {
		January, February, March, April, May, June, July, August, September, October, November, December;
	}
	enum DAY_NAMES {
		Su, Mo, Tu, We, Th, Fr, Sa;
	}
	
	private GregorianCalendar myCalendar;
	private int today;
	private DAY_NAMES[] arrayOfDayNames;
	private MONTHS[] arrayOfMonths;
	private JPanel[][] daysOrder;
	private JLabel[] dayNames;
	private JPanel dayPanelSelected;
	private int oneSelected = 0;
	private String daySelected;
	private int month;
	private int year;
	private JLabel monthAndYearLabel;
	private int nameOfFirstDayOfMonth;

	/**
	 * Month view constructor
	 */
	public MonthView(){
		myCalendar = new GregorianCalendar();
		arrayOfDayNames = DAY_NAMES.values();
		arrayOfMonths = MONTHS.values();
		daysOrder = new JPanel[6][7];
		dayNames = new JLabel[7];
		daySelected = null;
		monthAndYearLabel = new JLabel();
		displayCalendar();
	}
	
	/**
	 * displays the day View  showing the events at the specified time and day.
	 * It also coordinates with the month view so every time the user presses previous or next
	 * the day changes in the month view and the day view
	 */
	public void displayCalendar() {
		today = myCalendar.get(Calendar.DAY_OF_MONTH);
		month = myCalendar.get(Calendar.MONTH); //Jan = 0
		year = myCalendar.get(Calendar.YEAR);
		
		//Label to display the month and year
		monthAndYearLabel.setText("   " + arrayOfMonths[month] + " " + year);
		monthAndYearLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		monthAndYearLabel.setBackground(Color.WHITE);

		//Labels to display Su Mo Tu We Th Fr Sa 
		for (int i = 0; i < arrayOfDayNames.length; i++) {
			JLabel dayLabel = new JLabel("     " + arrayOfDayNames[i].toString());
			dayLabel.setBackground(Color.WHITE);
			dayNames[i] = dayLabel;
		}
		
		//Buttons to display days 1-28,29,30 or 31
		firstDayOfMonth();
		int lastDayOfMonth = myCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		int dayCount = myCalendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		
		for (int col = 0; col < daysOrder[0].length; col++) {
			for (int row = 0; row < daysOrder.length; row++) {
				
				//Place holders in the calendar before the first day and and after the last day
				if ((row < (nameOfFirstDayOfMonth - 1) && col == 0) || (dayCount > lastDayOfMonth) ) {
					JPanel holdBlankSpace = new JPanel();
					JLabel blankSpace = new JLabel("");
					holdBlankSpace.add(blankSpace);
					holdBlankSpace.setBackground(Color.WHITE);
					daysOrder[row][col] = holdBlankSpace;
					continue;
				}
				
				//If the day is less or equal to the last day of the month
				if (dayCount <= lastDayOfMonth) {
					
					//If the day is the same as today, put border around the day to represent today
					if (dayCount == today) 
					{
						JPanel holdTodayNumber = new JPanel();
						JLabel todayNumber = new JLabel(Integer.toString(dayCount));
						holdTodayNumber.add(todayNumber);
						holdTodayNumber.setBackground(Color.WHITE);
						holdTodayNumber.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(3.0f)));
						daysOrder[row][col] = holdTodayNumber;
					}
					else 
					{
						JPanel holdDayNumber = new JPanel();
						JLabel dayNumber = new JLabel(Integer.toString(dayCount));
						holdDayNumber.add(dayNumber);
						holdDayNumber.setBackground(Color.WHITE);
						daysOrder[row][col] = holdDayNumber;
					}
				}
				dayCount++;
			}
		}	
		
		//panel to hold the day names and day numbers of the month
		JPanel gridPanel = new JPanel(new GridLayout(0,7));
		gridPanel.setBackground(Color.WHITE);
		
		for(JLabel label : dayNames)
		{
			gridPanel.add(label);
		}
		
		for (int col = 0; col < daysOrder[0].length; col++) {
			for (int row = 0; row < daysOrder.length; row++) {
				daysOrder[row][col].addMouseListener(new MousePressedListener());
				gridPanel.add(daysOrder[row][col]);
			}
		}
		
		//add month and year label and grid panel to the month panel(this)
		this.setLayout(new BorderLayout());
		this.setBackground(Color.WHITE);
		this.add(monthAndYearLabel, BorderLayout.NORTH);
		this.add(gridPanel, BorderLayout.CENTER);
	}
	
	/**
	 * Mouselistener to know when a day on the month view is pressed
	 */
	private class MousePressedListener extends MouseAdapter
	{
		@Override
		public void mousePressed(MouseEvent event)
		{
				if(oneSelected == 0) {
					oneSelected = 1;
				}
				else
					dayPanelSelected.setBackground(Color.WHITE);
				
				dayPanelSelected = (JPanel) event.getSource();
				dayPanelSelected.setBackground(new Color(255,102,102));
				
				for(Component c: dayPanelSelected.getComponents()) {
					if(c instanceof JLabel) {
						daySelected = ((JLabel)c).getText();
					}
				}
		}
	}
	
	/**
	 * get the date of the selected day on the month view
	 * @return the date of selected day
	 */
	public String getDate(){
		if(daySelected == "")
			return "No date selected";
		else
			return (month+1) + "/"+ daySelected +"/" + year;
	}
	
	/**
	 * Checks if a day is selected 
	 * @return true if a day is selected
	 * 		   false if a day is not selected
	 */
	public boolean isDaySelected() {
		if(daySelected == null) 
			return false;
		else
			return true;
	}
	
	/**
	 * It takes the calendar back one day
	 */
	public void previous()
	{
		myCalendar.add(Calendar.DATE, -1);
		displayCalendar();
	}
	
	/**
	 * It taked the calendar forward one day
	 */
	public void next()
	{
		myCalendar.add(Calendar.DATE, 1);
		displayCalendar();
	}
	
	/**
	 * Gets the first day of each month to know where to start the month view
	 */
	public void firstDayOfMonth() {
		GregorianCalendar c = new GregorianCalendar();
		c.set(Calendar.MONTH, myCalendar.get(Calendar.MONTH));
		c.set(Calendar.DAY_OF_MONTH, 1);
		nameOfFirstDayOfMonth = c.get(Calendar.DAY_OF_WEEK);
	}
}