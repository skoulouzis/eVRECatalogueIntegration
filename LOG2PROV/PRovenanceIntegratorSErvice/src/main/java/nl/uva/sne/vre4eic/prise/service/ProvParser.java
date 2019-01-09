package nl.uva.sne.vre4eic.prise.service;

import nl.uva.sne.vre4eic.data.Service;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ProvParser {
    ArrayList<Service> services;
    Model model;

    public ProvParser(File inputProv, ArrayList<Service> services) throws FileNotFoundException {
        this.services = services;

        try {
            parseProv(new FileInputStream(inputProv));
        } catch (FileNotFoundException e) {
            throw e;
        }
    }

    public void parseProv(FileInputStream inputFile){
        model = ModelFactory.createDefaultModel();
        model.read(inputFile, null,"TTL");
    }
}
