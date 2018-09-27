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
import static org.junit.Assert.assertTrue;

/**
 * @author Gerald de Jong <gerald@delving.eu>
 * @author Yannis Marketakis <marketak@ics.forth.gr>
 * @author Nikos Minadakis <minadakn@ics.forth.gr>
 */
public class TestPosition {
    private final Logger log = Logger.getLogger(getClass());
    private final Generator VALUE_POLICY = X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(1));

    @Test
    public void testPositionInscriptionPath() {
        X3MLEngine engine = engine("/position/TestPositionInscriptionPath.x3ml");
        X3MLEngine.Output output = engine.execute(document("/position/Rijks1-Inscription3.xml"), policy("/position/RMA-gen-policy.xml"));
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/position/TestPositionInscriptionPath_rdf.xml");
//        System.out.println(StringUtils.join(expectedResult, "\n"));
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }

    @Test
    public void testPositionInscriptionA() {
        X3MLEngine engine = engine("/position/TestPositionInscription_a.x3ml");
        X3MLEngine.Output output = engine.execute(document("/position/Rijks1-Inscription3.xml"), policy("/position/RMA-gen-policy.xml"));
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/position/TestPositionInscription_a_rdf.xml"); 
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }
    
    @Test
    public void testPositionInscriptionB() {
        X3MLEngine engine = engine("/position/TestPositionInscription_b.x3ml");
        X3MLEngine.Output output = engine.execute(document("/position/Rijks1-Inscription3.xml"), policy("/position/RMA-gen-policy.xml"));
        String[] mappingResult = output.toStringArray();
        String[] expectedResult = xmlToNTriples("/position/TestPositionInscription_b_rdf.xml"); 
        List<String> diff = compareNTriples(expectedResult, mappingResult);
        assertTrue("\n" + StringUtils.join(diff, "\n") + "\n", errorFree(diff));
    }

}
