package ca.bargenson.http.asyncserver.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import ca.bargenson.http.asyncserver.HttpRequest;

/**
 * Created with IntelliJ IDEA.
 * User: bargenson
 * Date: 2014-06-15
 * Time: 3:15 AM
 */
public abstract class RestResource<T> {

    protected abstract T renderResource(HttpRequest request);

    public String renderResourceAsJson(HttpRequest request) {
        final T resource = renderResource(request);
        try {
            ObjectMapper mapper = new ObjectMapper();
            JaxbAnnotationModule module = new JaxbAnnotationModule();
            mapper.registerModule(module);
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(resource);
        } catch (JsonProcessingException e) {
            throw new JsonRepresentationException("Error generating Json for " + resource, e);
        }
    }

    public String renderResourceAsXml(HttpRequest request) {
        final T resource = renderResource(request);
        try {
            XmlMapper mapper = new XmlMapper();
            JaxbAnnotationModule module = new JaxbAnnotationModule();
            mapper.registerModule(module);
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(resource);
        } catch (JsonProcessingException e) {
            throw new XmlRepresentationException("Error generating XML for " + resource, e);
        }
    }

    public String renderResourceAsTxt(HttpRequest httpRequest) {
        return String.valueOf(renderResource(httpRequest));
    }

}
