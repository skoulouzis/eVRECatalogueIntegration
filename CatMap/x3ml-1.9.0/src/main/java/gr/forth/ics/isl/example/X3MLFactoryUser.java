package gr.forth.ics.isl.example;

import eu.delving.x3ml.X3MLEngineFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import org.apache.jena.riot.Lang;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 * @author Nikos Minadakis (minadakn 'at' ics 'dot' forth 'dot' gr)
 */
public class X3MLFactoryUser {
    
    /* The simplest possible senario. Add only the mandatory details */
    private static void simplestScenario(){
        X3MLEngineFactory.create()
                         .withMappings(new File("example/mappingsWithoutGenerator.x3ml"))
                         .withInputFiles(new File("example/input.xml"))
                         .execute();
    }
    
    /* Playing with generator policies and UUID sizes */
    private static void withGeneratorPolicyScenario() throws FileNotFoundException{
        X3MLEngineFactory.create()
                         .withMappings(new File("example/mappings.x3ml"))
                         .withInputFiles(new File("example/input.xml"))
                         .withUuidSize(2)
                         .withGeneratorPolicy(new File("example/generator-policy.xml"))
                         .execute();
    }
    
    /* Playing with multiple files */
    private static void multipleInputFilesScenario(){
        X3MLEngineFactory.create()
                         .withMappings(new File("example/mappingsWithoutGenerator.x3ml"))
                         .withInputFiles(new File("example/input.xml"))
                         .withInputFiles(new File("example/moreExamples/input1.xml"), new File("example/moreExamples/input2.xml"))
                         .execute();
    }
    
    /* Playing with multiple mapping files */
    private static void multipleMappingFilesScenario(){
        X3MLEngineFactory.create()
                         .withMappings(new File("example/mappingsWithoutGenerator.x3ml"), new File("example/mappingsWithoutGenerator2.x3ml"))
                         .withInputFiles(new File("example/input.xml"))
                         .execute();
    }
    
    /* Playing with multiple folders */
    private static void multipleFilesAndFoldersScenario(){
        X3MLEngineFactory.create()
                         .withMappings(new File("example/mappingsWithoutGenerator.x3ml"))
                         .withInputFiles(new File("example/input.xml"))
                         .withInputFolder(new File("example/moreExamples"), true)
                         .execute();
    }
    
    /* Playing with different output and output formats */
    private static void outputFormatsScenario(){
        X3MLEngineFactory.create()
                         .withMappings(new File("example/mappingsWithoutGenerator.x3ml"))
                         .withInputFiles(new File("example/input.xml"))
                         .withOutput("output.ntriples", X3MLEngineFactory.OutputFormat.NTRIPLES)
                         .execute();
        
        X3MLEngineFactory.create()
                         .withMappings(new File("example/mappingsWithoutGenerator.x3ml"))
                         .withInputFiles(new File("example/input.xml"))
                         .withOutput(new File("output.rdf"), X3MLEngineFactory.OutputFormat.RDF_XML)
                         .execute();
    }
    
    /* Export the contents of the association table */
    private static void exportAssocTableScenario(){
        X3MLEngineFactory.create()
                         .withMappings(new File("example/mappingsWithoutGenerator.x3ml"))
                         .withInputFiles(new File("example/input.xml"))
                         .withAssociationTable("target/AssociationTable.xml")
                         .execute();
    }
    
    /* Produce more verbose logging output */
    private static void verboseOutputScenario(){
        X3MLEngineFactory.create()
                         .withVerboseLogging()
                         .withMappings(new File("example/mappingsWithoutGenerator.x3ml"))
                         .withInputFiles(new File("example/input.xml"))
                         .withProgressReporting()
                         .execute();
    }
    
    /* Playing with terminologies */
    private static void terminologiesScenario(){
        X3MLEngineFactory.create()
                         .withMappings(new File("example/moreExamples/terminologies/mappings.x3ml"))
                         .withInputFiles(new File("example/moreExamples/terminologies/input.xml"))
                         .withGeneratorPolicy(new File("example/generator-policy.xml"))
                         .withTerminology(new File("example/moreExamples/terminologies/terms.nt"), Lang.NT)
                         .execute();
    }
    
