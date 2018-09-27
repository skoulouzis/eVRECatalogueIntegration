# X3ML Engine
---
## Introduction

The X3ML engine handles the URI generation and the data transformation steps of the data provision and aggregation process. It realizes the transformation of the source records to the target's format. The engine takes as input the source data (currently in the form of an XML document), the description of the mappings in the X3ML mapping definition file and the URI generation policy file and is responsible for transforming the XML document into a valid RDF document which is equivalent with the XML input, with respect to the given mappings and policy. The engine has been originally implemented in the context of the CultureBrokers project co-funded by the Swedish Arts Council and the British Museum.

## X3ML Language

The X3ML mapping definition language is an XML based language which describes schema mappings in such a way that they can be collaboratively created and discussed by experts. The X3ML language was designed on the basis of work that started in FORTH in 2006 and emphasizes on establishing a standardized mapping description which lends itself to collaboration and the building of a mapping memory to accumulate knowledge and experience. It was adapted primarily to be more according to the DRY principle (avoiding repetition) and to be more explicit in its contract with the URI Generating process. X3ML separates schema mapping from the concern of generating proper URIs so that different expertise can be applied to these two very different responsibilities.

* **[X3ML Language](https://github.com/isl/x3ml/blob/master/docs/x3ml-language.md)** - the mapping language

## Development

This project is a straightforward Maven 3 project, producing a single artifact in the form of a JAR file which contains the engine software.  The artifact will be used in a variety of different contexts, so the main focus of this project is to create exhaustively tested transformation engine.  Examples of input and expected output have been prepared by the participating organizations.

* **[Change Log](https://github.com/isl/x3ml/blob/master/docs/change-log.md)** - Changes between versions

### Project Structure

The project is structured with respect to Maven principles and any important or useful resources are categorized to different folders. More specifically it contains the following folders:

* **src**: contains the source code of the X3ML engine and tests
* **docs**: contains the corresponding documentation material for relevant resources (i.e. specification of the X3ML language, changelog of the X3ML engine, etc.)
* **resources**: contains information about useful resources (i.e. the X3ML schema XSD files and their documentation)
* **example**: contains examples (i.e. XML and X3ML files) that can be used for demonstrating the functionality of X3ML Engine

## Design Principles

* **Simplicity**

	It is easier to create complicated things than it is to find the simplicity in something that would otherwise be complex.  One important way to achieve simplicity and clarity is by carefully naming things so that their meaning is as obvious as possible to the naked eye.
	
* **Transparency**

	The most important feature of X3ML is its general application to mapping creation and execution and hopefully its longevity.  People must be able to easily understand how it works.  The **cleaner** the core design of this engine and X3ML language, and the clearer its documentation, the more readily it will get traction and become the basis for future mappings.

* **Re-use of Standards and Technologies**

	The best way to build a new software module is to carefully choose its dependencies, and keeping them as small as possible.  Building on top of proven technologies is the quickest way to a dependable result.

	* **[XStream](http://xstream.codehaus.org/)** - easy reading/writing of XML 
	
	* **[Handy URI Templates](https://github.com/damnhandy/Handy-URI-Templates)** - standardized URI generation [RFC 6570](http://tools.ietf.org/html/rfc6570)
	
	* **[Jena](https://jena.apache.org/)** - in-memory building of graph for RDF output

* **Facilitating Instance Matching**

	An application of X3ML which came up during discussions at the beginning of this project involved extracting semantic information with the intent of finding correct instance URIs.  This implies a relatively small extension to the original idea of the X3ML engine because it will have to provide modified source records as well as RDF in its output.
	
	When [instance matching](http://prezi.com/povcuuboyyg5/culture-brokers-enrichment/) is performed and URIs are found, it must be explcitly known how to substitute them back into the source data.  The X3ML engine will decorate the source record tree with placeholders so that the results of the instance matching can find their way back to the right locations in the source.


## Publications

* 	Nikos Minadakis, Yannis Marketakis, Haridimos Kondylakis, Giorgos Flouris, Maria Theodoridou, Martin Doerr, and Gerald de Jong. X3ML Framework: An effictive suite for supporting data mappings. Workshop for Extending, Mapping and Focusing the CRM - co-located with TPDL'2015, Poznan, Poland, September 2015. [PDF] (http://users.ics.forth.gr/~marketak/download/x3mlEngine_tpdl2015.pdf)
* 	Yannis Marketakis, Nikos Minadakis, Haridimos Kondylakis, Konstantina Konsolaki, Georgios Samaritakis, Maria Theodoridou, Giorgos Flouris, Martin Doerr . X3ML Mapping Framework for Information Integration in Cultural Heritage and beyond. International Journal on Digital Libraries, Special Issue: Extending, Mapping and Focusing the CIDOC CRM (to be published 2016). [PDF] (http://users.ics.forth.gr/~marketak/download/X3ML_Framework_IJDL_2016.pdf)	


X3ML console started now supports importing multiple input files and folders, and multiple x3ml mappings files. Furthermore they allow adding URLs of input and X3ML mappings resources (both single and multiple).

Furthermore the option parameters have been updated and introduced short parameters as well. The detailed usage help menu is shown when running X3ML Engine console starter and is shown below.

## Console run
To run the x3ml engine from the console you need to download the x3ml engine executable .jar and execute it according to the following instructions:

* *-i,--input <arg>*: XML input records.
  * Option A-single file: -input input.xml
  * Option B-multiple files (comma-sep): -input input1.xml,input2.xml,input3.xml
  * Option C-folder: -input #_folder_path
  * Option D-URL: -input @input_url
  * Option E-multiple URLs: -input @input_url1,input_url2,input_url3
  * Option F-stdin: -input @
* *-x,--x3ml <arg>*: X3ML mapping definition.
  * Option A-single file: -x3ml mapping.x3ml
  * Option B-multiple files (comma-sep): -x3ml mappings1.x3ml,mappings2.x3ml
  * Option C-stdin: -x3ml @
* *-p,--policy <arg>*: The value policy file: -policy policy.xml
* *-o,--output <arg>*: The RDF output file name: -output output.rdf
* *-u,--uuidTestSize <arg>*:  Create a test UUID generator of the given size.
* *-f,--format <arg>*: Output format. Options:
  * format application/n-triples
  * -format text/turtle
  * -format application/rdf+xml (default)
* *-a,--assocTable <arg>*: export the contents of the association table in XML format
* *-m,--mergeAssocWithRDF*:merge the contents of the association table with the RDF output
	
A simple example looks like:

```
java -jar x3ml.jar -i input.xml -x mappings.x3ml -p generator-policy.xml -o output.rdf -u 4
```

---

## Contacts

* Martin Doerr &lt;martin@ics.forth.gr&gt;
* Minadakis Nikos &lt;minadakn@ics.forth.gr&gt;
* Marketakis Yannis &lt;marketak@ics.forth.gr&gt;
