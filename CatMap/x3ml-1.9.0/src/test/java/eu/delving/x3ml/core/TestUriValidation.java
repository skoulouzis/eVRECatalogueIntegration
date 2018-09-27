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
package eu.delving.x3ml.core;

import gr.forth.UriValidator;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 * @author Nikos Minadakis (minadakn 'at' ics 'dot' forth 'dot' gr)
 */
public class TestUriValidation {
    private static final String VALID_STR_1="http://valid";
    private static final String VALID_STR_2="https://valid";
    private static final String VALID_STR_3="ftp://valid";
    private static final String VALID_STR_4="mailto:valid";
    private static final String VALID_STR_5="urn:uuid:123";
    private static final String VALID_STR_6="http://va lid";
    private static final String VALID_STR_7="http://va|lid";
    
    private static final String INVALID_STR_1="http ://invalid";
    private static final String INVALID_STR_2="http//invalid";
    private static final String INVALID_STR_3="invalid";
    private static final String INVALID_STR_4="someone@example.com";
    

    @Test
    public void testUriValidation(){
        assertTrue(UriValidator.isValid(VALID_STR_1));
        assertTrue(UriValidator.isValid(VALID_STR_2));
        assertTrue(UriValidator.isValid(VALID_STR_3));
        assertTrue(UriValidator.isValid(VALID_STR_4));
        assertTrue(UriValidator.isValid(VALID_STR_5));
        assertTrue(UriValidator.isValid(VALID_STR_6));
        assertTrue(UriValidator.isValid(VALID_STR_7));
        
        assertTrue(!UriValidator.isValid(INVALID_STR_1));
        assertTrue(!UriValidator.isValid(INVALID_STR_2));
        assertTrue(!UriValidator.isValid(INVALID_STR_3));
        assertTrue(!UriValidator.isValid(INVALID_STR_4));
    }   
}