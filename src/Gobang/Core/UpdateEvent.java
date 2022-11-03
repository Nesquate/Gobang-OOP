package Gobang.Core;

import java.awt.Color;
import java.io.*;  
import java.net.*; 
import javax.swing.*;

public class UpdateEvent extends Thread {
    private View_GUI gui;
    private Model model;
    private Controller controller;
    private Socket socket;

    public UpdateEvent(View_GUI gui, Model model, Controller controller,  Socket socket){
        this.gui = gui;
        this.model = model;
        this.controller = controller;
        this.socket = socket;
    }

    private void updateBoard(){
        for(int i = 0;i < this.model.getSize();i++){
            for(int j = 0;j < this.model.getSize();j++){
                if(this.model.board[i][j] == 1){
                    // this.gui.buttonArray[i][j].setBackground(Color.BLACK);
                    ImageIcon icon = new ImageIcon("Resources/Images/black.png");
                    this.gui.buttonArray[i][j].setIcon(icon);
                    this.gui.buttonArray[i][j].setActionCommand("1");
                }
                if(this.model.board[i][j] == 2){
                    // this.gui.buttonArray[i][j].setBackground(Color.WHITE);
                    ImageIcon icon = new ImageIcon("Resources/Images/white.png");
                    this.gui.buttonArray[i][j].setIcon(icon);
                    this.gui.buttonArray[i][j].setActionCommand("2");
                }
            }
        }
    }

    @Override
    public void run(){
        // while(true){
            try {
                System.out.println("Debug : Start Update Event.");
                BufferedReader in = new BufferedReader(
                                        new InputStreamReader(
                                            this.socket.getInputStream()));
                String inputLine = in.readLine();
                int i;
                System.out.println("Debug : inputLine -> "+inputLine);
                while ((inputLine = in.readLine()) != null) {
                    i = Integer.valueOf(inputLine);
                    if(i == 100){
                        System.out.println("Receiveing ID...");
                        inputLine = in.readLine();
                        while(inputLine.compareTo("100") == 0){
                            inputLine = in.readLine();
                        }
                        this.controller.onlineTurn = Integer.valueOf(inputLine);
                        System.out.println("You receive ID :"+this.controller.onlineTurn);
                    }
                    if(i == 200){
                        System.out.println("Receiveing...");
                        inputLine = in.readLine();
                        while(inputLine.compareTo("200") == 0){
                            inputLine = in.readLine();
                        }
                        this.controller.turn = Integer.valueOf(inputLine);
                        inputLine = in.readLine();
                        this.controller.x = Integer.valueOf(inputLine);
                        inputLine = in.readLine();
                        this.controller.y = Integer.valueOf(inputLine);
                        this.model.setValue(this.controller.x, this.controller.y, this.controller.turn);
                        System.out.println("Debug : "+this.controller.x+", "+this.controller.y+", "+this.model.board[this.controller.x][this.controller.y]);
                        updateBoard();
                        System.out.println("Debug : "+this.controller.winIsFour(this.controller.x, this.controller.y, this.controller.turn));
                        System.out.println("Debug : "+this.controller.winIsFive(this.controller.x, this.controller.y, this.controller.turn));
                        if(this.controller.winIsFour(this.controller.x, this.controller.y, this.controller.turn)){
                            this.gui.showWinnerOfFour(this.controller.turn);
                            continue;
                        }
                        if(this.controller.winIsFive(this.controller.x, this.controller.y, this.controller.turn)){
                            this.gui.showWinner(this.controller.turn);
                            continue;
                        }
                        if(this.controller.tie()){
                            this.gui.showTie();
                            continue;
                        }
                        this.controller.changeTurn();
                    }
                    if(i == 300){
                        System.out.println("Received reset game command!");
                        this.controller.playing = 1;
                        this.gui.resetButton();
                        this.model.resetBoard();
                        this.controller.turn = 1;
                    }
                    if(i == 500){
                        System.out.println("Received other player disconnected command!");
                        inputLine = in.readLine();
                        this.gui.showOpponentDisconnect();
                        this.gui.setButtonDisable();
                        // this.gui.resetButton();
                        // this.model.resetBoard();
                        this.controller.onlineTurn= Integer.valueOf(inputLine);
                        System.out.println("Received new ID : "+this.controller.onlineTurn);
                        // inputLine = in.readLine();
                    }
                    if(i == 999){
                        System.out.println("Received disconnect command!");
                        in.close();
                        this.controller.disconnectSocket();
                        interrupt();
                    }
                }
            } catch (IOException exception) {
                    return;
                    // System.out.println("Error : Read or send buffer error!");
                    // System.out.println("Debug :");
                    // exception.printStackTrace(); 
            }
        // }
    }
}

