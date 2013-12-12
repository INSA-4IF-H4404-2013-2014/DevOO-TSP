import Controller.MainWindowController;

public class Main {
    public static void main ( String[] args) {
        MainWindowController controller = new MainWindowController();

        // automatic load for map testing
        {
            controller.loadNetwork("../sujet/plan20x20.xml");
            controller.loadRound("../sujet/livraison20x20-1.xml");
        }
    }
}
