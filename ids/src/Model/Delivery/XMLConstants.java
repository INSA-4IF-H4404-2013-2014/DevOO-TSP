package Model.Delivery;

/**
 * Created with IntelliJ IDEA.
 * User: Deeper
 * Date: 05/12/13
 * Time: 22:23
 * To change this template use File | Settings | File Templates.
 */
public class XMLConstants {
    public static final String DELIVERY_ROOT_ELEMENT = "JourneeType";

    public static final String DELIVERY_WAREHOUSE_ELEMENT = "Entrepot";

    public static final String DELIVERY_WAREHOUSE_NODE_ATTR = "adresse";

    public static final String DELIVERY_SCHEDULES_ELEMENT = "PlagesHoraires";

    public static final String DELIVERY_SCHEDULE_ELEMENT = "Plage";

    public static final String DELIVERY_SCHEDULE_FIELDS_SEPARATOR = ":";

    public static final String DELIVERY_SCHEDULE_EARLIEST_ATTR = "heureDebut";

    public static final String DELIVERY_SCHEDULE_LATEST_ATTR = "heureFin";

    public static final String DELIVERY_DELIVERIES_ELEMENT = "Livraisons";

    public static final String DELIVERY_DELIVERY_ELEMENT = "Livraison";

    public static final String DELIVERY_DELIVERY_ID_ATTR = "id";

    public static final String DELIVERY_DELIVERY_CLIENT_ATTR = "client";

    public static final String DELIVERY_DELIVERY_NODE_ATTR = "adresse";
}
