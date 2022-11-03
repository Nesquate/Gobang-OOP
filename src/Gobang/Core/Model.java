package Gobang.Core;

public class Model {
    protected int[][] board;
    private String player1;
    private String player2;
    private int size;

    public Model(){
        
    }

    public int getSize(){
        return this.size;
    }

    public String getName(int turn){
        String name = null;
        if(turn == 1) 
            name = this.player1;
        if(turn == 2) 
            name = this.player2;
        return name;
    }

    public int setValue(int x, int y, int playerSN){
        if(this.board[x][y] != 0) return 1;
        this.board[x][y] = playerSN;
        return 0;
    }

    public void setSize(int size){
        this.size = size;
        this.board = new int[this.size][this.size];
    }

    public void setName(int turn, String name){
        if(turn == 1) 
            this.player1 = name;
        if(turn == 2) 
            this.player2 = name;
    }

    public void resetBoard(){
        for(int i = 0;i < this.size;i++){
            for(int j = 0;j < this.size;j++){
                this.board[i][j] = 0;
            }
        }
    }

    public boolean searchAll(){
        for(int i = 0;i < getSize();i++){
            for(int j = 0;j < getSize();j++){
                if(board[i][j] != 0) return false;
            }
        }
        return true;
    }

    public int search(int x, int y, int playerSN, int path){
        int i, j, find = 1;
        i = x;j = y;
        if(path == 0){
            while(i >= 0 && j >= 0){ // WN
                i--;j--;
                if(i < 0 || j < 0) break;
                if(this.board[i][j] != playerSN) break;
                find++;
            }
            i = x;j = y;
            while(i < this.size && j < this.size){ // SE
                i++;j++;
                if(i >= this.size || j >= this.size) break;
                if(this.board[i][j] != playerSN) break;
                find++;
            }
        }else if(path == 1){
            while(j >= 0){ // N
                j--;
                if(j < 0) break;
                if(this.board[i][j] != playerSN) break;
                find++;
            }
            i = x;j = y;
            while(j < this.size){ // S
                j++;
                if(j >= this.size) break;
                if(this.board[i][j] != playerSN) break;
                find++;
            }
        }else if(path == 2){
            while(i < this.size && j >= 0){ // EN
                i++;j--;
                if(i >= this.size || j < 0) break;
                if(this.board[i][j] != playerSN) break;
                find++;
            }
            i = x;j = y;
            while(i >= 0 && j < this.size){ // WS
                i--;j++;
                if(i < 0 || j >= this.size) break;
                if(this.board[i][j] != playerSN) break;
                find++;
            }
        }else if(path == 3){
            while(i < this.size){ // E
                i++;
                if(i >= this.size) break;
                if(this.board[i][j] != playerSN) break;
                find++;
            }
            
            i = x;j = y;
            while(i >= 0){ // W
                i--;
                if(i < 0) break;
                if(this.board[i][j] != playerSN) break;
                find++;
            }
        }else{
            return -1;
        }
        
        return find;
    }

    public boolean searchFourAndFront(int x, int y, int playerSN, int path){
        int i, j, find = 1, front = 0, back = 0;
        i = x;j = y;
        if(path == 0){
            while(i >= 0 && j >= 0){ // WN
                i--;j--;
                if(i < 0 || j < 0) break;
                if(this.board[i][j] != playerSN){
                    if(this.board[i][j] == 0){
                        front = 1;
                    }
                    break;
                }
                find++;
            }
            i = x;j = y;
            while(i < this.size && j < this.size){ // SE
                i++;j++;
                if(i >= this.size || j >= this.size) break;
                if(this.board[i][j] != playerSN){
                    if(this.board[i][j] == 0){
                        back = 1;
                    }
                    break;
                }
                find++;
            }
        }else if(path == 1){
            while(j >= 0){ // N
                j--;
                if(j < 0) break;
                if(this.board[i][j] != playerSN){
                    if(this.board[i][j] == 0){
                        front = 1;
                    }
                    break;
                }
                find++;
            }
            i = x;j = y;
            while(j < this.size){ // S
                j++;
                if(j >= this.size) break;
                if(this.board[i][j] != playerSN){
                    if(this.board[i][j] == 0){
                        back = 1;
                    }
                    break;
                }
                find++;
            }
        }else if(path == 2){
            while(i < this.size && j >= 0){ // EN
                i++;j--;
                if(i >= this.size || j < 0) break;
                if(this.board[i][j] != playerSN){
                    if(this.board[i][j] == 0){
                        front = 1;
                    }
                    break;
                }
                find++;
            }
            i = x;j = y;
            while(i >= 0 && j < this.size){ // WS
                i--;j++;
                if(i < 0 || j >= this.size) break;
                if(this.board[i][j] != playerSN){
                    if(this.board[i][j] == 0){
                        back = 1;
                    }
                    break;
                }
                find++;
            }
        }else if(path == 3){
            while(i < this.size){ // E
                i++;
                if(i >= this.size) break;
                if(this.board[i][j] != playerSN){
                    if(this.board[i][j] == 0){
                        front = 1;
                    }
                    break;
                }
                find++;
            }
            
            i = x;j = y;
            while(i >= 0){ // W
                i--;
                if(i < 0) break;
                if(this.board[i][j] != playerSN){
                    if(this.board[i][j] == 0){
                        back = 1;
                    }
                    break;
                }
                find++;
            }
        }else{
            return false;
        }
        
        if((front == 1 && back == 1) && (find == 4)) return true;
        return false;
    }

    

}
