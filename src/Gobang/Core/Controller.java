package Gobang.Core;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;  
import java.net.*; 


public class Controller implements ActionListener{
    private Model model;
    // private View_text view;
    private View_GUI gui;
    private int count;
    protected int x;
    protected int y;
    protected int turn;
    protected int onlineTurn;
    private Socket socket;
    protected int socketIsConnect;
    private UpdateEvent update;
    protected String address;
    protected int port;
    protected int playing;

    // public Controller(Model model, View_text view){
    //     this.model = model;
    //     this.view = view;
    //     this.turn = 1;
    // }

    public Controller(Model model, int count){
        this.model = model;
        this.turn = 1;
        this.count = count;
        this.playing = 0;
    }

    public void registerGUI(View_GUI gui){
        this.gui = gui;
    }

    public void actionPerformed(ActionEvent anEvent){
        if(anEvent.getSource() instanceof MyButton){
            buttonLogic((MyButton)anEvent.getSource());
        }
        if(anEvent.getSource() instanceof JMenuItem){
            JMenuItem menuitem = (JMenuItem)anEvent.getSource();
            if(menuitem.getActionCommand().compareTo("lgamereset") == 0){
                this.gui.resetButton();
                this.model.resetBoard();
                this.turn = 1;
            }
            if(menuitem.getActionCommand().compareTo("exit") == 0){
                if(this.socketIsConnect == 1){
                    disconnectSocket();
                }
                this.gui.closeWindow();
            }
            if(menuitem.getActionCommand().compareTo("connect") == 0){
                if(this.gui.showConnectWindow() == JOptionPane.OK_OPTION){
                    if(connectSocket()){
                        this.gui.resetButton();
                        this.gui.connectMode();
                        this.model.resetBoard();
                        this.socketIsConnect = 1;
                        this.turn = 1;
                        this.gui.setButtonDisable();
                        sendMsgToServer("100");
                    }else{
                        this.gui.showConnectFail();
                    }
                }
            }
            if(menuitem.getActionCommand().compareTo("disconnect") == 0){
                if(disconnectSocket()){
                    this.gui.resetButton();
                    this.gui.localMode();
                    this.model.resetBoard();
                    this.socketIsConnect = 0;
                    this.turn = 1;
                }else{
                    this.gui.showDisconnectFail();
                }
            }
            if(menuitem.getActionCommand().compareTo("egamereset") == 0){
                if(this.playing == 1){
                    this.gui.resetButton();
                    this.model.resetBoard();
                    sendMsgToServer("300");
                }
            }
        }
    }

