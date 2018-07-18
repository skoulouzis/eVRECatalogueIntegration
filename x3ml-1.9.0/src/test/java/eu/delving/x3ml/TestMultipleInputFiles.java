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
import static eu.delving.x3ml.AllTests.resource;
import static eu.delving.x3ml.AllTests.xmlToNTriples;
import gr.forth.Utils;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 * @author Nikos Minadakis (minadakn 'at' ics 'dot' forth 'dot' gr)
 */
public class TestMultipleInputFiles {
    private final Logger log = Logger.getLogger(getClass());

    @Test
    public void testUsingSingleFile() throws FileNotFoundException {
        List<InputStream> LIST_OF_INPUT_STREAMS=Arrays.asList(resource("/multiple_input_files/input1.xml"));
        X3MLEngine engine = engine("/multiple_input_files/mappings.x3ml");
        X3MLGeneratorPolicy policy=X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(2));
        X3MLEngine.Output output = engine.execute(document("/multiple_input_files/input1.xml"),policy);
        X3MLEngine.Output output2 = engine.execute(Utils.parseMultipleXMLFiles(LIST_OF_INPUT_STREAMS),policy);
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = output.toStringArray();
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }
    
    @Test
    public void testUsingMultipleFiles() throws FileNotFoundException {
        List<InputStream> LIST_OF_INPUT_STREAMS=Arrays.asList(resource("/multiple_input_files/input1.xml"),
                                                              resource("/multiple_input_files/input2.xml"));
        X3MLEngine engine = engine("/multiple_input_files/mappings.x3ml");
        X3MLGeneratorPolicy policy=X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(2));
        X3MLEngine.Output output = engine.execute(Utils.parseMultipleXMLFiles(LIST_OF_INPUT_STREAMS),policy);
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/multiple_input_files/expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }
    
    /*Use this to avoid situations like the ones in #49 */
    @Test
    public void testUsingMultipleFilesAvoidMerging() throws FileNotFoundException {
        List<InputStream> LIST_OF_INPUT_STREAMS=Arrays.asList(resource("/multiple_input_files/input3.xml"),
                                                              resource("/multiple_input_files/input4.xml"));
        X3MLEngine engine = engine("/multiple_input_files/mappings.x3ml");
        X3MLGeneratorPolicy policy=X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(2));
        X3MLEngine.Output output = engine.execute(Utils.parseMultipleXMLFiles(LIST_OF_INPUT_STREAMS),policy);
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/multiple_input_files/expectedOutput2.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }
    
    /*Concatenating multiple xml input files is valid only if the given xml input files have the same root element*/
    @Test
    public void testUsingMultipleFilesCheckError() throws FileNotFoundException {
        List<InputStream> LIST_OF_INPUT_STREAMS=Arrays.asList(resource("/multiple_input_files/input1_err.xml"),
                                                              resource("/multiple_input_files/input2_err.xml"));
        X3MLEngine engine = engine("/multiple_input_files/mappings.x3ml");
        X3MLGeneratorPolicy policy=X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(2));
        try{
            X3MLEngine.Output output = engine.execute(Utils.parseMultipleXMLFiles(LIST_OF_INPUT_STREAMS),policy);
            fail("Concatenating multiple xml input files is valid only if the given xml input files have the same root element");
        }catch(X3MLEngine.X3MLException ex){
            assertTrue("Successfully caught excpetion",true);
        }
    }
}