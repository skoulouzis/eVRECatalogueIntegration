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

import eu.delving.x3ml.engine.Generator;
import org.junit.Test;
import static eu.delving.x3ml.AllTests.*;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Yannis Marketakis <marketak@ics.forth.gr>
 * @author Nikos Minadakis <minadakn@ics.forth.gr>
 */
public class TestNamespaces {
    private final Generator VALUE_POLICY = X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(2));

    @Test
    public void testMissingNamespace() {
        X3MLEngine engine = engine("/namespace/missingNSmappings.x3ml");
        X3MLEngine.Output output = engine.execute(document("/namespace/input.xml"),VALUE_POLICY);
        String[] mappingResult = output.toStringArray();
        /* just make sure that it will not fail */
        assertNotNull(mappingResult);
        assertTrue(mappingResult.length>0);
    }
    
    @Test
    public void testMultipleNamespaces(){
        X3MLEngine engine = engine("/namespace/multipleNamespaces-mappings.x3ml");
        X3MLEngine.Output output = engine.execute(document("/namespace/multipleNamespaces-input.xml"),VALUE_POLICY);
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/namespace/multipleNamespaces-expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    } 
    
    @Test
    public void testNamespacesInInfoBlock(){
        X3MLEngine engine = engine("/namespace/namespacesInInfoBlock.x3ml");
        X3MLEngine.Output output = engine.execute(document("/namespace/multipleNamespaces-input.xml"),VALUE_POLICY);
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/namespace/namespacesInInfoBlock-expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }
    
    @Test
    public void testNamespacesEmptyInNamespacesBlock(){
        try{    //test case 1: when the prefix of the namespace is empty but the URI is not e.g, <namespace prefix="" uri="http://localhost/"/>
            engine("/namespace/namespacesEmpty_inNamespacesBlock-1.x3ml");
            fail("An X3MLException should have been thrown because the namespace information are empty");
        }catch(X3MLEngine.X3MLException ex){
            assertTrue("Sucessfully caught X3MLException",true);
        }
        try{    //test case 2: when the prefix of the namespace is not empty but the URI is e.g, <namespace prefix="err" uri=""/>
            engine("/namespace/namespacesEmpty_inNamespacesBlock-2.x3ml");
            fail("An X3MLException should have been thrown because the namespace information are empty");
        }catch(X3MLEngine.X3MLException ex){
            assertTrue("Sucessfully caught X3MLException",true);
        }
        try{    //test case 3: when both the prefix of the namespace and the URI are empty e.g, <namespace prefix="" uri=""/>
            engine("/namespace/namespacesEmpty_inNamespacesBlock-3.x3ml");
            assertTrue("No exception should be thrown if both the prefix and the URI of the namespace are empty",true);
        }catch(X3MLEngine.X3MLException ex){
            fail("No exception should be thrown if both the prefix and the URI of the namespace are empty");
        }
    } 
    
    @Test
    public void testNamespacesEmptyInInfoBlock(){
        try{    //test case 1: when the prefix of the namespace is empty but the URI is not e.g, <namespace prefix="" uri="http://localhost/"/>
            engine("/namespace/namespacesEmpty_inInfoBlock-1.x3ml");
            fail("An X3MLException should have been thrown because the namespace information are empty");
        }catch(X3MLEngine.X3MLException ex){
            assertTrue("Sucessfully caught X3MLException",true);
        }
        try{    //test case 2: when the prefix of the namespace is not empty but the URI is e.g, <namespace prefix="err" uri=""/>
            engine("/namespace/namespacesEmpty_inInfoBlock-2.x3ml");
            fail("An X3MLException should have been thrown because the namespace information are empty");
        }catch(X3MLEngine.X3MLException ex){
            assertTrue("Sucessfully caught X3MLException",true);
        }
        try{    //test case 3: when both the prefix of the namespace and the URI are empty e.g, <namespace prefix="" uri=""/>
            engine("/namespace/namespacesEmpty_inInfoBlock-3.x3ml");
            assertTrue("No exception should be thrown if both the prefix and the URI of the namespace are empty",true);
        }catch(X3MLEngine.X3MLException ex){
            fail("No exception should be thrown if both the prefix and the URI of the namespace are empty");
        }
        try{    //test case 4: when namespace information are empty in the source_info block
            engine("/namespace/namespacesEmpty_inInfoBlock-4.x3ml");
            assertTrue("No exception should be thrown if both the prefix and the URI of the namespace are empty",true);
        }catch(X3MLEngine.X3MLException ex){
            fail("No exception should be thrown if both the prefix and the URI of the namespace are empty");
        }
    } 
}