    private boolean connectSocket(){
        try{
            this.socket = new Socket(this.address, this.port);
            this.update = new UpdateEvent(this.gui, this.model, this, this.socket);
            // this.onlineTurn = receiveMsg();
            // System.out.println("You receive ID :"+this.onlineTurn);
            this.update.start();
        }catch(IOException exception){
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    protected int receiveMsg(){
        int i = 0;
        try {
			BufferedReader in = new BufferedReader(
									new InputStreamReader(
										this.socket.getInputStream()));
            String inputLine = in.readLine();
            i = Integer.valueOf(inputLine);
            inputLine = null;
            in.close();
        } catch (IOException exception) {
            System.out.println("Error : Read buffer error!");
            System.out.println("Debug :");
            exception.printStackTrace(); 
        }
        return i;
    }

    protected boolean disconnectSocket(){
        this.update.interrupt();
        try{
            sendMsgToServer("999");
            this.socket.close();
        }catch(IOException exception){
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    private void sendMsgToServer(String msg){
        try {
            PrintWriter outBound = new PrintWriter(this.socket.getOutputStream(), true);
            outBound.println(msg);
            System.out.println("Debug : Send "+msg);
        } catch(IOException exception) {
            System.out.println("Error : Send message error!");
            System.out.println("Debug :");
            exception.printStackTrace();
        }
    }

    private void buttonLogic(MyButton item){
        String itemText = item.getActionCommand();
        if(itemText.compareTo("") == 0) {
            if(socketIsConnect == 1){
                if(this.onlineTurn == this.turn){
                    whosTurn(item);
                    sendMsgToServer("200");
                    sendMsgToServer(String.valueOf(this.turn));
                    sendMsgToServer(String.valueOf(this.x));
                    sendMsgToServer(String.valueOf(this.y));
                    // changeTurn();
                }
            }else{
                whosTurn(item);
                if(winIsFour(this.x, this.y, this.turn)){
                    this.gui.showWinnerOfFour(this.turn);
                    return;
                }
                if(winIsFive(this.x, this.y, this.turn)){
                    this.gui.showWinner(this.turn);
                    return;
                }
                if(tie()){
                    this.gui.showTie();
                    return;
                }
                changeTurn();
            }
        }       
    }

    private void whosTurn(MyButton item){
        if(this.turn == 1) {
            item.setActionCommand("1");
            ImageIcon icon = new ImageIcon("Resources/Images/black.png");
            item.setIcon(icon);
            // item.setBackground(Color.BLACK);
            item.setIcon(icon);
            setPoint(item.getXPos(), item.getYPos());
            System.out.println("Set "+this.turn+" "+x+" "+y+" here");
            return;
        }
        if(this.turn == 2){
            item.setActionCommand("2");
            ImageIcon icon = new ImageIcon("Resources/Images/white.png");
            item.setIcon(icon);
            // item.setBackground(Color.WHITE);
            setPoint(item.getXPos(), item.getYPos());
            System.out.println("Set "+this.turn+" "+x+" "+y+" here");
            return;
        }
    }

    protected void changeTurn(){
        if(this.turn == 1){
            this.turn = 2;
            System.out.println("Change turn to "+this.turn);
            return;
        }
        if(this.turn == 2){
            this.turn = 1;
            System.out.println("Change turn to "+this.turn);
            return;
        }
    }

    private int getSize(){
        return this.model.getSize();
    }

    private String getName(){
        return this.model.getName(this.turn);
    }

    private void setPoint(int x, int y){
        this.x = x;
        this.y = y;
        this.model.setValue(this.x, this.y, this.turn);
    }

    private void setPoint(){
        Scanner sc = new Scanner(System.in);
        this.x = sc.nextInt();
        this.y = sc.nextInt();
        this.model.setValue(this.x, this.y, turn);
    }

    protected boolean winIsFive(int x, int y, int turn){
        if(this.count == this.model.search(this.x, this.y, this.turn, 0) ||
        this.count == this.model.search(this.x, this.y, this.turn, 1) ||
        this.count == this.model.search(this.x, this.y, this.turn, 2) ||
        this.count == this.model.search(this.x, this.y, this.turn, 3)) return true;
        return false;
    }


    protected boolean winIsFour(int x, int y, int turn){
        if(true == this.model.searchFourAndFront(this.x, this.y, this.turn, 0) ||
        true == this.model.searchFourAndFront(this.x, this.y, this.turn, 1) ||
        true == this.model.searchFourAndFront(this.x, this.y, this.turn, 2) ||
        true == this.model.searchFourAndFront(this.x, this.y, this.turn, 3)) return true;
        return false;
    }

    protected boolean tie(){
        if(this.model.searchAll()) return true;
        return false;
    }

    public void gameStartGUI(){
        this.gui.setVisible(true);
    }

    // private boolean gameControlText(){
    //     String name;
    //     name = this.model.getName(this.turn);
    //     this.view.show(this.turn, this.getSize(), this.model.board, name);
    //     this.setPoint();
    //     if(win() == true){
    //         this.view.showWinner(name);
    //         return false;
    //     }
    //     return true;
    // }

    // public void gameStartText(){
    //     Scanner sc = new Scanner(System.in);
    //     String name1;
    //     String name2;
    //     int size;

    //     this.view.showSetSize();
    //     size = sc.nextInt();
    //     this.model.setSize(size);

    //     this.view.showSetCount();
    //     this.count = sc.nextInt();

    //     this.view.showSetPlayer(1);
    //     name1 = sc.next();
    //     this.model.setName(1, name1);
    //     this.view.showSetPlayer(2);
    //     name2 = sc.next();
    //     this.model.setName(2, name2);

    //     while(gameControlText()){
    //         if(this.turn == 1){
    //             this.turn = 2;
    //         }else{
    //             this.turn = 1;
    //         }
    //     }

    //     this.view.showGameEnd();

    // }
    
}
