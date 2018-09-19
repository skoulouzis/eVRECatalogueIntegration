package gr.forth.ics.isl.exporter;

import gr.forth.ics.isl.exception.GenericException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import org.json.JSONObject;

/**
 *
 * @author S. Koulouzis
 */
public interface CatalogueExporter {

    public void exportAll(String outputPath) throws GenericException;

    public Collection<String> fetchAllDatasetUUIDs() throws MalformedURLException, IOException;

    public JSONObject exportResource(String resourceId) throws MalformedURLException, IOException;

    public String transformToXml(JSONObject jsonObject);

    public void setLimit(int limit);

}
