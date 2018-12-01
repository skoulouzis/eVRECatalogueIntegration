/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.prise.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author S. Koulouzis
 */
public class Util {

    public static File convert(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + File.separator + file.getOriginalFilename());
        convFile.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

    public static boolean urlExists(String URLName) {
        try {
            HttpURLConnection.setFollowRedirects(true);
            //        HttpURLConnection.setInstanceFollowRedirects(false)
            HttpURLConnection con
                    = (HttpURLConnection) new URL(URLName).openConnection();
            con.setInstanceFollowRedirects(true);
            con.setRequestMethod("HEAD");

            int code = con.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                if (code == HttpURLConnection.HTTP_MOVED_TEMP
                        || code == HttpURLConnection.HTTP_MOVED_PERM
                        || code == HttpURLConnection.HTTP_SEE_OTHER) {
                    String newUrl = con.getHeaderField("Location");

                    // get the cookie if need, for login
                    String cookies = con.getHeaderField("Set-Cookie");
                    con = (HttpURLConnection) new URL(newUrl).openConnection();
                    con.setRequestProperty("Cookie", cookies);
                    code = con.getResponseCode();
                }
            }

            return (code == HttpURLConnection.HTTP_OK);
        } catch (MalformedURLException ex) {
            return false;
        } catch (IOException ex) {
            return false;
        }
    }

}
