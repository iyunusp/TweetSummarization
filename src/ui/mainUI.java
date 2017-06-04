package ui;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class mainUI extends JFrame {
	public static mainUI main;
	private final int WINDOW_WIDTH = 600, WINDOW_HEIGHT = 400;
	private JPanel grid;
	private HintTextField userI, dateI;
	private JButton summ;
	private JTextArea tweets,summary;
	private String user;
	int date=0;
	private void initAll(){
		grid = new JPanel(new GridLayout(2, 1));
		JPanel grid1 = new JPanel(new GridLayout(3, 1));
		JPanel grid2 = new JPanel(new GridLayout(2, 1));
		grid2.setBorder(new EmptyBorder(0, 5, 0, 5));
		JPanel flow= new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel flow1= new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel flow2= new JPanel(new FlowLayout(FlowLayout.CENTER));
		userI= new HintTextField("Twitter ID without '@' symbol ex: iyunusp");
		userI.setPreferredSize(new Dimension(225,25));
		dateI= new HintTextField("yymmdd ex: 170131 for 31 January 2017");
		dateI.setPreferredSize(new Dimension(225,25));
		JLabel userL=new JLabel("Username"),dateL=new JLabel("Date");
		userL.setPreferredSize(new Dimension(80,25));
		dateL.setPreferredSize(new Dimension(80,25));
		tweets= new JTextArea();
		tweets.setEditable(false);
		tweets.setLineWrap(true);
		tweets.setToolTipText("Tweets");
		JScrollPane scroll= new JScrollPane(tweets,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(new EmptyBorder(0, 0, 8, 0));
		summary= new JTextArea();
		summary.setEditable(false);
		summary.setLineWrap(true);
		summary.setToolTipText("Summary of Tweet");
		summ= new JButton("Summarize");
		summ.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				user=userI.getText();
				if(!user.equals("")){
					try{
						date=Integer.parseInt(dateI.getText());
						if(dateI.getText().length()!=6)
							throw new Exception();
						summarize();
					}catch (Exception e){
						JOptionPane.showMessageDialog(new JFrame(), "Date must be natural number with length of 6 number", "Warning",
			        	        JOptionPane.WARNING_MESSAGE);
					}
				}else{
					JOptionPane.showMessageDialog(new JFrame(), "Username can't be empty", "Warning",
		        	        JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		flow.add(userL);
		flow.add(userI);
		grid1.add(flow);
		flow1.add(dateL);
		flow1.add(dateI);
		grid1.add(flow1);
		flow2.add(summ);
		grid1.add(flow2);
		grid2.add(scroll);
		grid2.add(summary);
		grid.add(grid1);
		grid.add(grid2);
		add(grid);
	}
	private void testPython(){
		try {
			String s="",logg="";
			Process p=Runtime.getRuntime().exec("py -3 pytes.py");
	        BufferedReader stdError = new BufferedReader(new 
	                 InputStreamReader(p.getErrorStream()));
	        while ((s = stdError.readLine()) != null) {
	        	logg+=s;
	        }
	        if(!"".equals(logg)){
	        	userI.setEnabled(false);
	        	dateI.setEnabled(false);
	        	summ.setEnabled(false);
	        	JOptionPane.showMessageDialog(new JFrame(), "You need atleast python3 with installed nltk library\nto run this program", "Error",
	        	        JOptionPane.ERROR_MESSAGE);
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public mainUI(){
		initAll();
		testPython();
		setVisible(true);
		setTitle("Tweet Summary");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	public static mainUI newInstance(){
		main= new mainUI();
		return main;
	}
	private void summarize(){
		String s=null,log="";
		try {
			Process p=Runtime.getRuntime().exec("py -3 tweet.py -u "+user+" -d "+date);
			BufferedReader stdInput = new BufferedReader(new 
	                 InputStreamReader(p.getInputStream()));

	        BufferedReader stdError = new BufferedReader(new 
	                 InputStreamReader(p.getErrorStream()));
	        // read the output from the command
	        for (int i = 0;(s = stdInput.readLine()) != null; i++) {
	        	if(i==0){
	        		tweets.setText(s);
	        	}else{
	        		summary.setText(s);
	        	}
			}
	        // read any errors from the attempted command
	        while ((s = stdError.readLine()) != null) {
	        	log=log+s+"\n";
	        }
	        if(!log.equals("")){
	        	JOptionPane.showMessageDialog(new JFrame(), "There are no more than\n 3 Tweets in "+date+" for "+user, "Information",
	        	        JOptionPane.INFORMATION_MESSAGE);
	        	userI.resetText();
	        	dateI.resetText();
	        	tweets.setText("");
	        	summary.setText("");
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	class HintTextField extends JTextField implements FocusListener {

		  private final String hint;
		  private boolean showingHint;

		  public HintTextField(final String hint) {
		    super(hint);
		    this.hint = hint;
		    this.showingHint = true;
		    super.addFocusListener((FocusListener) this);
		  }
		  public void resetText(){
			  super.setText(hint);
		      showingHint = true;
		  }
		  @Override
		  public void focusGained(FocusEvent e) {
		    if(this.getText().isEmpty()) {
		      super.setText("");
		      showingHint = false;
		    }
		  }
		  @Override
		  public void focusLost(FocusEvent e) {
		    if(this.getText().isEmpty()) {
		      super.setText(hint);
		      showingHint = true;
		    }
		  }

		  @Override
		  public String getText() {
		    return showingHint ? "" : super.getText();
		  }
	}
}
