import Controller.MainWindowController;

public class Main {
    public static void main ( String[] args) {
        if (System.getProperty("os.name").contains("Mac")) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "INSA Map");
        }

        MainWindowController controller = new MainWindowController();

        // automatic load for map testing
        /*
        {
            controller.loadNetwork("../sujet/plan20x20.xml");
            controller.loadRound("../sujet/livraison20x20-1.xml");
        }

        */

    }
}
