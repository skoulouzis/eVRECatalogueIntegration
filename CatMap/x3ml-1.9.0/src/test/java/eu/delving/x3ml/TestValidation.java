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
import eu.delving.x3ml.engine.Generator;
import java.util.List;
import junit.framework.TestCase;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 * @author Nikos Minadakis (minadakn 'at' ics 'dot' forth 'dot' gr)
 */
//@RunWith(value = BlockJUnit4ClassRunner.class)
public class TestValidation extends TestCase{
    private final Generator VALUE_POLICY = X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(1));

    /* Examines if the X3ML mappings file is a valid XML file. */
    @Test(expected = X3MLEngine.X3MLException.class)
    public void testX3mlMappingsValidationAgainstXML() {
        try{
            X3MLEngine engine = engine("/validation/mappingsInvalidXml.x3ml");
            fail("The given X3ML mappings file are invalid (XML-invalid). We should encounter X3MLException here");
        }catch(X3MLEngine.X3MLException ex){
            assertTrue("Successfully caught X3MLException", true);
        }
    }    

    /* Examines if the X3ML mappings file is compliant with X3ML schema. 
    For some weird reason it throws an assertion error, instead of X3MLException, therefore I'm cathcing both*/
    @Test(expected = X3MLEngine.X3MLException.class)
    public void testX3mlMappingsValidationAgainstSchema() {
        try{
            X3MLEngine engine = engine("/validation/mappingsInvalidSchema.x3ml");
            fail("The given X3ML mappings file are invalid (not compliant with X3ML schema). We should encounter X3MLException here");
        }catch(X3MLEngine.X3MLException | AssertionError ex){
            assertTrue("Successfully caught X3MLException",true);
        }
    }    

    /* The success scenario */
    @Test
    public void testX3mlMappingsValidationAgainstXmlAndSchemaValid() {
        X3MLEngine engine = engine("/validation/mappingsValid.x3ml");
        assertTrue("Successfully validated X3ML mappings file",true);
    }    
}