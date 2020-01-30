package eu.vre4eic.eVRETaverna.ui.serviceprovider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.Icon;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.jvnet.ws.wadl.Application;
import org.jvnet.ws.wadl.Param;
import org.jvnet.ws.wadl.ast.ApplicationNode;
import org.jvnet.ws.wadl.ast.MethodNode;
import org.jvnet.ws.wadl.ast.PathSegment;
import org.jvnet.ws.wadl.ast.RepresentationNode;
import org.jvnet.ws.wadl.ast.ResourceNode;
import org.jvnet.ws.wadl.ast.WadlAstBuilder;
import org.jvnet.ws.wadl.util.MessageListener;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import net.sf.taverna.t2.activities.rest.RESTActivityConfigurationBean;
import net.sf.taverna.t2.activities.rest.RESTActivity.HTTP_METHOD;
import net.sf.taverna.t2.servicedescriptions.AbstractConfigurableServiceProvider;
import net.sf.taverna.t2.servicedescriptions.ConfigurableServiceProvider;
import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import eu.vre4eic.eVRETaverna.util.Common;
import java.beans.MethodDescriptor;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import net.opengis.wps.x100.ProcessBriefType;
import net.opengis.wps.x100.WPSCapabilitiesType;
import org.apache.taverna.gis.client.GisClientFactory;
import org.apache.taverna.gis.client.IGisClient;
import org.apache.taverna.gis.client.IPortDataDescriptor;
import org.apache.taverna.gis.client.impl.D4scienceWPSClientSession;
import org.apache.taverna.gis.client.impl.GisClientNorthImpl;
import org.apache.taverna.gis.ui.serviceprovider.GisServiceDesc;
import org.apache.taverna.gis.ui.serviceprovider.GisServiceProvider;
import org.apache.taverna.gis.ui.serviceprovider.GisServiceProviderConfig;
import org.jvnet.ws.wadl.Option;
import org.jvnet.ws.wadl.ast.InvalidWADLException;
import org.n52.wps.client.WPSClientException;

