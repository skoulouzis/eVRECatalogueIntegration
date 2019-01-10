package nl.uva.sne.vre4eic.prise.service;

import nl.uva.sne.vre4eic.data.Service;
import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ProvParser {
    Model model;

    public ProvParser(File inputProv, ArrayList<Service> services) throws FileNotFoundException {
        try {
            parseProv(new FileInputStream(inputProv), services);
        } catch (FileNotFoundException e) {
            throw e;
        }
    }

    public void parseProv(FileInputStream inputFile, ArrayList<Service> services){
        model = ModelFactory.createDefaultModel();
        model.read(inputFile, null,"TTL");

        for(Service s : services){
            Query query = QueryFactory.create(getQueryString(s.getName()));
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet rs = qe.execSelect();

            while(rs.hasNext()){
                QuerySolution sol = rs.nextSolution();

                XSDDateTime startTime = (XSDDateTime) sol.get("startTime").asLiteral().getValue();
                XSDDateTime endTime = (XSDDateTime) sol.get("endTime").asLiteral().getValue();

                s.setStartTime(startTime.asCalendar().getTime());
                s.setEndTime(startTime.asCalendar().getTime());
            }
        }
    }

    String getQueryString(String serviceName){
        return  "PREFIX prov: <http://www.w3.org/ns/prov#> " +
                "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#> " +
                "SELECT ?startTime ?endTime " +
                "WHERE { " +
                    "?y  rdfs:label  \"Processor execution " + serviceName + "\"@en . "+
                    "?y  prov:startedAtTime  ?startTime . "+
                    "?y  prov:endedAtTime  ?endTime . " +
                "}";

    }
}
