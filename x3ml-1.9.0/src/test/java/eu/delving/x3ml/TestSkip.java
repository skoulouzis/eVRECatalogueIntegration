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
import static eu.delving.x3ml.AllTests.policy;
import static eu.delving.x3ml.AllTests.xmlToNTriples;
import java.io.FileNotFoundException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 * @author Nikos Minadakis (minadakn 'at' ics 'dot' forth 'dot' gr)
 */
public class TestSkip {
    private final Logger log = Logger.getLogger(getClass());

    @Test
    public void testWithSkipFalse() throws FileNotFoundException {
        X3MLEngine engine = engine("/skip/mappings_skipFalse.x3ml");
        X3MLEngine.Output output = engine.execute(document("/skip/input.xml"),policy("/skip/generator-policy.xml"));    
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/skip/expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }   
    
    @Test
    public void testWithSkip() throws FileNotFoundException {
        X3MLEngine engine = engine("/skip/mappings_skip.x3ml");
        X3MLEngine.Output output = engine.execute(document("/skip/input.xml"),policy("/skip/generator-policy.xml"));    
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/skip/expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }   
    
    @Test
    public void testWithSkipLinkFalse() throws FileNotFoundException {
        X3MLEngine engine = engine("/skip/mappings_skipLinkFalse.x3ml");
        X3MLEngine.Output output = engine.execute(document("/skip/input.xml"),policy("/skip/generator-policy.xml"));    
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/skip/expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }   
    
    @Test
    public void testWithSkipLink() throws FileNotFoundException {
        X3MLEngine engine = engine("/skip/mappings_skipLink.x3ml");
        X3MLEngine.Output output = engine.execute(document("/skip/input.xml"),policy("/skip/generator-policy.xml"));    
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/skip/expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }   
        
}