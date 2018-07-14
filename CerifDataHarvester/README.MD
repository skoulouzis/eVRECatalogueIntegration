This repository contains the required source code for exporting cerif data from various RIs and portals. More specifically it contains harvesters that export data in XML format with respect to CERIF model (http://www.eurocris.org/cerif/feature-tour/cerif-16). So far the following harvesters are supported

# EKT exporter #

One of the clients is responsible for exporting data from EKT (http://cc-refim.ekt.gr/cerif-rest-refim/). We export data to XML format (with respect to CERIF XML), of the following categories: 

* Organisations 
* Projects 
* Persons 
* Publications
* Fundings
* Services
* Postal addresses
* Electronic address

# RCUK exporter #

One of the clients is responsible for exporting data from RCUK (http://gtr.rcuk.ac.uk/cerif/). We export data to XML format (with respect to CERIF XML), of the following categories: 

* Organisations 
* Projects 
* Persons 
* Publications

# FRIS exporter #

One of the clients is responsible for exporting data from FRIS (http://www.researchportal.be/en/). We export data to XML format (with respect to CERIF XML), of the following categories: 

* Organisations 
* Projects 
* Persons 
* Publications

# EPOS exporter #

One of the clients is responsible for exporting sample data from EPOS (http://130.186.13.192/eposWebApi/researchInfrastructures/). We export data to XML format as regards Research Infrastructures.

# D4Science Catalog exporter #

The client harvest the public resources from the D4Science Catalog (https://ckan-d4s.d4science.org).
The catalog is powered by CKAN technology. The clients exploit the CKAN APIs for exporting the resources in JSON format 
and subsequently are transformed to their analogous XML representation. The resources that are exported fall in the 
following categories: 

* Datasets
* Virtual Research Environments (VREs)
* Software components and Methods
* Services
