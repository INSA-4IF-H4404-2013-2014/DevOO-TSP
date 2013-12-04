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

import Utils.Utils;
import Utils.UtilsException;


/**
 * Created with IntelliJ IDEA.
 * User: gabadie
 * Date: 27/11/13
 * Time: 13:53
 * To change this template use File | Settings | File Templates.
 */
public class Graph implements aaaaaaaaaa{

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
                throw new UtilsException("node '" + id + "' already exists");
            }

            node = this.createNode(id, x, y);
        }
        catch (UtilsException e) {
            if (id != -1) {
                throw new UtilsException("failed to parse " + xmlElement.getNodeName() + " id=\"" + id + "\": " + e);
            }
            else {
                throw new UtilsException("failed to parse " + xmlElement.getNodeName() + ": " + e);
            }
        }

        return node;
    }

    /**
     * Loads an arc from a given XML element
     * @param xmlElement the node's element
     * @param from the leaving node
     * @return
     *  - the arc that has just been created
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
            length = Utils.parsePositiveFloatFromXmlAttribute(xmlElement, "length");
            int destinationId = Utils.parseUIntFromXmlAttribute(xmlElement, "destination");

            if (speed == 0.0) {
                throw new UtilsException("null speed");
            }

            to = this.findNode(destinationId);

            if (to == null) {
                throw new UtilsException("unknown node id");
            }
        }
        catch (UtilsException e) {
            throw new UtilsException("failed to parse " + xmlElement.getNodeName() + " leaving node \"" + from.getId() + "\": " + e);
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
     * Creates a Graph from a given XML file
     * @param xmlPath the xml file 's path
     * @return
     *  - the graph that has just been created
     * @throws
     *  - UtilsException if failed with the reason why it has failed in it
     */
    public static Graph createFromXml(String xmlPath) throws UtilsException {
        File fileXml = new File(xmlPath);

        Element root;
        Document document;
        DocumentBuilder factory;
        Graph graph;

        try {
            try {
                factory = DocumentBuilderFactory.newInstance().newDocumentBuilder();

                document = factory.parse(fileXml);

                root = document.getDocumentElement();
            } catch (ParserConfigurationException pce) {
                throw new UtilsException("DOM parsor configuration exception");
            } catch (SAXException se) {
                throw new UtilsException("XML parse stage failed");
            } catch (IOException ioe) {
                throw new UtilsException("Error while reading file \"" + xmlPath + "\"");
            }

            if (!root.getNodeName().equals("Reseau")) {
                throw new UtilsException("Unexpected XML root name \"" + root.getNodeName() + "\"");
            }

            graph = new Graph();
            graph.loadNetworkFromXml(root);
        }
        catch (UtilsException e) {
            throw  new UtilsException("File \"" + xmlPath + "\" > " + e);
        }

        return graph;
    }

    public static void main(String [ ] args) {
        try {
            Graph graph = createFromXml("../sujet/plan10x10.xml");
        }
        catch (UtilsException e) {
            System.out.println(e);
        }

        System.out.println("OK");
    }

}
