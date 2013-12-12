package View.MapPanel;

import Model.City.*;

/**
 * MapPanel node's event listener.
 */
public interface NodeListener {

    /**
     * Click event on a node in a given map panel
     * @param node the model node that has been clicked
     */
    public void nodeClicked(Model.City.Node node);

}
