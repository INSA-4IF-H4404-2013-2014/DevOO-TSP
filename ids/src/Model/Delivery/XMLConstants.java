package model.Delivery;

/**
 * Created with IntelliJ IDEA.
 * User: Deeper
 * Date: 05/12/13
 * Time: 22:23
 * To change this template use File | Settings | File Templates.
 * XML Constants from the XML Round file
 */
public class XMLConstants {
    /** Root element */
    public static final String DELIVERY_ROOT_ELEMENT = "JourneeType";

    /* Warehouse element */
    public static final String DELIVERY_WAREHOUSE_ELEMENT = "Entrepot";

    /** Warehouse address attribute */
    public static final String DELIVERY_WAREHOUSE_NODE_ATTR = "adresse";

    /** Schedules element */
    public static final String DELIVERY_SCHEDULES_ELEMENT = "PlagesHoraires";

    /** Schedule element */
    public static final String DELIVERY_SCHEDULE_ELEMENT = "Plage";

    /** Separator used in the description of the time in a day by the earliest and latest bound attributes of a Schedule element */
    public static final String DELIVERY_SCHEDULE_FIELDS_SEPARATOR = ":";

    /** Earliest bound attribute of a Schedule element */
    public static final String DELIVERY_SCHEDULE_EARLIEST_ATTR = "heureDebut";

    /** Latest bound attribute of a Schedule element */
    public static final String DELIVERY_SCHEDULE_LATEST_ATTR = "heureFin";

    /** Deliveries element */
    public static final String DELIVERY_DELIVERIES_ELEMENT = "Livraisons";

    /** Delivery element */
    public static final String DELIVERY_DELIVERY_ELEMENT = "Livraison";

    /** Delivery's ID attribute */
    public static final String DELIVERY_DELIVERY_ID_ATTR = "id";

    /** Delivery's client's ID attribute */
    public static final String DELIVERY_DELIVERY_CLIENT_ATTR = "client";

    /** Delivery's address (node's ID) attribute */
    public static final String DELIVERY_DELIVERY_NODE_ATTR = "adresse";
}