public class EvreServiceProvider extends AbstractConfigurableServiceProvider<VRE4EICServiceProviderConfig>
        implements ConfigurableServiceProvider<VRE4EICServiceProviderConfig> {

    private Logger logger = Logger.getLogger(EvreServiceProvider.class);
    private String token = "";
    private List<ServiceDescription> catalogResults = new ArrayList<>();

    public EvreServiceProvider() {

        super(new VRE4EICServiceProviderConfig());
        //catalogResults.clear();

    }

    private static final URI providerId = URI.create("http://v4e-lab.isti.cnr.it:8080/service-provider/eVRETaverna");

    @SuppressWarnings("unchecked")
    public void findServiceDescriptionsAsync(final FindServiceDescriptionsCallBack callBack) {
        // Use callback.status() for long-running searches
        // callBack.status("Resolving example services");

        // final SchemaCompiler s2j = XJC.createSchemaCompiler();
        //final Set<URI> jsonSchemas = new LinkedHashSet<URI>();
        WadlAstBuilder astBuilder = new WadlAstBuilder(new WadlAstBuilder.SchemaCallback() {

            public void processSchema(InputSource input) {

                /*
				 * // Assume that the stream is a buffered stream at this point // and mark a
				 * position InputStream is = input.getByteStream(); is.mark(8192);
				 * 
				 * // Read the first bytes and look for the xml header // String peakContent =
				 * null;
				 * 
				 * try { Reader r = new InputStreamReader(is, "UTF-8");
				 * 
				 * CharBuffer cb = CharBuffer.allocate(20); r.read(cb); cb.flip(); peakContent =
				 * cb.toString(); } catch (IOException e) { throw new
				 * RuntimeException("Internal problem pushing back buffer", e); } finally { try
				 * { is.reset(); } catch (IOException ex) { throw new
				 * RuntimeException("Internal problem pushing back buffer", ex); }
				 * 
				 * }
				 * 
				 * // By default assume a xml schema, better guess // because some XML files
				 * don't start with <?xml // as per bug WADL-66 if
				 * (peakContent.matches("^\\s*\\{")) { // We are guessing this is a json type
				 * jsonSchemas.add(URI.create(input.getSystemId())); } else { //if
				 * (peakContent==null || peakContent.contains("<?xml") ||
				 * peakContent.startsWith("<")) { s2j.parseSchema(input); }
                 */
            }

            public void processSchema(String uri, Element node) {
                /* s2j.parseSchema(uri, node); */
            }
        }, new MessageListener() {

            @Override
            public void warning(String message, Throwable throwable) {
                logger.warn(message, throwable);
            }

            @Override
            public void info(String message) {
                logger.info(message);
            }

            @Override
            public void error(String message, Throwable throwable) {
                logger.error(message, throwable);
            }
        }) {
            @Override
            protected Application processDescription(URI desc) throws JAXBException, IOException {
                URLConnection conn = desc.toURL().openConnection();
                conn.setRequestProperty("Accept", "application/vnd.sun.wadl+xml");
                conn.setRequestProperty("Accept", "application/xml");

                System.out.println("Message 1" + desc);
                InputStream is = conn.getInputStream();
                System.out.println("Message 2" + desc + " " + is.toString());
                return processDescription(desc, is);
            }
        };

        callBack.status("Resolving e-VRE services");
        String uNm = getConfiguration().getUserName();
        String uPw = getConfiguration().getPassword();

        String evreUrl = getConfiguration().getUri().toASCIIString();

        URL loginUrl;
        URL wsDescUrl;

        try {
            //token=Common.getToken();

            Common.setUserId(uNm);
            Common.setPassword(uPw);
            Common.setEvreUrl(new URL("http://v4e-lab.isti.cnr.it:8080"));

            loginUrl = Common.getLoginUrl();

            callBack.partialResults(catalogResults);
            HttpURLConnection loginCon = (HttpURLConnection) loginUrl.openConnection();
            loginCon.setRequestMethod("GET");
            loginCon.setDoInput(true);
            loginCon.setDoOutput(true);
            InputStream iSt = loginCon.getInputStream();
            BufferedReader bRead = new BufferedReader(new InputStreamReader(iSt, "UTF-8"));
            String line = "";
            JsonObject ob = new JsonObject();
            while ((line = bRead.readLine()) != null) {
                ob = (JsonObject) new JsonParser().parse(line);
                token = ob.get("token").toString();
                System.out.println(token);
            }
            Common.setToken(token.replaceAll("\"", "").trim());

            //get service descriptions references
            //wsDescUrl=new URL("http://v4e-lab.isti.cnr.it:8080/WorkflowService/wfservice/getserviced?evresid="+uNm+"&token="+token.replaceAll("\"", "").trim());
            wsDescUrl = new URL(evreUrl + "/WorkflowService/wfservice/getserviced?evresid=" + uNm + "&token=" + token.replaceAll("\"", "").trim());
            HttpURLConnection descCon = (HttpURLConnection) wsDescUrl.openConnection();
            descCon.setRequestMethod("GET");
            descCon.setDoInput(true);
            descCon.setDoOutput(true);
            InputStream iStDesc = descCon.getInputStream();
            BufferedReader bReadDesc = new BufferedReader(new InputStreamReader(iStDesc, "UTF-8"));
            //System.out.println("fava");
            line = "";
            ob = new JsonObject();
            JsonArray jA = new JsonArray();
            while ((line = bReadDesc.readLine()) != null) {
                ob = (JsonObject) new JsonParser().parse(line);
                String status = ob.get("status").toString();
                if (status.equalsIgnoreCase("\"SUCCEED\"")) {
                    jA = ob.getAsJsonObject("message").getAsJsonArray("services");

                    System.out.println(jA.toString());
                }

            }

            JsonObject wpsWADL = (JsonObject) new JsonParser().parse("{\"ref\": \"file:///" + System.getProperty("user.home") + "/Downloads/application2.wadl.xml\",\"name\": \"WPS\"}");
            jA.add(wpsWADL);
            if (jA.size() > 0) {
                Iterator<JsonElement> itjA = jA.iterator();
                List<ResourceNode> rs;
                while (itjA.hasNext()) {
                    ob = (JsonObject) itjA.next();
                    System.out.println(ob.get("ref").toString());
                    this.getConfiguration().setUri(new URI(ob.get("ref").toString().replaceAll("\"", "").trim()));
                    try {
                        URI serviceURI = this.getConfiguration().getUri();
                        System.out.println(serviceURI);
                        ApplicationNode an = astBuilder.buildAst(new URI(serviceURI.toASCIIString()));
                        rs = an.getResources();
                        Iterator<ResourceNode> it = rs.iterator();
                        boolean isWPSService = false;
                        String wpsBaseURL = null;
                        ResourceNode rn = it.next();
                        wpsBaseURL = rn.getUriTemplate();
                        if (wpsBaseURL.startsWith("http") && wpsBaseURL.contains("wps")) {
                            isWPSService = true;
                            List<ResourceNode> children = rn.getChildResources();
                            for (ResourceNode child : children) {
                                if (child.getUriTemplate().equals("WebProcessingService")) {
                                    break;
                                }
                                isWPSService = false;
                            }
                        }

                        if (!isWPSService) {
                            for (ResourceNode r : rs) {
                                processEndpointClass(catalogResults, r, ob.get("name").toString().replaceAll("\"", "").trim(), uNm);
                            }
                        } else {
                            createWPSService(catalogResults, wpsBaseURL);
                        }

                        callBack.partialResults(catalogResults);
                    } catch (IOException | URISyntaxException | InvalidWADLException exc) {
                    } catch (WPSClientException ex) {
                        java.util.logging.Logger.getLogger(EvreServiceProvider.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        } catch (JsonSyntaxException | IOException | URISyntaxException e1) {
            // TODO Auto-generatnew JsonParser().parsenew JsonParser().parseed catch block
            e1.printStackTrace();
        }

        /*		for (int i = 1; i <= getConfiguration().getNumberOfServices(); i++) {
			System.out.println("-------Example " + i);
			ExampleServiceDesc service = new ExampleServiceDesc();
			// Populate the service description bean

			service.setExampleString("VRE4EIC WS " + i);

			service.setExampleUri(getConfiguration().getUri());
			// Optional: set description
			service.setDescription("VRE4EIC Web Service " + i);
			evreResults.add(service);

		}

		
		callBack.partialResults(evreResults);*/

 /*List<WadlServiceDesc> results = new ArrayList<WadlServiceDesc>();

		try {
			callBack.status("Resolving EPOS services");
			for (int i = 0; i < wadlURLString.length; i++) {
				String tu = this.getConfiguration().getUri().toASCIIString();
				this.getConfiguration().setUri(new URI(wadlURLString[i]));
				ApplicationNode an = astBuilder.buildAst(new URI(this.getConfiguration().getUri().toASCIIString()));
				List<ResourceNode> rs = an.getResources();
				for (ResourceNode r : rs) {
					processEndpointClass(results, r);
				}
			}

		} catch (InvalidWADLException | IOException | URISyntaxException e) {
			logger.error(e);
		}*/
        // partialResults() can also be called several times from inside
        // for-loop if the full search takes a long time
        callBack.partialResults(catalogResults);

        // No more results will be coming
        //callBack.finished();
    }

    private void processEndpointClass(List<ServiceDescription> results, ResourceNode root, String name, String userNm) {
        for (ResourceNode r : root.getChildResources()) {

            processSubClass(results, r, name, userNm);
        }
    }

    private void processSubClass(List<ServiceDescription> results, ResourceNode resource, String name, String userNm) {
        // generate Java methods for each resource method
        for (MethodNode m : resource.getMethods()) {

            processMethodDecls(results, m, name, userNm);
        }

        // generate sub classes for each child resource
        for (ResourceNode r : resource.getChildResources()) {

            processSubClass(results, r, name, userNm);
        }
    }

    private void processMethodDecls(List<ServiceDescription> results, MethodNode method, String name, String userNm) {
        RESTActivityConfigurationBean serviceConfigBean = RESTActivityConfigurationBean.getDefaultInstance();

        HTTP_METHOD httpMethod = HTTP_METHOD.GET;
        String methodName = method.getName();
        switch (methodName) {
            case "GET":
                httpMethod = HTTP_METHOD.GET;
                break;
            case "POST":
                httpMethod = HTTP_METHOD.POST;
                break;
            case "PUT":
                httpMethod = HTTP_METHOD.PUT;
                break;
            case "DELETE":
                httpMethod = HTTP_METHOD.DELETE;
                break;
            default:
                // This means that valid HTTP methods such as OPTIONS are ignored
                return;
        }

        serviceConfigBean.setHttpMethod(httpMethod);

        StringBuilder sb = new StringBuilder();

//        WebProcessingService
        ResourceNode parent = method.getOwningResource();
        String uriTemplate = parent.getUriTemplate();
        System.err.println(uriTemplate);
        ResourceNode pParent = parent.getParentResource();
        uriTemplate = pParent.getUriTemplate();
        System.err.println(uriTemplate);

        List<PathSegment> segments = method.getOwningResource().getPathSegments();
        List<String> pathSegments = new ArrayList<>();
        for (int segement = 0; segement < segments.size(); segement++) {
            String pathSegment = segments.get(segement).getTemplate();
            if (pathSegment.contains("{") && pathSegment.contains(":")) {
                String firstPart = pathSegment.substring(0, pathSegment.indexOf(":"));
                String secondPart = pathSegment.substring(pathSegment.indexOf("}"));
                pathSegment = firstPart + secondPart;
            }
            boolean bufferEndsInSlash = sb.length() > 0 ? sb.charAt(sb.length() - 1) == '/' : false;
            boolean pathSegmentStartsWithSlash = pathSegment.startsWith("/");

            if (pathSegmentStartsWithSlash && bufferEndsInSlash) {
                // We only need the one slash, so remove one from the
                // pathSegement
                sb.append(pathSegment, 1, pathSegment.length());
            } else if (pathSegmentStartsWithSlash || bufferEndsInSlash || (segement == 0)) {
                // Only one has a slash so it is fine to append
                sb.append(pathSegment);
            } else {
                // Neither has one so add a slash in
                sb.append('/');
                sb.append(pathSegment);
            }
            if ("/".equals(pathSegment)) {
                pathSegments.add(pathSegment);
            } else {
                if (pathSegment.startsWith("/")) {
                    pathSegments.add(pathSegment.substring(1));
                } else {
                    pathSegments.add(pathSegment);
                }
            }
        }

        if (!method.getQueryParameters().isEmpty()) {
            boolean first = true;
            for (Param p : method.getQueryParameters()) {
                if (first) {
                    first = false;
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                String fixed = p.getFixed();
                String paramName = p.getName();
                if (fixed != null) {
                    sb.append(paramName).append("=").append(fixed);
                } else {
                    sb.append(paramName).append("={").append(paramName).append("}");
                }

            }
        }

        serviceConfigBean.setUrlSignature(sb.toString());

        List<RepresentationNode> supportedInputs = method.getSupportedInputs();
        List<RepresentationNode> supportedOutputs = new ArrayList<>();
        for (List<RepresentationNode> nodeList : method.getSupportedOutputs().values()) {
            for (RepresentationNode node : nodeList) {
                supportedOutputs.add(node);
            }
        }

        if (!supportedInputs.isEmpty()) {
            serviceConfigBean.setContentTypeForUpdates(supportedInputs.get(0).getMediaType());
        }

        if (!supportedOutputs.isEmpty()) {
            serviceConfigBean.setAcceptsHeaderValue(supportedOutputs.get(0).getMediaType());
        }

        // TODO Something about the headers
        WadlServiceDesc newServiceDesc = new WadlServiceDesc(this.getConfiguration().getUri().toASCIIString(),
                pathSegments, serviceConfigBean, name, userNm.trim());

        results.add(newServiceDesc);

    }

    /**
     * Icon for service provider
     */
    public Icon getIcon() {
        return ExampleServiceIcon.getIcon();
    }

    /**
     * Name of service provider, appears in right click for 'Remove service
     * provider'
     */
    public String getName() {
        return "VRE4EIC WS Resources";
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getId() {
        return providerId.toASCIIString();
    }

    @Override
    protected List<? extends Object> getIdentifyingData() {
        return Arrays.asList(getConfiguration().getUri());
    }

    private void createWPSService(List<ServiceDescription> results, String wpsServiceURL) throws WPSClientException, UnsupportedEncodingException, MalformedURLException, IOException {
        String d4Science_token = new String(Files.readAllBytes(Paths.get(System.getProperty("user.home") + File.separator + ".d4Science_token")), "UTF-8");

        String wpsURL = wpsServiceURL + "/WebProcessingService?gcube-token=" + d4Science_token.replaceAll("\n", "").trim() + "&";

        D4scienceWPSClientSession wps = D4scienceWPSClientSession.getInstance();
        wps.connect(wpsURL);
        WPSCapabilitiesType capabilities = wps.getWPSCaps(wpsURL).getCapabilities();

        ProcessBriefType[] processes = capabilities.getProcessOfferings().getProcessArray();
        ArrayList<String> processIdentifiers = new ArrayList<>();
        for (ProcessBriefType proc : processes) {
            processIdentifiers.add(proc.getIdentifier().getStringValue());
        }
        GisServiceProviderConfig gisConf = new GisServiceProviderConfig(wpsURL, processIdentifiers);

        IGisClient gisServiceClient = GisClientFactory.getInstance().getGisClient(gisConf.getOgcServiceUri().toASCIIString());
        for (String processID : processIdentifiers) {
            GisServiceDesc service = new GisServiceDesc();

            // Populate the service description bean
            service.setOgcServiceUri(gisConf.getOgcServiceUri());
            service.setProcessIdentifier(processID);

            // TODO: Optional: set description (Set a better description)
            if (gisServiceClient instanceof GisClientNorthImpl) {
                service.setDescription(((GisClientNorthImpl) gisServiceClient).getProcessDescription(processID));
            } else {
                service.setDescription(processID);
            }

            // Get input ports
            List<IPortDataDescriptor> inputList = gisServiceClient.getTaverna2InputPorts(processID);

            service.setInputPortDefinitions(inputList);

            // Get output ports
            List<IPortDataDescriptor> outputList = gisServiceClient.getTaverna2OutputPorts(processID);

            service.setOutputPortDefinitions(outputList);

            results.add(service);

//            // partialResults() can also be called several times from inside
//            // for-loop if the full search takes a long time
//            callBack.partialResults(results);
        }

    }

}
