/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.wps2wadl;

import com.sun.codemodel.JClassAlreadyExistsException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.util.JAXBResult;
import javax.xml.namespace.QName;
import net.opengis.wps.x100.CapabilitiesDocument;
import net.opengis.wps.x100.ProcessBriefType;
import net.opengis.wps.x100.WPSCapabilitiesType;
import org.jvnet.ws.wadl.Application;
import org.jvnet.ws.wadl.Method;
import org.jvnet.ws.wadl.Option;
import org.jvnet.ws.wadl.Param;
import org.jvnet.ws.wadl.ParamStyle;
import org.jvnet.ws.wadl.Request;
import org.jvnet.ws.wadl.Resource;
import org.jvnet.ws.wadl.Resources;
import org.jvnet.ws.wadl.ast.ApplicationNode;
import org.jvnet.ws.wadl.ast.InvalidWADLException;
import org.jvnet.ws.wadl.ast.ResourceNode;
import org.jvnet.ws.wadl.ast.WadlAstBuilder;
import org.jvnet.ws.wadl.util.MessageListener;
import org.jvnet.ws.wadl2java.Wadl2Java;
import org.n52.wps.client.WPSClientException;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.ext.Locator2Impl;

/**
 *
 * @author S. Koulouzis
 */
public class Main {

    public static void main(String args[]) throws IOException, JAXBException, JClassAlreadyExistsException {
        D4scienceWPS wps = D4scienceWPS.getInstance();
        String TOKEEN = null;
        URL wpsURLBase = new URL("http://dataminer-prototypes.d4science.org/wps/");
//                    wps.connect(wpsURLBase.toString()+"/WebProcessingService?gcube-token=" + TOKEEN + "&");
//            WPSCapabilitiesType capabilities = wps.getWPSCapabilities(url).getCapabilities();
//
//            System.err.println(capabilities.getLang());
//            System.err.println(capabilities.getLanguages());
//            ProcessBriefType[] processes = capabilities.getProcessOfferings().getProcessArray();
//            for (ProcessBriefType proc : processes) {
//                System.err.println(proc);
//            }
        ArrayList<Option> identifierOptions = new ArrayList<>();
        Option option = new Option();
        option.setValue("org.gcube.dataanalysis.wps.statisticalmanager.synchserver.mappedclasses.transducerers.ARGO_FEBRUARY");
        identifierOptions.add(option);

        option = new Option();
        option.setValue("org.gcube.dataanalysis.wps.statisticalmanager.synchserver.mappedclasses.transducerers.ARGO_DATA_CONVERSION_SUITE");
        identifierOptions.add(option);

        Converter converter = new Converter(wpsURLBase, identifierOptions);
        String wadl = converter.convert();
        System.err.println(wadl);
    }

}
