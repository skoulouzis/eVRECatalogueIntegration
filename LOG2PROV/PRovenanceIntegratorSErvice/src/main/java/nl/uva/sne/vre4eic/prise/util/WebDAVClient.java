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
import java.io.File;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

public class WebDAVClient {

    private final String uri;
    private final HttpClient client;

    public WebDAVClient(String uri) {
        if (!uri.endsWith("/")) {
            uri += "/";
        }
        this.uri = uri;

        this.client = new HttpClient();
    }

    public int putFile(File workflowFile, String webdavFolder, String mimetype) throws Exception {
        PutMethod httpMethod = new PutMethod(uri + webdavFolder + "/" + workflowFile.getName());
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
