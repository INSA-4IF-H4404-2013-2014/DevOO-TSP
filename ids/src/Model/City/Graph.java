package Model.City;

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

/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 27/11/13
 * Time: 13:53
 * To change this template use File | Settings | File Templates.
 */
public class Graph {

    /** the graph's streets */
    List<Street> streets;

    /** the graph's nodes */
    Map<Integer,Node> nodes;


    /**
     * Constructs a empty graph
     */
    public Graph() {
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
            if (name == i.getName()) {
                return i;
            }
        }

        return null;
    }

    /**
     * Creates a node with given id and position (x,y)
     * @param id the node's id
     * @param x the x position
     * @param y the y position
     * @throws UnsupportedOperationException if node with the given id already exists
     */
    public Node createNode(int id, int x, int y) throws UnsupportedOperationException{
        if (this.findNode(id) != null) {
            throw new UnsupportedOperationException();
        }

        Node node = new Node(id, x, y);

        this.nodes.put(id, node);

        return node;
    }

    /**
     * Creates a node from a given XML element
     * @param xmlElement the node's element
     * @return
     *  - null if failed
     *  - the node that has just been created
     */
    private Node createNode(Element xmlElement) {
        Node node = Node.createFromXml(xmlElement);

        if (node == null) {
            return null;
        }

        if (this.findNode(node.getId()) != null) {
            return null;
        }

        this.nodes.put(node.getId(), node);

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
     * Loads an arc from a given XML element
     * @param xmlElement the node's element
     * @param from the leaving node
     * @return
     *  - null if failed
     *  - the arc that has just been created
     */
    private Arc loadArc(Element xmlElement, Node from) {
        String streetName = xmlElement.getAttribute("nomRue");

        if (streetName == null) {
            return null;
        }

        String speedString = xmlElement.getAttribute("vitesse");

        if (speedString == null) {
            return null;
        }

        float speed = Float.parseFloat(speedString);

        if (speed <= 0.0) {
            return null;
        }

        String lengthString = xmlElement.getAttribute("longueur");

        if (lengthString == null) {
            return null;
        }

        float length = Float.parseFloat(lengthString);

        if (length < 0.0) {
            return null;
        }

        String destinationString = xmlElement.getAttribute("destination");

        if (destinationString == null) {
            return null;
        }

        int destinationId = Integer.parseInt(destinationString);

        Node to = this.findNode(destinationId);

        if (to == null) {
            return null;
        }

        Street street = this.createStreet(streetName);

        return new Arc(street, from, to, length, speed);
    }

    /**
     * Load a network from a given XML element
     * @param xmlElement the network's element
     * @return
     *  - false if failed
     *  - true if succeed
     */
    private boolean loadXmlNetwork(Element xmlElement)
    {
        NodeList xmlNodeList = xmlElement.getElementsByTagName("Noeud");

        for (int i = 0; i < xmlNodeList.getLength(); i++) {
            Element xmlNode = (Element) xmlNodeList.item(i);

            if (this.createNode(xmlNode) == null) {
                return false;
            }
        }

        for (int i = 0; i < xmlNodeList.getLength(); i++) {
            Element xmlNode = (Element) xmlNodeList.item(i);

            Node from = this.findNode(Integer.parseInt(xmlNode.getAttribute("id")));

            NodeList xmlArcList = xmlNode.getElementsByTagName("TronconSortant");

            for (int j = 0; j < xmlArcList.getLength(); j++) {
                Element xmlArc = (Element) xmlNodeList.item(i);

                if (this.loadArc(xmlArc, from) == null) {
                    return false;
                }
            }

        }

        return true;
    }

    /**
     * Creates a Graph from a given XML file
     * @param xmlPath the xml file 's path
     * @return
     *  - null if failed
     *  - the graph that has just been created
     */
    public static Graph createFromXml(String xmlPath)
    {
        File fileXml = new File(xmlPath);

        Element root;
        Document document;
        DocumentBuilder factory;

        try {
            factory = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            document = factory.parse(fileXml);

            root = document.getDocumentElement();
        } catch (ParserConfigurationException pce) {
            System.out.println("DOM parsor configuration exception");
            return null;
        } catch (SAXException se) {
            System.out.println("Xml parse error");
            return null;
        } catch (IOException ioe) {
            System.out.println("I/O error");
            return null;
        }

        if (!root.getNodeName().equals("Reseau")) {
            System.out.println("Xml parse error");
            return null;
        }

        Graph graph = new Graph();

        if (!graph.loadXmlNetwork(root)) {
            return null;
        }

        return graph;
    }

}
