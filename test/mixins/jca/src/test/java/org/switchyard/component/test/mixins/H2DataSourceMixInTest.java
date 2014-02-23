/*
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.switchyard.component.test.mixins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.switchyard.component.test.mixins.naming.NamingMixIn;
import org.switchyard.component.test.mixins.transaction.H2DataSourceMixIn;

/**
 * Unit test for {@link H2DataSourceMixIn}.
 * 
 * @author Daniel Tschan <tschan@puzzle.ch>
 */
public class H2DataSourceMixInTest {
    private static H2DataSourceMixIn h2DataSourceMixIn;
    private static NamingMixIn namingMixIn;

    @BeforeClass
    public static void setup() {
        namingMixIn = new NamingMixIn();
        namingMixIn.initialize();
        h2DataSourceMixIn = new H2DataSourceMixIn();
        h2DataSourceMixIn.initialize();
    }

    @AfterClass
    public static void tearDown() {
        h2DataSourceMixIn.uninitialize();
    }

    @Test
    public void testH2DataSource() throws Exception {
        Connection connection =  H2DataSourceMixIn.getConnection();
        Statement statement = connection.createStatement();
        
        ResultSet resultSet = statement.executeQuery("select 1");
        assertTrue(resultSet.next());
        assertEquals(1, resultSet.getInt(1));
        
        statement.close();
        connection.close();
    }
}
