package Gobang.Server;

import java.io.*; 
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.net.*;

public class ServerUnit {
    private ServerSocket server;
    private Socket socket;
    private int port;
    private Room room;

    public ServerUnit(){
        System.out.println("Use default port : 8888");
        newServer(8888);
    }

    public ServerUnit(String args){
        System.out.println("Use port : "+args);
        newServer(Integer.valueOf(args));
    }

    public ServerUnit(int port){
        newServer(port);
    }

    private void newServer(int port){
        this.port = port;
        try{
            this.server = new ServerSocket(this.port);
        }catch (IOException exception){
            System.out.println("Error : Cannot create Server!");
            System.out.println("Debug : ");
            exception.printStackTrace();
        }
    }

    protected void startAndReceive(){
        this.room = new Room(2);

        while(true){
            try{
                this.socket = this.server.accept();
                if(room.getEnter() != room.getSize()){
                    room.addToRoom(this.socket);
                    ClientConnect connect = new ClientConnect(this, this.socket ,this.room);
                    connect.start();
                    System.out.println("New socket : "+this.socket);
                    System.out.println("Debug : roomGetEnter -> "+room.getEnter());
                    if(room.getEnter() >= 2){
                        try{
                            TimeUnit.SECONDS.sleep(1);
                            sendAll("300");
                        }catch(InterruptedException e){
                            System.out.println("Error whiling sleeping.");
                        }
                        
                    } 
                }else{
                    System.out.println("New socket BUT the room is full : "+this.socket);
                    socket.close();
                }
            }catch(IOException exception){
                System.out.println("Error : Socket Problem!");
                System.out.println("Debug : ");
                exception.printStackTrace();
            }
        }
    }

    protected void sendAll(String message){
        Iterator client = this.room.clientList.iterator();
        while (client.hasNext()) {
			Socket tempSocket = (Socket) client.next();
			try {
				PrintWriter outBound = new PrintWriter(tempSocket.getOutputStream(), true);
                outBound.println(message);
                System.out.println("Debug : "+message+" for "+tempSocket+" sent.");
                if(message.compareTo("500") == 0) this.room.resetPlayerID(tempSocket);
			} catch(IOException exception) {
                System.out.println("Error : Send All error!");
                System.out.println("Debug :");
				exception.printStackTrace();
			}
        }
    }
}
