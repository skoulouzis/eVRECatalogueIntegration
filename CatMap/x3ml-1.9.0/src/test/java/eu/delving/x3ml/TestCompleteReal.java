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
public class TestCompleteReal {
    private final Logger log = Logger.getLogger(getClass());

    @Test
    public void testdFMRO() throws FileNotFoundException {
        X3MLEngine engine = engine("/completeTests_real/dFMRO/mappings.x3ml");
        X3MLEngine.Output output = engine.execute(document("/completeTests_real/dFMRO/input.xml"),policy("/completeTests_real/dFMRO/generator-policy.xml",2));
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/completeTests_real/dFMRO/expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }   

    @Test
    public void testPergamon() throws FileNotFoundException {
        X3MLEngine engine = engine("/completeTests_real/pergamon/mappings.x3ml");
        X3MLEngine.Output output = engine.execute(document("/completeTests_real/pergamon/input.xml"),policy("/completeTests_real/pergamon/generator-policy.xml",2));
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/completeTests_real/pergamon/expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }   

    @Test
    public void testSAR() throws FileNotFoundException {
        X3MLEngine engine = engine("/completeTests_real/SAR/mappings.x3ml");
        X3MLEngine.Output output = engine.execute(document("/completeTests_real/SAR/input.xml"),policy("/completeTests_real/SAR/generator-policy.xml",2));
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/completeTests_real/SAR/expectedOutput.rdf");
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\nLINES:"+ diff.size() + "\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }   
}