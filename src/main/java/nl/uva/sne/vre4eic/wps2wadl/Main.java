/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.wps2wadl;

import com.sun.codemodel.JClassAlreadyExistsException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import net.opengis.wps.x100.ProcessBriefType;
import net.opengis.wps.x100.ProcessDescriptionType;
import net.opengis.wps.x100.WPSCapabilitiesType;
import org.jvnet.ws.wadl.Option;
import org.n52.wps.client.WPSClientException;

/**
 *
 * @author S. Koulouzis
 */
public class Main {

    public static void main(String args[]) throws IOException, JAXBException, JClassAlreadyExistsException {
        try {
            D4scienceWPS wps = D4scienceWPS.getInstance();
            String TOKEEN = "bfdc8842-4dd3-4553-8437-7c38b9d128b9-843339462";
            URL wpsURLBase = new URL("http://dataminer-prototypes.d4science.org/wps/");
            String wpsURL = wpsURLBase.toString() + "/WebProcessingService?gcube-token=" + TOKEEN + "&";

            wps.connect(wpsURL);
            WPSCapabilitiesType capabilities = wps.getWPSCapabilities(wpsURL).getCapabilities();
//
            String lang = capabilities.getLang();
//            System.err.println(capabilities.getLanguages());
            ProcessBriefType[] processes = capabilities.getProcessOfferings().getProcessArray();
            ArrayList<Option> identifierOptions = new ArrayList<>();
            for (ProcessBriefType proc : processes) {
                Option option = new Option();
                option.setValue(proc.getIdentifier().getStringValue());
                identifierOptions.add(option);
            }

            WADLGenerator wadlGen = new WADLGenerator(wpsURLBase, lang);
            String wadl = wadlGen.generateBaseWADL(identifierOptions);
//            System.err.println(wadl);

            String processID = "org.gcube.dataanalysis.wps.statisticalmanager.synchserver.mappedclasses.transducerers.CATCHES_AGGREGATED_FOLLOWING_A_SELECT_VARIABLE";
            ProcessDescriptionType processDescription = wps.getProcessDescription(wpsURL, processID);
            wadl = wadlGen.generateExecuteWADL(processDescription);
            System.err.println(wadl);
        } catch (WPSClientException | UnsupportedEncodingException | MalformedURLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
