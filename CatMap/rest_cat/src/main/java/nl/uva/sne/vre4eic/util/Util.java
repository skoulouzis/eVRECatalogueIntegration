/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.util;

import gr.forth.ics.isl.util.XML;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author S. Koulouzis
 */
public class Util {

    private static Map<String, String> CACHE = new HashMap<>();

    public static boolean isCKAN(String url) {
        String type = CACHE.get(url);
        if (type != null && type.equals("CKAN")) {
            return true;
        }
        try {

            if (!url.contains("/api/action/help_show?name=tag_list")) {
                url += "/api/action/help_show?name=tag_list";
            }

            JSONObject json = readJsonFromUrl(url);
            CACHE.put(url, "CKAN");
            return json.getBoolean("success");
        } catch (IOException | JSONException ex) {
            return false;
        }
    }

    public static boolean isCSW(String url) {
        String type = CACHE.get(url);
        if (type != null && type.equals("CSW")) {
            return true;
        }
        try {
            if (!url.contains("/csw?REQUEST=GetCapabilities&SERVICE=CSW&VERSION=2.0.2&constraintLanguage=CQL_TEXT&constraint_language_version=1.1.0")) {
                url += "/csw?REQUEST=GetCapabilities&SERVICE=CSW&VERSION=2.0.2&constraintLanguage=CQL_TEXT&constraint_language_version=1.1.0";
            }

            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            fac.setNamespaceAware(true);
            Document doc = fac.newDocumentBuilder().parse(new URL(url).openStream());

            Node capabilities = XML.getNode(doc.getFirstChild(), "Capabilities");
            if (capabilities == null) {
                return false;
            }
        } catch (SAXException | ParserConfigurationException | IOException ex) {
            return false;
        }
        CACHE.put(url, "CSW");
        return true;
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        JSONObject json = new JSONObject(readPage(url));
        return json;
    }

    public static String readPage(String url) throws MalformedURLException, IOException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }

    }

}
