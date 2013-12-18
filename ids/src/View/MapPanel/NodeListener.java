package view.MapPanel;

/**
 * MapPanel node's event listener.
 */
public interface NodeListener {

    /**
     * Click event on a node in a given map panel
     * @param panel the map panel that has received the mouse clicked event
     * @param node the model node that has been clicked
     */
    public void nodeClicked(MapPanel panel, model.City.Node node);

    /**
     * Click event on the background in a given map panel
     * @param panel the map panel that has received the mouse clicked event
     */
    public void backgroundClicked(MapPanel panel);

}
