import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *Main view of the calendar. It puts both monthView and dayView together.
 *It creates the buttons for create, quit, previous, and next.
 *It also contains the listeners to those buttons.
 */
public class CalendarView {
	
	private MonthView month;
	private DayView day;
	private Model model;
	private JFrame frame;
	private EventView newEvent;
	private JButton leftArrow;
	private JButton rightArrow;
	private JButton quit;
	private JButton create;
	private JFrame popUpFrame;
	
	public CalendarView() throws ClassNotFoundException, IOException
	{
		//Create model, monthView, and dayView, and load the events previously saved  
		model = new Model();
		month = new MonthView();
		model.load();
		day = new DayView(model);
		
		//create frame 
		frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setUndecorated(true);
		
		//create buttons in Calendar
		leftArrow = new JButton("<");
		rightArrow = new JButton(">");
		quit = new JButton("Quit");
		create = new JButton("CREATE");
		
		//decorate buttons
		leftArrow.setBackground(new Color(204,204,204));
		leftArrow.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		rightArrow.setBackground(new Color(204,204,204));
		rightArrow.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		
		//quit.setBackground(new Color(51,153,255));
		quit.setBackground(new Color(0,0,204));
		quit.setForeground(Color.WHITE);
		quit.setFont(new Font("Tahoma", Font.BOLD, 14));
		quit.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		
		create.setBackground(new Color(204,0,0));
		create.setForeground(Color.WHITE);
		create.setFont(new Font("Tahoma", Font.BOLD, 14));
		create.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		
		//controllers for buttons
		leftArrow.addActionListener(e->{
			month.previous();
			day.previous();
		});
		
		rightArrow.addActionListener(e->{
			month.next();
			day.next();
		});
		
		quit.addActionListener(e->{
			frame.dispose();
			try {
				model.save();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		create.addActionListener(e->{
			if(month.isDaySelected() == true)
				popUp(month);
		});
		
		//create panel to hold both arrows buttons
		JPanel arrowsPanel = new JPanel();
		arrowsPanel.setBackground(Color.WHITE);
		arrowsPanel.add(leftArrow);
		arrowsPanel.add(rightArrow);
		
		//create panel to hold arrows panel and quit button
		JPanel arrowsAndQuitPanel = new JPanel(new BorderLayout());
		arrowsAndQuitPanel.setBackground(Color.WHITE);
		arrowsAndQuitPanel.setBorder(new EmptyBorder(10, 10, 20, 10));
		arrowsAndQuitPanel.add(quit, BorderLayout.EAST);
		arrowsAndQuitPanel.add(arrowsPanel, BorderLayout.CENTER);
		
		//create panel to hold all buttons
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBackground(Color.WHITE);
		northPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
		northPanel.add(arrowsAndQuitPanel, BorderLayout.NORTH);
		northPanel.add(create, BorderLayout.WEST);
		
		//add all panels to frame
		frame.add(northPanel, BorderLayout.NORTH);
		frame.add(month, BorderLayout.WEST);
		frame.add(day, BorderLayout.CENTER);
		
		frame.setSize(1000, 500);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	/**
	 * Creates a pop Up frame to ask the user for an event and time.
	 * It also creates the save button and the listener for it.
	 * @param m month view to get the date
	 */
	private void popUp(MonthView m) {
	   //Create pop up frame and the labels, field, and button for it
	   popUpFrame = new JFrame();
	   popUpFrame.setLayout(new BorderLayout(50,20));
	   popUpFrame.setUndecorated(true);
	   popUpFrame.getRootPane().setBorder(BorderFactory.createMatteBorder(1,10,1,10, new Color(255,255,204)));
	   popUpFrame.getContentPane().setBackground(new Color(255,255,204));
	   JLabel eventLabel = new JLabel("Enter an event: ");
	   JTextField event = new JTextField(20);
	   JPanel date = new JPanel();
	   date.setBackground(Color.WHITE);
	   date.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	   JLabel dateLabel = new JLabel(m.getDate());
	   date.add(dateLabel);
	   JTextField from = new JTextField(4);
	   JTextField to = new JTextField(4);
	   JLabel timeLabel = new JLabel(" to ");
	   timeLabel.setHorizontalAlignment(JLabel.CENTER);
	   JButton save = new JButton("SAVE");
	   JLabel timeConflict = new JLabel("TIME CONFLICT - can't save event");
	   JLabel BlankField = new JLabel("Blank Field - can't save event");
	   
	   //save button listener
	   save.addActionListener(e->{
		   //If a field is empty, don't save
		   if(from.getText().isEmpty() || to.getText().isEmpty() || event.getText().isEmpty()) {
				popUpFrame.add(BlankField, BorderLayout.SOUTH);
				popUpFrame.setSize(500, 120);
				popUpFrame.validate();
		   }
		   else { //Else save 
			   newEvent = new EventView(dateLabel.getText(), from.getText(), to.getText(), event.getText());
			   boolean result = checkForTimeConflict(newEvent);
			  if(result == false) {  //If there is no time conflict, save, and close the pop up frame
				  model.setNewEvent(newEvent);
				  day.displayView();
				  popUpFrame.dispose();
			  }
			  else   //If there is a time conflict, don't save
			  {
				popUpFrame.add(timeConflict, BorderLayout.SOUTH);
				popUpFrame.setSize(500, 120);
				popUpFrame.validate();
			  }
		   }
	   });
	   
	   //Create JPanels to hold all the labels, fields and the save button in the pop up frame 
	   JPanel holdEventAndEventLabel = new JPanel(new BorderLayout());
	   JPanel holdFromToAndTimeLabel = new JPanel(new GridLayout(0, 3));
	   
	   holdEventAndEventLabel.setBackground(new Color(255,255,204));
	   holdFromToAndTimeLabel.setBackground(new Color(255,255,204));
	   
	   holdEventAndEventLabel.add(eventLabel, BorderLayout.NORTH);
	   holdEventAndEventLabel.add(event, BorderLayout.SOUTH);
	   
	   holdFromToAndTimeLabel.add(from);
	   holdFromToAndTimeLabel.add(timeLabel);
	   holdFromToAndTimeLabel.add(to);
	   
	   popUpFrame.add(holdEventAndEventLabel, BorderLayout.NORTH);
	   popUpFrame.add(date, BorderLayout.WEST);
	   popUpFrame.add(holdFromToAndTimeLabel, BorderLayout.CENTER);
	   popUpFrame.add(save, BorderLayout.EAST);
	  
	   popUpFrame.setSize(500, 80);
	   popUpFrame.setLocationRelativeTo(null);
	   popUpFrame.setVisible(true);
	}
	
	
	/**
	 * Check if there is a time conflict between events
	 * @param newEvent the new event the user wants to create
	 * @return true if there is a time conflict
	 *         false if the is no time conflict
	 */
	private boolean checkForTimeConflict(EventView newEvent)
	{
		for(EventView e : model.getAllEvents())
		{
			if(newEvent.getDate().equals(e.getDate()) && newEvent.getStartingTime().equals(e.getStartingTime()))
				return true;
		}
		
		return false;
	}
}

