package model.City;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import utils.Utils;
import utils.UtilsException;


/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 27/11/13
 * Time: 13:53
 * To change this template use File | Settings | File Templates.
 */
public class Network {

    /** the graph's streets */
    List<Street> streets;

    /** the graph's nodes */
    Map<Integer,Node> nodes;


    /**
     * Constructs an empty graph
     */
    public Network() {
        this.streets = new LinkedList<Street>();
        this.nodes = new HashMap<Integer,Node>();
    }

    /**
     * Gets all streets
     * @return streets list
     */
    public List<Street> getStreets() {
        return streets;
    }

    /**
     * Gets all nodes
     * @return nodes list
     */
    public Map<Integer,Node> getNodes() {
        return nodes;
    }

    /**
     * Generates a list of all arcs
     * @return arcs list
     */
    public List<Arc> getArcs() {
        List<Arc> arcs = new LinkedList<Arc>();

        for (Street i : this.streets) {
            arcs.addAll(i.getArcs());
        }

        return arcs;
    }

    /**
     * Finds a node with a given id
     * @param id node id
     * @return
     *  - null if there is no node with the given id
     *  - the node that has the given id
     */
    public Node findNode(int id) {
        return this.nodes.get(id);
    }

    /**
     * Fins a street with a given name
     * @param name the street name
     * @return
     *  - null if there is no street with the given name
     *  - the street that has the given name
     */
    public Street findStreet(String name) {
        for (Street i : this.streets) {
            if (name.equals(i.getName())) {
                return i;
            }
        }

        return null;
    }

    /**
     * Finds a arcView between two nodes
     * @param from node id
     * @param to node id
     * @return
     *  - null if there is no arcView with the given parameters
     *  - the existing arcView
     */
    public Arc findArc(int from, int to) {
        Node node = this.findNode(from);

        if(node == null) {
            return null;
        }

        return node.findOutgoingTo(to);
    }

    /**
     * Returns the cost of an existing arcView going from a source node to an end node
     * Note : A call to this method with an unexisting arcView will throw a null pointer exception
     * @param source Source node
     * @param target Target node
     * @return The cost of an arcView
     */
    public int getCost(int source, int target) {
        return findArc(source, target).getCost();
    }

    /**
     * Creates a node with given id and position (x,y)
     * @param id the node's id
     * @param x the x position
     * @param y the y position
     * @throws UnsupportedOperationException if node with the given id already exists
     */
    public Node createNode(int id, int x, int y) throws UnsupportedOperationException {
        if (this.findNode(id) != null) {
            throw new UnsupportedOperationException();
        }

        Node node = new Node(id, x, y);

        this.nodes.put(id, node);

        return node;
    }

    /**
     * Creates a street with a given name if not already existing, and returns it
     * @param name the street's name
     * @return the street that has the given name
     */
    public Street createStreet(String name) {
        Street street = this.findStreet(name);

        if (street != null) {
            return street;
        }

        street = new Street(name);

        this.streets.add(street);

        return street;
    }

    /**
     * Loads a node from a given XML element
     * @param xmlElement the node's element
     * @return
     *  - the node that has just been created
     * @throws
     *  - UtilsException if failed with the reason why it has failed in it
     */
    private Node loadNodeFromXml(Element xmlElement) throws UtilsException {
        int id = -1;
        Node node;

        try {
            id = Utils.parseUIntFromXmlAttribute(xmlElement, "id");
            int x = Utils.parseUIntFromXmlAttribute(xmlElement, "x");
            int y = Utils.parseUIntFromXmlAttribute(xmlElement, "y");

            if (this.findNode(id) != null) {
                throw new UtilsException("le noeud '" + id + "' existe deja");
            }

            node = this.createNode(id, x, y);
        }
        catch (UtilsException e) {
            if (id != -1) {
                throw new UtilsException("Element XML " + xmlElement.getNodeName() + " id=\"" + id + "\": " + e);
            }
            else {
                throw new UtilsException("Element XML " + xmlElement.getNodeName() + ": " + e);
            }
        }

        return node;
    }

