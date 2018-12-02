/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.prise.util;

/**
 *
 * @author alogo
 */
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

import org.apache.jackrabbit.webdav.DavConstants;
import org.apache.jackrabbit.webdav.MultiStatus;
import org.apache.jackrabbit.webdav.MultiStatusResponse;
import org.apache.jackrabbit.webdav.property.DavProperty;
import org.apache.jackrabbit.webdav.property.DavPropertySet;

import org.json.JSONArray;
import org.json.JSONObject;
import static org.springframework.http.RequestEntity.method;

public class WebDAVClient {

    private String uri;
    private final HttpClient client;

    public WebDAVClient(String uri) {
        this.uri = uri;
        this.client = new HttpClient();
    }

    public int putFile(File workflowFile, String webdavFolder, String mimetype) throws Exception {
        PutMethod httpMethod = new PutMethod("http://localhost/" + webdavFolder + "/" + workflowFile.getName());
        int statusCode;
        RequestEntity requestEntity = new FileRequestEntity(workflowFile, mimetype);
        httpMethod.setRequestEntity(requestEntity);
        try {
            statusCode = client.executeMethod(httpMethod);
            byte[] responseBody = httpMethod.getResponseBody();

        } finally {
            httpMethod.releaseConnection();
        }
        return statusCode;
    }

}
