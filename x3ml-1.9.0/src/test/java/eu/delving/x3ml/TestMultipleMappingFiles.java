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
import gr.forth.Utils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 * @author Nikos Minadakis (minadakn 'at' ics 'dot' forth 'dot' gr)
 */
public class TestMultipleMappingFiles extends TestCase{
    private final Generator VALUE_POLICY = X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(2));
    
    @Test
    public void testUsingSingleMappingFile() {
        X3MLEngine engine = engine("/multiple_mapping_files/mappingsSingle.x3ml");
        X3MLEngine.Output output = engine.execute(document("/multiple_mapping_files/input.xml"),VALUE_POLICY);
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/multiple_mapping_files/expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }
    
    @Test
    public void testUsingMultipleMappingFile() {
        List<InputStream> mappingStreams=Arrays.asList(
                                            TestMultipleMappingFiles.class.getResourceAsStream("/multiple_mapping_files/mappingsMultiple1.x3ml"),
                                            TestMultipleMappingFiles.class.getResourceAsStream("/multiple_mapping_files/mappingsMultiple2.x3ml"),
                                            TestMultipleMappingFiles.class.getResourceAsStream("/multiple_mapping_files/mappingsMultiple3.x3ml"));
        X3MLEngine engine=X3MLEngine.load(new ByteArrayInputStream(Utils.mergeMultipleMappingFiles(mappingStreams).getBytes()));
        X3MLEngine.Output output = engine.execute(document("/multiple_mapping_files/input.xml"),VALUE_POLICY);
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/multiple_mapping_files/expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }
    
    @Test
    public void testUsingMultipleMappingFileError() {
        try{
            List<InputStream> mappingStreams=Arrays.asList(
                                                TestMultipleMappingFiles.class.getResourceAsStream("/multiple_mapping_files/mappingsMultiple1.x3ml"),
                                                TestMultipleMappingFiles.class.getResourceAsStream("/multiple_mapping_files/mappingsMultiple2.x3ml"),
                                                TestMultipleMappingFiles.class.getResourceAsStream("/multiple_mapping_files/mappingsMultiple3.x3ml"), 
                                                TestMultipleMappingFiles.class.getResourceAsStream("/multiple_mapping_files/mappingsMultipleWrong.x3ml"));
            X3MLEngine engine=X3MLEngine.load(new ByteArrayInputStream(Utils.mergeMultipleMappingFiles(mappingStreams).getBytes()));            
            fail("One of the given X3ML mappings is not correct. We should encounter an excepetion here");
        }catch(X3MLEngine.X3MLException ex){
            assertTrue("Successfully caught X3MLException", true);
        }
    }
    
}