package Gobang.Core;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
// import java.awt.event.*;

public class View_GUI extends JFrame implements WindowListener{
    // constant value
    private static final int FRAME_WIDTH    = 600;
    private static final int FRAME_HEIGHT   = 600;
    private static final int FRAME_X_ORIGIN = 500;
    private static final int FRAME_Y_ORIGIN = 150;

    // Button array
    protected MyButton[][] buttonArray;
    private Container pane;
    private JMenuBar menubar;
    private Controller handler;
    private int size;
    private JMenuItem itemNew, itemReset, itemExit, itemConnect, itemDisconnect, itemRemoteReset;
    private WindowListener windowhandler;

    public View_GUI(Controller handler, int size){
        this.size = size;

        this.pane = this.getContentPane( );


        //set the frame properties
        this.setSize      (FRAME_WIDTH, FRAME_HEIGHT);
        this.setResizable (false);
        this.setTitle     ("Gobang");
        this.setLocation  (FRAME_X_ORIGIN, FRAME_Y_ORIGIN);
        
        this.buttonArray = new MyButton[this.size][this.size];

        // set Handler 
        this.handler = handler;
        this.handler.registerGUI(this);

        // set MenuBar
        this.menubar = new JMenuBar();
        this.setJMenuBar(this.menubar);
        
        settingLayout();
        setButtons();
        setFileMenu();
        setConnectMenu();
        // setAboutMenu();

		
		//register 'Exit upon closing' as a default close operation
        // setDefaultCloseOperation( EXIT_ON_CLOSE );
        this.addWindowListener(this);
    }

    @Override
    public void windowActivated(WindowEvent e){

    }
    @Override
    public void windowClosed(WindowEvent e){

    }
    @Override
    public void windowClosing(WindowEvent e){
        if(this.handler.socketIsConnect == 1){
            this.handler.disconnectSocket();
        }
        this.closeWindow();
    }
    @Override
    public void windowDeactivated(WindowEvent e){

    }
    @Override
    public void windowDeiconified(WindowEvent e){

    }
    @Override
    public void windowIconified(WindowEvent e){

    }
    @Override
    public void windowOpened(WindowEvent e){

    }

    public void registerController(Controller controller){
        this.handler = controller;
    }

    public void settingLayout(){
        this.pane.setLayout(new GridLayout(9,9));
    }

    public void setButtons(){
        for(int i = 0; i < this.size; i++) {
            for(int j = 0; j < this.size; j++){
                MyButton button = new MyButton();
			    //button.setText(Integer.toString(i));
                button.setBackground(Color.decode("#D08D3E"));
                // button.setContentAreaFilled(false);
                button.setFocusPainted(false);
                button.setBorder(new LineBorder(Color.BLACK));
                this.pane.add(button);
                button.addActionListener(handler);
                button.setXPos(i);
                button.setYPos(j);
			    this.buttonArray[i][j] = button;
            }
			
		}
    }

    public void setFileMenu(){
        JMenu menu;
        menu = new JMenu("File");
        // itemNew = new JMenuItem("New Local Game");
		// itemNew.addActionListener(handler);
		// menu.add(itemNew);
        itemReset = new JMenuItem("Local Game Reset");
        itemReset.addActionListener(handler);
        itemReset.setActionCommand("lgamereset");
		menu.add(itemReset);
        itemExit = new JMenuItem("Exit");
        itemExit.setActionCommand("exit");
		itemExit.addActionListener(handler);
		menu.add(itemExit);
		
		this.menubar.add(menu);
    }

