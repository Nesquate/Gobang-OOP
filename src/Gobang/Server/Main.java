package Gobang.Server;

public class Main{
    public static void main(String[] args){
        ServerUnit unit;
        if(args.length == 0){
            unit = new ServerUnit();
        }else{
            unit = new ServerUnit(args[0]);
        }
        
        unit.startAndReceive();
    }
}
