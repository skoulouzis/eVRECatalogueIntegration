# eVRE-ENVRI
<p align="left">
  <img src="https://teaching.science.uva.nl/wp-content/uploads/2017/12/uva-logo.png" alt="HTML5 Icon" width="60" height="65" >
            <img src="https://www.envriplus.eu/wp-content/uploads/2015/08/ENVRI-plus_m.png" alt="HTML5 Icon" width="85" height="60" hspace=20>
            <img src="https://www.envriplus.eu/wp-content/uploads/2017/10/Untitled-300x294.png" alt="HTML5 Icon" width="70" height="70">
</p>

This reposetory contains the follwing modules: 

* CatMap: Is a flexible catalogue integrator that can convert metadata catalogue records to other formats using the 3m mapper
* WPS2WADL: A simple tool to convert WPS services to WADL
* eVRETaverna-gis-plugin: A taverna plugin to discover and invoke WPS services adapted for D4Science services 
* eVRETaverna: A taverna plugin to discover WADL services from the eVRE workflow catalogue adapted to also discover WPS services 


## Demo

Scaling eVRE to multiple Research infrastructures. To achive this, first automate the alignment of metadata of a catalogue onto CERIF, next dynamically load heterogeneous data catalogues into semantic triple stores. Following that,  dynamically load OGC web services catalogue in workflow and finally contextually integrate distributed provenance information of a workflow.

<p align="center">
  <img width="460" height="300" src="https://raw.githubusercontent.com/QCAPI-DRIP/eVRE-ENVRI_Blocks/master/images/EVRE-ENVRIPLUS-Demo-0928.png">
</p>


### Metadata Recorder Creator
Demonstrates the automatic the alignment of metadata of a catalogue onto CERIF. The Metadata Recorder Creator retrieves records from a catalogue endpoint and creates mapped CERIF records using conferrable mappers created by 3M. The records are available for downscaling.  

<p align="center">
  <img width="360" height="200" src="https://raw.githubusercontent.com/QCAPI-DRIP/eVRE-ENVRI/master/images/demo1Arch.png">
</p>


A demo can be found here: http://drip.vlan400.uvalight.net:8083/catalogue_mapper/index1.html

### CERIF Community Cataloge Manager
Demonstrates how to dynamically load heterogeneous data catalogues into semantic triple stores. A catalogue endpoint is provided 
and the MRCreator is configured using  mapping file, created by 3M. Next, a the converted records are loaded in the search engine. 

<p align="center">
  <img width="360" height="200" src="https://raw.githubusercontent.com/QCAPI-DRIP/eVRE-ENVRI/master/images/demo2Arch.png">
</p>

A demo can be found here: http://drip.vlan400.uvalight.net:8083/catalogue_mapper/index2.html

### Provenance Context Integrator 
Demonstrates the integration of distributed provenance information of a workflow. The 
Provenance Context Integrator allows to load workflow execution results, Prov based provenance, service logs create CERIF based contextual description and perform simple queries


<p align="center">
  <img width="360" height="200" src="https://raw.githubusercontent.com/QCAPI-DRIP/eVRE-ENVRI/master/images/demo3Arch.png">
</p>

A demo can be found here: http://drip.vlan400.uvalight.net:8083/catalogue_mapper/index3.html

