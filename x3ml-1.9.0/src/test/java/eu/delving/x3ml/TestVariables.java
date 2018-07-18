/*==============================================================================
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
==============================================================================*/
package eu.delving.x3ml;

import static eu.delving.x3ml.AllTests.compareNTriples;
import static eu.delving.x3ml.AllTests.document;
import static eu.delving.x3ml.AllTests.engine;
import static eu.delving.x3ml.AllTests.errorFree;
import static eu.delving.x3ml.AllTests.xmlToNTriples;
import java.io.FileNotFoundException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 * @author Nikos Minadakis (minadakn 'at' ics 'dot' forth 'dot' gr)
 */
public class TestVariables {
    private final Logger log = Logger.getLogger(getClass());

    @Test
    public void testVariablesScopeWithinMapping() throws FileNotFoundException {
        X3MLEngine engine = engine("/variables/globalVariables-mappings.x3ml");
        X3MLGeneratorPolicy policy=X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(2));
        X3MLEngine.Output output = engine.execute(document("/variables/variables-input.xml"),policy);
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/variables/globalVariables-expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }   
    
    @Test
    public void testVariables() throws FileNotFoundException {
        X3MLEngine engine = engine("/variables/variables-mappings.x3ml");
        X3MLGeneratorPolicy policy=X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(2));
        X3MLEngine.Output output = engine.execute(document("/variables/variables-input.xml"),policy);
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/variables/variables-expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }   
    
    /* Test that the variables are working as expected, even if we ommit the details of a 
    particular entity (because we can find them through the variable elsewhere) */
    @Test
    public void testVariablesOmmitEntityWithIntermediateDetails() throws FileNotFoundException {
        X3MLEngine engine = engine("/variables/variablesOmmitEntityDetailsWithIntermediate-mappings.x3ml");
        X3MLGeneratorPolicy policy=X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(2));
        X3MLEngine.Output output = engine.execute(document("/variables/variables-input.xml"),policy);
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/variables/variablesOmmitEntityDetailsWithIntermediate-expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }
    
    /* Test that the variables are working as expected, even if we ommit the details of a 
    particular entity (because we can find them through the variable elsewhere) */
    @Test
    public void testVariablesOmmitEntityWithAdditionalDetails() throws FileNotFoundException {
        X3MLEngine engine = engine("/variables/variablesOmmitEntityDetailsWithAdditional-mappings.x3ml");
        X3MLGeneratorPolicy policy=X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(2));
        X3MLEngine.Output output = engine.execute(document("/variables/variables-input.xml"),policy);
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/variables/variablesOmmitEntityDetailsWithAdditional-expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }
    
    /* Test that the variables are working as expected, even if we ommit the details of a 
    particular entity (because we can find them through the variable elsewhere) */
    @Test
    public void testVariablesOmmitEntityDetails() throws FileNotFoundException {
        X3MLEngine engine = engine("/variables/variables-OmmitEntityDetails-mappings.x3ml");
        X3MLGeneratorPolicy policy=X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(2));
        X3MLEngine.Output output = engine.execute(document("/variables/variables-input.xml"),policy);
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/variables/variables-OmmitEntityDetails-expectedResults.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }
    
    /* Test that the variables are working as expected, even if we ommit the details of a 
    particular entity (because we can find them through the variable elsewhere) */
    @Test
    public void testVariablesOmmitEntityDetailsErr() throws FileNotFoundException {
        try{
            X3MLEngine engine = engine("/variables/variables-OmmitEntityDetails-mappings_err.x3ml");
            X3MLGeneratorPolicy policy=X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(2));
            X3MLEngine.Output output = engine.execute(document("/variables/variables-input.xml"),policy);
            fail("At this point we should encounter an X3MLException - Variable details are missing");
        }catch(X3MLEngine.X3MLException ex){
           assertTrue("Successfully caught X3MLException",true);
        }
    }
    
    /* Test that the variables are working as expected, even if we ommit the details of a 
    particular entity (because we can find them through the variable elsewhere) */
    @Test
    public void testGlobalVariablesOmmitEntityWithIntermediateDetails() throws FileNotFoundException {
        X3MLEngine engine = engine("/variables/globalVariablesOmmitEntityDetailsWithIntermediate-mappings.x3ml");
        X3MLGeneratorPolicy policy=X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(2));
        X3MLEngine.Output output = engine.execute(document("/variables/variables-input.xml"),policy);
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/variables/globalVariablesOmmitEntityDetailsWithIntermediate-expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }
    
    /* Test that the variables are working as expected, even if we ommit the details of a 
    particular entity (because we can find them through the variable elsewhere) */
    @Test
    public void testGlobalVariablesOmmitEntityDetails() throws FileNotFoundException {
        X3MLEngine engine = engine("/variables/globalVariablesOmmitEntityDetails-mappings.x3ml");
        X3MLGeneratorPolicy policy=X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(2));
        X3MLEngine.Output output = engine.execute(document("/variables/variables-input.xml"),policy);
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/variables/globalVariablesOmmitEntityDetails-expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }
    
    /* Test that the variables are working as expected, even if we ommit the details of a 
    particular entity (because we can find them through the variable elsewhere) */
    @Test
    public void testGlobalVariablesOmmitEntityWithAdditionalDetails() throws FileNotFoundException {
        X3MLEngine engine = engine("/variables/globalVariablesOmmitEntityDetailsWithAdditional-mappings.x3ml");
        X3MLGeneratorPolicy policy=X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(2));
        X3MLEngine.Output output = engine.execute(document("/variables/variables-input.xml"),policy);
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/variables/globalVariablesOmmitEntityDetailsWithAdditional-expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }
    
    /* Test that the variables are working as expected, even if we ommit the details of a 
    particular entity (because we can find them through the variable elsewhere) */
    @Test
    public void testGlobalVariablesOmmitEntityDetailsErr() throws FileNotFoundException {
        try{
            X3MLEngine engine = engine("/variables/globalVariablesOmmitEntityDetails-mappings_err.x3ml");
            X3MLGeneratorPolicy policy=X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(2));
            X3MLEngine.Output output = engine.execute(document("/variables/variables-input.xml"),policy);
            fail("At this point we should encounter an X3MLException - Global variable details are missing");
        }catch(X3MLEngine.X3MLException ex){
            assertTrue("Successfully caught X3MLException",true);
        }
    }
}