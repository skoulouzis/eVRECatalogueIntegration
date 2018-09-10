package gr.forth.ics.isl.common;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class D4ScienceResources {

    /* Endpoint resources */
    public static final String D4SCIENCE_CATALOG_URL = "https://ckan-d4s.d4science.org";
    public static final String[] ALL_RESOURCES_ENDPOINT = new String[]{"/api/action/package_list"};
    public static final String RESOURCE_ENDPOINT = "/api/action/package_show";
    public static final String ID_PARAMETER = "id";

    /* JSON resources */
    public static final String RESULT = "result";

    /* Exported file resources */
    public static final String RESOURCE = "resource";
    public static final String RESOURCE_ELEMENT_OPEN = "<resource>";
    public static final String RESOURCE_ELEMENT_CLOSE = "</resource>";
    public static final String EXTENSION_XML = ".xml";
    public static final String EXTENSION_JSON = ".json";
}
