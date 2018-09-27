/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.forth.ics.isl.cerifdataharvester.gui;

import gr.forth.ics.isl.common.CerifResources;
import gr.forth.ics.isl.common.EktResources;
import gr.forth.ics.isl.exception.GenericException;
import gr.forth.ics.isl.exporter.EktExporter;
import java.io.File;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class EktExporterGui {
    
    private void createFolders(boolean harvestProjects, boolean harvestPersons, boolean harvestPublications, boolean harvestOrganizations, boolean harvestEAddress, boolean harvestPAddress, boolean harvestFundings){
        if(harvestProjects){
            new File(EktResources.PROJECTS).mkdir();
        }
        if(harvestPersons){
            new File(EktResources.PERSONS).mkdir();
        }
        if(harvestPublications){
            new File(EktResources.PUBLICATIONS).mkdir();
        }
        if(harvestOrganizations){
            new File(EktResources.ORGANIZATION_UNITS).mkdir();
        }
        if(harvestEAddress){
            new File(EktResources.ELECTRONIC_ADDRESSES).mkdir();
        }
        if(harvestPAddress){
            new File(EktResources.POSTAL_ADDRESSES).mkdir();
        }
        if(harvestFundings){
            new File(EktResources.FUNDINGS).mkdir();
        }
    }

    public void startFromUI(boolean harvestProjects, boolean harvestPersons, boolean harvestPublications, boolean harvestOrganizations, boolean harvestEAddress, boolean harvestPAddress, boolean harvestFundings) throws GenericException{
        EktExporter exporter=new EktExporter(EktResources.EKT_ENDPOINT_URL);
        if(harvestProjects || harvestPersons || harvestPublications || harvestOrganizations || harvestEAddress || harvestPAddress || harvestFundings){
            this.createFolders(harvestProjects, harvestPersons, harvestPublications, harvestOrganizations, harvestEAddress, harvestPAddress, harvestFundings);
        }
        if(harvestPersons){
            try{
                exporter.exportLinksIterative(EktResources.EKT_ENDPOINT_URL+EktResources.PERSONS_SUFFIX, 
                                              CerifResources.CF_PERS_ELEMENT, 
                                              EktResources.PERSONS+"/"+EktResources.PERSONS, 
                                              CerifResources.CF_PERS_ELEMENT);
            }catch(GenericException ex){
                throw new GenericException("An error occured while harvesting CERIF data");
            }
        }
        if(harvestProjects){
            try{
                exporter.exportLinksIterative(EktResources.EKT_ENDPOINT_URL+EktResources.PROJECTS_SUFFIX, 
                                              CerifResources.CF_PROJ_ELEMENT, 
                                              EktResources.PROJECTS+"/"+EktResources.PROJECTS, 
                                              CerifResources.CF_PROJ_ELEMENT);
            }catch(GenericException ex){
                throw new GenericException("An error occured while harvesting CERIF data");
            }
        }
        if(harvestOrganizations){
            try{
                exporter.exportLinksIterative(EktResources.EKT_ENDPOINT_URL+EktResources.ORG_UNITS_SUFFIX, 
                                  CerifResources.CF_URI_ELEMENT,
                                  EktResources.ORGANIZATION_UNITS+"/"+EktResources.ORGANIZATION_UNITS, 
                                  CerifResources.CF_ORG_UNIT_ELEMENT);
            }catch(GenericException ex){
                throw new GenericException("An error occured while harvesting CERIF data");
            }
        }
        if(harvestPublications){
            try{
                exporter.exportLinksIterative(EktResources.EKT_ENDPOINT_URL+EktResources.PUBLICATIONS_SUFFIX, 
                                  CerifResources.CF_RES_PUBL_ELEMENT, 
                                  EktResources.PUBLICATIONS+"/"+EktResources.PUBLICATIONS, 
                                  CerifResources.CF_RES_PUBL_ELEMENT);
            }catch(GenericException ex){
                throw new GenericException("An error occured while harvesting CERIF data");
            }
        }
        if(harvestEAddress){
            try{
                exporter.exportLinksIterative(EktResources.EKT_ENDPOINT_URL+EktResources.ELECTRONIC_ADDRESSES_SUFFIX,
                                  CerifResources.CF_EADDR_ELEMENT,
                                  EktResources.ELECTRONIC_ADDRESSES+"/"+EktResources.ELECTRONIC_ADDRESSES,
                                  CerifResources.CF_EADDR_ELEMENT);
            }catch(GenericException ex){
                throw new GenericException("An error occured while harvesting CERIF data");
            }
        }
        if(harvestPAddress){
            try{
                exporter.exportLinksIterative(EktResources.EKT_ENDPOINT_URL+EktResources.POSTAL_ADDRESSES_SUFFIX,
                                  CerifResources.CF_PADDR_ELEMENT, 
                                  EktResources.POSTAL_ADDRESSES+"/"+EktResources.POSTAL_ADDRESSES, 
                                  CerifResources.CF_PADDR_ELEMENT);
            }catch(GenericException ex){
                throw new GenericException("An error occured while harvesting CERIF data");
            }
        }
        if(harvestFundings){
            try{
                exporter.exportLinksIterative(EktResources.EKT_ENDPOINT_URL+EktResources.FUNDINGS_SUFFIX, 
                                  CerifResources.CF_FUND_ELEMENT, 
                                  EktResources.FUNDINGS, 
                                  CerifResources.CF_FUND_ELEMENT);
            }catch(GenericException ex){
                throw new GenericException("An error occured while harvesting CERIF data");
            }
        }
    }
}
