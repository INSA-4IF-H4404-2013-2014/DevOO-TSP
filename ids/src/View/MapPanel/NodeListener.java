package View.MapPanel;

import Model.City.*;

/**
 * MapPanel node's event listener.
 */
public interface NodeListener {

    /**
     * Click event on a node in a given map panel
     * @param panel the map panel that has received the mouse clicked event
     * @param node the model node that has been clicked
     */
    public void nodeClicked(MapPanel panel, Model.City.Node node);

}
