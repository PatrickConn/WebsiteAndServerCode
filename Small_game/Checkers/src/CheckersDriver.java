
import javax.swing.JApplet;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class CheckersDriver extends JApplet implements ActionListener, ItemListener {
	
	public CheckersBoard draughtsPanel;
	public JButton newGameButton;
	CheckersDriver main;

	public void init() {
		try {
			CheckersDriver main;
			main = this;
			draughtsPanel = new CheckersBoard(main);
	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        	public void run() {
					setLayout(null);
			        newGameButton = new JButton("New Game");
			        newGameButton.setFont(new Font("Dialog",Font.BOLD,14));
			        newGameButton.setEnabled(true);
			        newGameButton.addActionListener(new ActionListener() {
			            public void actionPerformed(ActionEvent e)
			            {
			                //Execute when button is pressed
			            	draughtsPanel.newGame();
			            	
			            	
			            
			            }
			        });
			        


			           
			        add(draughtsPanel);
			        add(newGameButton);
			        setSize(900,720);
			        draughtsPanel.setBounds(0,0,720,720);
			        newGameButton.setBounds(720,280,116,54);
			        repaint();
			
	        	}
	        });
		}
		catch(Exception e) {
			System.out.println("Exception");
		}
		
	}
	public void initialPositionSet()
    {
        
   
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub);
	    
		
	}


}
