package Gobang.Core;

public class Main{
    public static void main(String[] args){
        Model model = new Model();
        model.setSize(9);
        // View_text view = new View_text();
        // Controller controller = new Controller(model, view);
        Controller controller = new Controller(model, 5);
        View_GUI gui = new View_GUI(controller, 9);
        controller.gameStartGUI();
    }
}