    /* The simplest possible senario. Add only the mandatory details */
    private static void simplestStreamScenario() throws FileNotFoundException{
        X3MLEngineFactory.create()
                         .withMappings(new FileInputStream(new File("example/mappingsWithoutGenerator.x3ml")))
                         .withInput(new FileInputStream(new File("example/input.xml")))
                         .execute();
    }
    
    /* Playing with generator policies and UUID sizes */
    private static void withGeneratorPolicyStreamScenario() throws FileNotFoundException{
        X3MLEngineFactory.create()
                         .withMappings(new FileInputStream(new File("example/mappings.x3ml")))
                         .withInput(new FileInputStream(new File("example/input.xml")))
                         .withUuidSize(2)
                         .withGeneratorPolicy(new FileInputStream(new File("example/generator-policy.xml")))
                         .execute();
    }
    
    /* Playing with multiple files */
    private static void multipleInputStreamsScenario() throws FileNotFoundException{
        X3MLEngineFactory.create()
                         .withMappings(new FileInputStream(new File("example/mappingsWithoutGenerator.x3ml")))
                         .withInput(new FileInputStream(new File("example/input.xml")))
                         .withInput(new FileInputStream(new File("example/moreExamples/input1.xml")),
                                    new FileInputStream(new File("example/moreExamples/input2.xml")))
                         .execute();
    }
    
        
    /* Playing with multiple mapping files */
    private static void multipleMappingStreamsScenario() throws FileNotFoundException{
        X3MLEngineFactory.create()
                         .withMappings(new FileInputStream(new File("example/mappingsWithoutGenerator.x3ml")),
                                       new FileInputStream(new File("example/mappingsWithoutGenerator2.x3ml")))
                         .withInput(new FileInputStream(new File("example/input.xml")))
                         .execute();
    }
    
    /* Playing with different output and output formats using streams */
    private static void outputFormatsStreamsScenario() throws FileNotFoundException{
        X3MLEngineFactory.create()
                         .withMappings(new File("example/mappingsWithoutGenerator.x3ml"))
                         .withInputFiles(new File("example/input.xml"))
                         .withOutput(System.out, X3MLEngineFactory.OutputFormat.RDF_XML)
                         .execute();
        
        X3MLEngineFactory.create()
                         .withMappings(new File("example/mappingsWithoutGenerator.x3ml"))
                         .withInputFiles(new File("example/input.xml"))
                         .withOutput(new PrintStream(new File("output.rdf")), X3MLEngineFactory.OutputFormat.RDF_XML)
                         .execute();
    }
    
    /* Playing with terminologies */
    private static void terminologiesStreamsScenario() throws FileNotFoundException{
        X3MLEngineFactory.create()
                         .withMappings(new File("example/moreExamples/terminologies/mappings.x3ml"))
                         .withInputFiles(new File("example/moreExamples/terminologies/input.xml"))
                         .withGeneratorPolicy(new File("example/generator-policy.xml"))
                         .withTerminology(new FileInputStream(new File("example/moreExamples/terminologies/terms.nt")), Lang.NT)
                         .execute();
    }
    
    public static void main(String[] args) throws FileNotFoundException{
        simplestScenario();
        withGeneratorPolicyScenario();
        multipleInputFilesScenario();
        multipleMappingFilesScenario();
        multipleFilesAndFoldersScenario();
        outputFormatsScenario();
        exportAssocTableScenario();
        verboseOutputScenario();
        terminologiesScenario();
        /* Using Streams */
        simplestStreamScenario();
        withGeneratorPolicyStreamScenario();
        multipleInputStreamsScenario();
        multipleMappingStreamsScenario();
        outputFormatsStreamsScenario();
        terminologiesStreamsScenario();
    }
}