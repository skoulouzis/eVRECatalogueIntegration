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
import eu.delving.x3ml.engine.Generator;
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
public class TestLiterals {
    private final Logger log = Logger.getLogger(getClass());
    private final Generator VALUE_POLICY = X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(1));

    @Test
    public void testLiteralTypeFullURI() throws FileNotFoundException {
        X3MLEngine engine = engine("/literals/mappings.x3ml");
        X3MLEngine.Output output = engine.execute(document("/literals/input.xml"),policy("/literals/generator-policy.xml"));
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/literals/expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }   
    
    @Test
    public void testLiteralTypeUsingAbbrev() throws FileNotFoundException {
        X3MLEngine engine = engine("/literals/mappings2.x3ml");
        X3MLEngine.Output output = engine.execute(document("/literals/input.xml"),policy("/literals/generator-policy.xml"));
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/literals/expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }   
    
    @Test
    public void testDateTypeFullURI() throws FileNotFoundException {
        X3MLEngine engine = engine("/literals/mappings_Dates.x3ml");
        X3MLEngine.Output output = engine.execute(document("/literals/input.xml"),policy("/literals/generator-policy.xml"));
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/literals/expectedOutputDates.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }   
    
    @Test
    public void testDateTypeUsingAbbrev() throws FileNotFoundException {
        X3MLEngine engine = engine("/literals/mappings_Dates2.x3ml");
        X3MLEngine.Output output = engine.execute(document("/literals/input.xml"),policy("/literals/generator-policy.xml"));
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/literals/expectedOutputDates.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }   
    
    @Test
    public void testLanguage() throws FileNotFoundException {
        X3MLEngine engine = engine("/literals/02_lang-mappings.x3ml");
        X3MLEngine.Output output = engine.execute(document("/literals/02_lang-input.xml"),VALUE_POLICY);
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/literals/02_lang-expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }   
}