package Gobang.Core;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MyButton extends JButton{
    private int x;
    private int y;

    public MyButton(){
        super();
    }

    protected void setXPos(int x){
        this.x = x;
    }

    protected void setYPos(int y){
        this.y = y;
    }

    protected int getXPos(){
        return this.x;
    }

    protected int getYPos(){
        return this.y;
    }
}