    public int showConnectWindow(){
        JTextField addressField = new JTextField(20);
        JTextField portField = new JTextField(5);

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Address:"));
        myPanel.add(addressField);
        myPanel.add(Box.createHorizontalStrut(5)); // a spacer
        myPanel.add(new JLabel("Port:"));
        myPanel.add(portField);

        while(true){
            int result = JOptionPane.showConfirmDialog(this, myPanel,
            "Enter Server Address", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                if(addressField.getText().length() == 0 || portField.getText().length() == 0){
                    JOptionPane.showMessageDialog(this, "Empty Field is not allow!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                this.handler.address = addressField.getText();
                this.handler.port = Integer.valueOf(portField.getText());
                return result;
            }
            if (result == JOptionPane.CANCEL_OPTION){
                return result;
            }
        }
    }

    public void setConnectMenu(){
        JMenu menu;
        menu = new JMenu("Connect");
        itemConnect = new JMenuItem("Connect Server");
        itemConnect.addActionListener(handler);
        itemConnect.setActionCommand("connect");
		menu.add(itemConnect);
        itemDisconnect = new JMenuItem("Disconnect");
        itemDisconnect.addActionListener(handler);
        itemDisconnect.setActionCommand("disconnect");
        itemDisconnect.setEnabled(false);
        menu.add(itemDisconnect);
        itemRemoteReset = new JMenuItem("Remote Game Reset");
        itemRemoteReset.addActionListener(handler);
        itemRemoteReset.setActionCommand("egamereset");
        itemRemoteReset.setEnabled(false);
		menu.add(itemRemoteReset);
		
		this.menubar.add(menu);
    }

    public void connectMode(){
        itemConnect.setEnabled(false);
        itemDisconnect.setEnabled(true);
        itemRemoteReset.setEnabled(true);
        itemReset.setEnabled(false);
        this.resetButton();
    }
    public void localMode(){
        itemConnect.setEnabled(true);
        itemDisconnect.setEnabled(false);
        itemRemoteReset.setEnabled(false);
        itemReset.setEnabled(true);
        this.resetButton();
    }

    public void setAboutMenu(){
        JMenu menu;
        JMenuItem item;
        menu = new JMenu("About");
        item = new JMenuItem("Gobang");
		item.addActionListener(handler);
		menu.add(item);
        item = new JMenuItem("This Program");
        item.addActionListener(handler);
        menu.add(item);
		
		this.menubar.add(menu);
    }

    protected void showConnectFail(){
        JOptionPane.showMessageDialog(this, "Fail to Connect server", "Connect Fail", JOptionPane.ERROR_MESSAGE);
        // JOptionPane.showMessageDialog(this, "Fail to connect server");
        setButtonDisable();
    }
    protected void showDisconnectFail(){
        JOptionPane.showMessageDialog(this, "Fail to disconnect server", "Disconnect Fail", JOptionPane.ERROR_MESSAGE);
        // JOptionPane.showMessageDialog(this, "Fail to disconnect server");
        setButtonDisable();
    }

    protected void showWinner(int turn){
        ImageIcon icon = new ImageIcon("Resources/Images/firework.gif");
        JOptionPane.showMessageDialog(this, "Player "+turn+" wins!", "Wins!", JOptionPane.INFORMATION_MESSAGE, icon);
        // JOptionPane.showMessageDialog(this, "Player "+turn+" wins!");
        setButtonDisable();
    }

    protected void showWinnerOfFour(int turn){
        ImageIcon icon = new ImageIcon("Resources/Images/firework.gif");
        JOptionPane.showMessageDialog(this, "Player "+turn+" wins by four alive!", "Wins by four!", JOptionPane.INFORMATION_MESSAGE, icon);
        // JOptionPane.showMessageDialog(this, "Player "+turn+" wins by four alive!");
        setButtonDisable();
    }
    protected void showTie(){
        // JOptionPane.showMessageDialog(this, "This game is Tie!");
        JOptionPane.showMessageDialog(this, "This game is Tie!", "Tie!", JOptionPane.INFORMATION_MESSAGE);
        setButtonDisable();
    }
    protected void showOpponentDisconnect(){
        JOptionPane.showMessageDialog(this, "Your opponent is disconnect!");
    }

    protected void setButtonDisable(){
        for(int i = 0;i < this.size;i++){
            for(int j = 0;j < this.size;j++){
                buttonArray[i][j].setBackground(Color.GRAY);
                buttonArray[i][j].setEnabled(false);
            }
        }
    }

    protected void closeWindow(){
        this.dispose();
    }

    protected void resetButton(){
        for(int i = 0;i < this.size;i++){
            for(int j = 0;j < this.size;j++){
                buttonArray[i][j].setEnabled(true);
                buttonArray[i][j].setIcon(null);
                buttonArray[i][j].setActionCommand("");
                buttonArray[i][j].setBackground(Color.decode("#D08D3E"));
            }
        }
    }


    // for unit test only
    // public static void main(String args[]) {
    //     View_GUI frame = new View_GUI(9);
        
    //     frame.setVisible(true);
        
    //     frame.getContentPane().setBackground(Color.ORANGE);
    // }
 

}