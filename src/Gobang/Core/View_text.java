package Gobang.Core;

public class View_text{
    public View_text(){
        
    }

    private void printBoard(int size, int[][] board){
        int showView;
        System.out.print("\033[H\033[2J");
        for(int i = 0;i < size;i++){
            for(int j = 0;j < size;j++){
                if(board[i][j] == 0) showView = 0;
                else showView = board[i][j];
                System.out.printf("%d ", showView);
                if(j == size - 1) System.out.println();
            }
        }
    }

    public void show(int turn, int size, int[][] board, String name){
        this.printBoard(size, board);
        System.out.println("-----------------");
        System.out.println("Now Playing :"+name);
        System.out.print("Enter set (x, y) :");
    }

    public void showSetSize(){
        System.out.print("\033[H\033[2J");
        System.out.print("Set board size :");
    }

    public void showSetCount(){
        System.out.print("Set winner condition : ");
    }
    public void showSetPlayer(int turn){
        String name = null;
        if(turn == 1) name = "Player 1";
        if(turn == 2) name = "Player 2";
        System.out.print("Set "+name+" name :");
    }

    public void showGameEnd(){
        System.out.println("The game is end.");
    }

    public void showWinner(String name){
        System.out.print("\033[H\033[2J");
        System.out.println(name+" is winner!");
    }
}
