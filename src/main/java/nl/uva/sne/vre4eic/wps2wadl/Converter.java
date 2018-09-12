/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.wps2wadl;

import java.io.StringWriter;
import java.net.URL;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
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
public class Converter {

    private final URL wpsURLBase;
    private final Iterable<Option> identifierOptions;

    public Converter(URL wpsURLBase, Iterable<Option> identifierOptions) {
        this.wpsURLBase = wpsURLBase;
        this.identifierOptions = identifierOptions;
    }

    public String convert() throws JAXBException {
        Application application = new Application();
        Resources resources = new Resources();
        resources.setBase(wpsURLBase.toString());

        Resource getCapabilitiesResource = createWebProcessingServiceResources();
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

        Param lang = new Param();
        lang.setName("lang");
        lang.setStyle(ParamStyle.QUERY);
        lang.setFixed("en-US");
        request.getParam().add(lang);

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

    private Method createExecuteMethod(Iterable<Option> identifierOptions) {
        Request request = createBaseRequest();
        Method execute = new Method();
        execute.setName("GET");
        execute.setId("Execute");

        Param requestParam = new Param();
        requestParam.setName("request");
        requestParam.setStyle(ParamStyle.QUERY);
        requestParam.setFixed("Execute");
        request.getParam().add(requestParam);

        Param identifier = new Param();
        identifier.setName("identifier");
        identifier.setStyle(ParamStyle.QUERY);

        for (Option option : identifierOptions) {
            identifier.getOption().add(option);
        }
        request.getParam().add(identifier);

        Param dataInputs = new Param();
        dataInputs.setName("DataInputs");
        dataInputs.setStyle(ParamStyle.QUERY);
        request.getParam().add(dataInputs);

        execute.setRequest(request);
        return execute;
    }

    private Resource createWebProcessingServiceResources() {

        Resource webProcessingService = new Resource();
        webProcessingService.setPath("WebProcessingService");

        Method getCapabilities = new Method();
        getCapabilities.setHref("#GetCapabilities");
        webProcessingService.getMethodOrResource().add(getCapabilities);

        Method describeProcessResource = new Method();
        describeProcessResource.setHref("#DescribeProcessResource");
        webProcessingService.getMethodOrResource().add(describeProcessResource);

        Method executeResource = new Method();
        executeResource.setHref("#Execute");
        webProcessingService.getMethodOrResource().add(executeResource);

        return webProcessingService;
    }

    private String toXML(Application application) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Application.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter out = new StringWriter();
        marshaller.marshal(application, out);
        return out.toString();
    }

}
