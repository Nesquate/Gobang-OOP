package Gobang.Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Room {
    protected ArrayList<Socket> clientList;
    private int roomSize;
    protected int enterCount;
    protected int playerID;

    public Room(int size){
        this.clientList = new ArrayList<Socket>();
        this.roomSize = size;
        this.enterCount = 0;
        this.playerID = 0;
    }

    protected void addToRoom(Socket socket){
        if(this.enterCount <= this.roomSize){
            this.clientList.add(socket);
            // try {
                this.playerID++;
                Integer sendID = this.playerID;
                // PrintWriter outBound = new PrintWriter(socket.getOutputStream(), true);
                // outBound.println("100");
                // outBound.println(sendID.toString());
                this.enterCount++;
                System.out.println("Sucessful to add room.");
                System.out.println("Debug : Now Enter ->"+this.enterCount);
                System.out.println("Debug : Now ID ->"+this.playerID);
			// } catch(IOException exception) {
            //     System.out.println("Error : Send PlayerID error!");
            //     System.out.println("Debug :");
            //     exception.printStackTrace();
			// }
        }  
        else
            System.out.println("Room is full.");
    }

    protected void removeFromRoom(Socket socket){
        this.clientList.remove(socket);
        this.enterCount--;
        this.playerID = this.playerID - 1;
        System.out.println("Debug : Now ->"+this.playerID);
        System.out.println("Sucessful to remove from room.");
        System.out.println("Debug : Now Enter ->"+this.enterCount);
        System.out.println("Debug : Now ID ->"+this.playerID);
    }

    protected void resetPlayerID(Socket socket){
        System.out.println("Resetting player ID...");
        try {
            System.out.println("Debug : Before receive ->"+this.playerID);
            if(this.playerID >= 2){
                this.playerID--;
            }
            Integer sendID = this.playerID;
            PrintWriter outBound = new PrintWriter(socket.getOutputStream(), true);
            outBound.println(sendID.toString());
            System.out.println("Sucessful to reset PlayerID!");
        } catch(IOException exception) {
            System.out.println("Error : Send PlayerID error!");
            System.out.println("Debug :");
            exception.printStackTrace();
        }
    }

    protected int getNowID(){
        return this.playerID;
    }

    protected int getSize(){
        return this.roomSize;
    }

    protected int getEnter(){
        return this.enterCount;
    }

}
