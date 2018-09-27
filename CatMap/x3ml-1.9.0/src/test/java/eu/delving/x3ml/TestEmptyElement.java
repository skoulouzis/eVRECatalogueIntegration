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
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import java.util.List;
import static eu.delving.x3ml.AllTests.*;
import junit.framework.TestCase;
import org.apache.log4j.Level;

/**
 * @author Gerald de Jong <gerald@delving.eu>
 * @author Nikos Minadakis <minadakn@ics.forth.gr>
 * @author Yannis Marketakis <marketak@ics.forth.gr>
 */

public class TestEmptyElement extends TestCase{
    private final Generator VALUE_POLICY = X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(1));

    @Override
    public void setUp(){
        Logger.getLogger(gr.forth.Utils.class).setLevel(Level.OFF);
    }
    
    @Test
    public void testEmptyElementInDomain() {
        X3MLEngine engine = engine("/empty_element/01_mapping_domain.x3ml");
        X3MLEngine.Output output = engine.execute(document("/empty_element/input.xml"),VALUE_POLICY);
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/empty_element/01_expected_output.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }
    
    @Test
    public void testEmptyElementInRange() {
        X3MLEngine engine = engine("/empty_element/02_mapping_range.x3ml");
        X3MLEngine.Output output = engine.execute(document("/empty_element/input.xml"),VALUE_POLICY);
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/empty_element/02_expected_output.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }
    
    /* Used to test if the execution does not stop when there are nodes that are missing (related to issue #116) */
    @Test
    public void testMissingElementsFromLink() {
        X3MLEngine engine = engine("/empty_element/03_mappings.x3ml");
        X3MLEngine.Output output = engine.execute(document("/empty_element/03_input.xml"),policy("/empty_element/03_generator-policy.xml"));
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/empty_element/03_expected_output.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }
}
