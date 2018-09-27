/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.forth.ics.isl.cerifdataharvester.gui;

import gr.forth.ics.isl.common.FrisResources;
import gr.forth.ics.isl.exception.GenericException;
import gr.forth.ics.isl.exporter.FrisExporter;
import java.io.File;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class FrisExporterGui {
    
    private void createFolders(boolean harvestProjects, boolean harvestPersons, boolean harvestPublications, boolean harvestOrganizations){
        if(harvestProjects){
            new File(FrisResources.PROJECTS).mkdir();
        }
        if(harvestPersons){
            new File(FrisResources.PERSONS).mkdir();
        }
        if(harvestPublications){
            new File(FrisResources.PUBLICATIONS).mkdir();
        }
        if(harvestOrganizations){
            new File(FrisResources.ORGANIZATIONS).mkdir();
        }
    }

    public void startFromUI(boolean harvestProjects, boolean harvestPersons, boolean harvestPublications, boolean harvestOrganizations) throws GenericException{
        FrisExporter exporter=new FrisExporter(FrisResources.FRIS_ENDPOINT_URL);
        if(harvestProjects || harvestPersons || harvestPublications || harvestOrganizations){
            this.createFolders(harvestProjects, harvestPersons, harvestPublications, harvestOrganizations);
        }
        if(harvestPersons){
            try{
                exporter.exportResources(FrisResources.FRIS_ENDPOINT_URL+FrisResources.PERSONS_SUFFIX, 
                                         FrisResources.PERSONS, 
                                         FrisResources.PERSONS);
            }catch(GenericException ex){
                throw new GenericException("An error occured while harvesting CERIF data");
            }
        }
        if(harvestProjects){
            try{
                exporter.exportResources(FrisResources.FRIS_ENDPOINT_URL+FrisResources.PROJECTS_SUFFIX, 
                                         FrisResources.PROJECTS, 
                                         FrisResources.PROJECTS);
            }catch(GenericException ex){
                throw new GenericException("An error occured while harvesting CERIF data");
            }
        }
        if(harvestOrganizations){
            try{
                exporter.exportResources(FrisResources.FRIS_ENDPOINT_URL+FrisResources.ORGANIZATIONS_SUFFIX, 
                                         FrisResources.ORGANIZATIONS, 
                                         FrisResources.ORGANIZATIONS);
            }catch(GenericException ex){
                throw new GenericException("An error occured while harvesting CERIF data");
            }
        }
        if(harvestPublications){
            try{
                exporter.exportResources(FrisResources.FRIS_ENDPOINT_URL+FrisResources.PUBLICATIONS_SUFFIX, 
                                         FrisResources.PUBLICATIONS, 
                                         FrisResources.PUBLICATIONS);
            }catch(GenericException ex){
                throw new GenericException("An error occured while harvesting CERIF data");
            }
        }
    }
}