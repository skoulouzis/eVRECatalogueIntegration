/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.forth.ics.isl.cerifdataharvester.gui;

import gr.forth.ics.isl.common.RcukResources;
import gr.forth.ics.isl.exception.GenericException;
import gr.forth.ics.isl.exporter.RcukExporter;
import java.io.File;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class RcukExporterGui {
    
    private void createFolders(boolean harvestProjects, boolean harvestPersons, boolean harvestPublications, boolean harvestOrganizations){
        if(harvestProjects){
            new File(RcukResources.PROJECTS).mkdir();
        }
        if(harvestPersons){
            new File(RcukResources.PERSONS).mkdir();
        }
        if(harvestPublications){
            new File(RcukResources.PUBLICATIONS).mkdir();
        }
        if(harvestOrganizations){
            new File(RcukResources.ORGANIZATION_UNITS).mkdir();
        }
    }

    public void startFromUI(boolean harvestProjects, boolean harvestPersons, boolean harvestPublications, boolean harvestOrganizations) throws GenericException{
        RcukExporter exporter=new RcukExporter(RcukResources.RCUK_ENDPOINT_URL);
        if(harvestProjects || harvestPersons || harvestPublications || harvestOrganizations){
            this.createFolders(harvestProjects, harvestPersons, harvestPublications, harvestOrganizations);
        }
        if(harvestPersons){
            try{
                exporter.exportResources(RcukResources.RCUK_ENDPOINT_URL+RcukResources.PERSONS_SUFFIX, 
                                              RcukResources.PERSONS, 
                                              RcukResources.PERSONS);
            }catch(GenericException ex){
                throw new GenericException("An error occured while harvesting CERIF data");
            }
        }
        if(harvestProjects){
            try{
                exporter.exportResources(RcukResources.RCUK_ENDPOINT_URL+RcukResources.PROJECTS_SUFFIX, 
                                              RcukResources.PROJECTS, 
                                              RcukResources.PROJECTS);
            }catch(GenericException ex){
                throw new GenericException("An error occured while harvesting CERIF data");
            }
        }
        if(harvestOrganizations){
            try{
                exporter.exportResources(RcukResources.RCUK_ENDPOINT_URL+RcukResources.ORG_UNITS_SUFFIX, 
                                              RcukResources.ORGANIZATION_UNITS, 
                                              RcukResources.ORGANIZATION_UNITS);
            }catch(GenericException ex){
                throw new GenericException("An error occured while harvesting CERIF data");
            }
        }
        if(harvestPublications){
            try{
                exporter.exportResources(RcukResources.RCUK_ENDPOINT_URL+RcukResources.PUBLICATIONS_SUFFIX, 
                                              RcukResources.PUBLICATIONS, 
                                              RcukResources.PUBLICATIONS);
            }catch(GenericException ex){
                throw new GenericException("An error occured while harvesting CERIF data");
            }
        }
    }
}
