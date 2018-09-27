/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.wps2wadl;

import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import net.opengis.wps.x100.ProcessDescriptionType;
import org.jvnet.ws.wadl.Application;
import org.jvnet.ws.wadl.Method;
import org.jvnet.ws.wadl.Option;
import org.jvnet.ws.wadl.Param;
import org.jvnet.ws.wadl.ParamStyle;
import org.jvnet.ws.wadl.Request;
import org.jvnet.ws.wadl.Resource;
import org.jvnet.ws.wadl.Resources;

/**
 *
 * @author S. Koulouzis
 */
public class WADLGenerator {

    private final URL wpsURLBase;
    private final String lang;

    public WADLGenerator(URL wpsURLBase, String lang) {
        this.wpsURLBase = wpsURLBase;
        this.lang = lang;
    }

    public String generateExecuteWADL(ProcessDescriptionType processDescription) throws JAXBException {

//        InputDescriptionType[] dataInputs = processDescription.getDataInputs().getInputArray();
//        for(InputDescriptionType input: dataInputs){
//
//        }
        Application application = new Application();
        Resources resources = new Resources();
        resources.setBase(wpsURLBase.toString());

        Resource getCapabilitiesResource = createWebProcessingServiceResources(new String[]{D4scienceWPS.WPS_OPERATION_EXECUTE});
        resources.getResource().add(getCapabilitiesResource);
        application.getResources().add(resources);

        List<Option> identifierOptions = new ArrayList<>();
        Option identifierOption = new Option();
        identifierOption.setValue(processDescription.getIdentifier().getStringValue());
        identifierOptions.add(identifierOption);

        Method execute = createExecuteMethod(identifierOptions);
        application.getResourceTypeOrMethodOrRepresentation().add(execute);

        return toXML(application);

    }

    public String generateBaseWADL(List<Option> identifierOptions) throws JAXBException {
        Application application = new Application();
        Resources resources = new Resources();
        resources.setBase(wpsURLBase.toString());

        Resource getCapabilitiesResource = createWebProcessingServiceResources(D4scienceWPS.WPS_OPERATION_ALL);
        resources.getResource().add(getCapabilitiesResource);
        application.getResources().add(resources);

        Method getCapabilities = createGetCapabilitiesMethod();
        application.getResourceTypeOrMethodOrRepresentation().add(getCapabilities);

        Method describeProcess = createDescribeProcessMethod(identifierOptions);
        application.getResourceTypeOrMethodOrRepresentation().add(describeProcess);

        Method execute = createExecuteMethod(identifierOptions);
        application.getResourceTypeOrMethodOrRepresentation().add(execute);

        return toXML(application);
    }

    private Request createBaseRequest() {

        Request request = new Request();
        Param token = new Param();
        token.setName("gcube-token");
        token.setStyle(ParamStyle.QUERY);
        request.getParam().add(token);

        Param langParam = new Param();
        langParam.setName("lang");
        langParam.setStyle(ParamStyle.QUERY);
        langParam.setFixed(lang);
        request.getParam().add(langParam);

        Param service = new Param();
        service.setName("service");
        service.setStyle(ParamStyle.QUERY);
        service.setFixed("WPS");
        request.getParam().add(service);

        Param version = new Param();
        version.setName("version");
        version.setStyle(ParamStyle.QUERY);
        version.setFixed("1.0.0");
        request.getParam().add(version);
        return request;
    }

    private Method createGetCapabilitiesMethod() {
        Request request = createBaseRequest();
        Method getCapabilities = new Method();
        getCapabilities.setName("GET");
        getCapabilities.setId("GetCapabilities");

        Param requestParam = new Param();
        requestParam.setName("request");
        requestParam.setStyle(ParamStyle.QUERY);
        requestParam.setFixed("GetCapabilities");
        request.getParam().add(requestParam);

        getCapabilities.setRequest(request);

//        Response response = new Response();
//        response.getStatus().add(Long.valueOf(200));
        return getCapabilities;
    }

    private Method createDescribeProcessMethod(Iterable<Option> identifierOptions) {
        Request request = createBaseRequest();
        Method describeProcess = new Method();
        describeProcess.setName("GET");
        describeProcess.setId("DescribeProcess");

        Param requestParam = new Param();
        requestParam.setName("request");
        requestParam.setStyle(ParamStyle.QUERY);
        requestParam.setFixed("DescribeProcess");
        request.getParam().add(requestParam);

        Param identifier = new Param();
        identifier.setName("identifier");
        identifier.setStyle(ParamStyle.QUERY);

        for (Option option : identifierOptions) {
            identifier.getOption().add(option);
        }

        request.getParam().add(identifier);

        describeProcess.setRequest(request);
        return describeProcess;
    }

    private Method createExecuteMethod(List<Option> identifierOptions) {
        Request request = createBaseRequest();
        Method execute = new Method();
        execute.setId("Execute");
        execute.setName("GET");

        Param requestParam = new Param();
        requestParam.setName("request");
        requestParam.setStyle(ParamStyle.QUERY);
        requestParam.setFixed("Execute");
        request.getParam().add(requestParam);

        Param identifier = new Param();
        identifier.setName("identifier");
        identifier.setStyle(ParamStyle.QUERY);
        if (identifierOptions.size() == 1) {
            identifier.setFixed(identifierOptions.get(0).getValue());
        } else {
            for (Option option : identifierOptions) {
                identifier.getOption().add(option);

            }
        }
        request.getParam().add(identifier);

        Param dataInputs = new Param();
        dataInputs.setName("DataInputs");
        dataInputs.setStyle(ParamStyle.QUERY);
        request.getParam().add(dataInputs);

        execute.setRequest(request);
        return execute;
    }

    private Resource createWebProcessingServiceResources(String[] wpsOperations) {

        Resource webProcessingService = new Resource();
        webProcessingService.setPath("WebProcessingService");

        for (String wpsOperation : wpsOperations) {
            Method method = new Method();
            method.setHref("#" + wpsOperation);
            webProcessingService.getMethodOrResource().add(method);
        }

        return webProcessingService;
    }

    private String toXML(Application application) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Application.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
//        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.w3.org/2001/XMLSchema");

        StringWriter out = new StringWriter();
        marshaller.marshal(application, out);
        return out.toString();
    }

}