    /**
     * Loads an arcView from a given XML element
     * @param xmlElement the node's element
     * @param from the leaving node
     * @return
     *  - the arcView that has just been created
     * @throws
     *  - UtilsException if failed with the reason why it has failed in it
     */
    private Arc loadArcFromXml(Element xmlElement, Node from) throws UtilsException {
        String streetName;
        float speed;
        float length;
        Node to;

        try {
            streetName = Utils.stringFromXmlAttribute(xmlElement, "nomRue");
            speed = Utils.parsePositiveFloatFromXmlAttribute(xmlElement, "vitesse");
            length = Utils.parsePositiveFloatFromXmlAttribute(xmlElement, "longueur");
            int destinationId = Utils.parseUIntFromXmlAttribute(xmlElement, "destination");

            if (speed == 0.0) {
                throw new UtilsException("vitesse nule");
            }

            to = this.findNode(destinationId);

            if (to == null) {
                throw new UtilsException("destination inconnue");
            }

            if (from.findOutgoingTo(to) != null) {
                throw new UtilsException("le troncon a distination de '" + destinationId + "' existe deja");
            }

            Arc goingBackArc = to.findOutgoingTo(from);

            if (goingBackArc != null && !goingBackArc.getStreet().getName().equals(streetName)) {
                throw new UtilsException("ne possede pas le meme nom de rue (\"" + streetName + "\") que l'arcView " +
                        to.getId() + " a " + from.getId() + "(\"" + goingBackArc.getStreet().getName() + "\")");
            }
        }
        catch (UtilsException e) {
            throw new UtilsException("Element XML " + xmlElement.getNodeName() + " quitant le noeud \"" + from.getId() + "\": " + e);
        }

        Street street = this.createStreet(streetName);

        return new Arc(street, from, to, length, speed);
    }

    /**
     * Load a network from a given XML element
     * @param xmlElement the network's element
     * @throws
     *  - UtilsException if failed with the reason why it has failed in it
     */
    private void loadNetworkFromXml(Element xmlElement) throws UtilsException {
        NodeList xmlNodeList = xmlElement.getElementsByTagName("Noeud");

        if(xmlNodeList.getLength() == 0) {
            throw new UtilsException("XML vide");
        }

        for (int i = 0; i < xmlNodeList.getLength(); i++) {
            Element xmlNode = (Element) xmlNodeList.item(i);

            this.loadNodeFromXml(xmlNode);
        }

        for (int i = 0; i < xmlNodeList.getLength(); i++) {
            Element xmlNode = (Element) xmlNodeList.item(i);

            String nodeId = xmlNode.getAttribute("id");
            Node from = this.findNode(Integer.parseInt(nodeId));

            NodeList xmlArcList = xmlNode.getElementsByTagName("TronconSortant");

            for (int j = 0; j < xmlArcList.getLength(); j++) {
                Element xmlArc = (Element) xmlArcList.item(j);

                this.loadArcFromXml(xmlArc, from);
            }

        }
    }

    /**
     * Creates a Network from a given XML file
     * @param xmlPath the xml file 's path
     * @return
     *  - the graph that has just been created
     * @throws
     *  - UtilsException if failed with the reason why it has failed in it
     */
    public static Network createFromXml(String xmlPath) throws UtilsException {
        File fileXml = new File(xmlPath);

        Element root;
        Document document;
        DocumentBuilder factory;
        Network network;

        try {
            factory = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            document = factory.parse(fileXml);

            root = document.getDocumentElement();
        } catch (ParserConfigurationException pce) {
            throw new UtilsException("DOM parsor configuration exception");
        } catch (SAXException se) {
            throw new UtilsException("L'étape de parsing XML a échoué.");
        } catch (IOException ioe) {
            throw new UtilsException("Erreur lors de la lecture du fichier \"" + xmlPath + "\".");
        }

        if (!root.getNodeName().equals("Reseau")) {
            throw new UtilsException("Nom de racine inattendu : \"" + root.getNodeName() + "\"");
        }

        network = new Network();
        network.loadNetworkFromXml(root);

        return network;
    }
}
