package Gobang.Server;

import java.io.*;  
import java.net.*; 
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ClientConnect extends Thread{
    private ServerUnit unit;
    private Room room;
    private Socket socket;

    public ClientConnect(ServerUnit unit, Socket socket, Room room){
        this.unit = unit;
        this.socket = socket;
        this.room = room;
    }

    private void stopSocket() throws IOException{
        // System.out.println("Receiveing...");
        System.out.println("Disconnect socket : "+this.socket);
        this.room.removeFromRoom(this.socket);
        this.socket.close();
        return;
    }

    @Override
    public void run(){
        // while(true){
            int i = 0;
            try {
                BufferedReader in = new BufferedReader(
                                        new InputStreamReader(
                                            this.socket.getInputStream()));
                String inputLine;
                
                while ((inputLine = in.readLine()) != null) {
                    i = Integer.valueOf(inputLine);
                    System.out.println("Debug : "+i);
                    if(i == 100){
                        System.out.println("Transfering ID...");
                        String sendID = Integer.toString(room.getNowID());
                        try {
                            PrintWriter outBound = new PrintWriter(this.socket.getOutputStream(), true);
                            outBound.println("87");
                            outBound.println("100");
                            outBound.println(sendID);
                            System.out.println("Debug : "+sendID+" for "+this.socket+" sent.");

                            // inputLine = in.readLine();
                        } catch(IOException exception) {
                            System.out.println("Error : Send ID error!");
                            System.out.println("Debug :");
                            exception.printStackTrace();
                        }
                    }
                    if(i == 200){
                        System.out.println("Transfering...");
                        this.unit.sendAll("200");
                        while(inputLine != null){
                            System.out.println("Receive : "+inputLine);
                            if(inputLine.compareTo("999") == 0){
                                i = Integer.valueOf(inputLine);
                                inputLine = null;
                                break;
                            }
                            this.unit.sendAll(inputLine);
                            inputLine = in.readLine();
                        }
                    }
                    if(i == 300){
                        this.unit.sendAll("300");
                    }
                    if(i == 999){
                        inputLine = null;
                        break;
                    }
                }
                if(i == 999){
                    // in.close();
                    stopSocket();
                    if(this.room.getEnter() != 0)
                        this.unit.sendAll("500");
                    interrupt();
                }
                
            } catch (IOException exception) {
                    System.out.println("Error : Read or send buffer error!");
                    System.out.println("Debug :");
                    exception.printStackTrace();
                    // if(this.room.getEnter() >= 0) this.room.enterCount--;
                    this.room.removeFromRoom(this.socket);
            }
        // }
        
    }

}
