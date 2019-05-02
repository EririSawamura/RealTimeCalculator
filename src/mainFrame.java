import java.awt.*;
import java.awt.event.*;
public class mainFrame extends Frame implements ActionListener{
	final private static String CALCULATOR = "Calculator Panel", TEMPERATURE = "Temperature Panel";
	public static void main(String[] args)  {
        new mainFrame();
	}
    private mainFrame() {
    	CardLayout cL = new CardLayout();
    	setLayout(cL);
        setTitle("Clever calculator");
        setSize(450, 480);
        setVisible(true);
        addWindowListener(new WindowListener());
        MenuBar mB = new MenuBar();
        Menu m = new Menu("Choice");
        MenuItem mI2 = new MenuItem("Temperature");
        MenuItem mI3 = new MenuItem("Calculator");
        mI2.addActionListener(this);
        mI3.addActionListener(this);            
        m.add(mI3);
        m.add(mI2);
        mB.add(m);
        setMenuBar(mB);
		Calculator c1 = new Calculator();
		TemperatureConverter mC = new TemperatureConverter();
        add(mC, TEMPERATURE);
        add(c1, CALCULATOR);
        cL.show(this, CALCULATOR);
    }
	@Override
	public void actionPerformed(ActionEvent e) {
		CardLayout card = (CardLayout)this.getLayout();
		String choice = e.getActionCommand();
		if(choice.equals("Calculator")) {
		    card.show(this, CALCULATOR);
		}
		else if(choice.equals("Temperature")) {
		    card.show(this, TEMPERATURE);
		}
	}
	class WindowListener extends java.awt.event.WindowAdapter{
        public void windowClosing(WindowEvent evt) {
            System.exit(0);
        }
    }
}