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
package org.switchyard.component.test.mixins.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.h2.jdbcx.JdbcDataSource;
import org.switchyard.SwitchYardException;
import org.switchyard.component.test.mixins.naming.NamingMixIn;
import org.switchyard.test.MixInDependencies;
import org.switchyard.test.mixins.AbstractTestMixIn;

/**
 * SwitchYard test mixin providing a H2 test database and datasource registered under the JNDI name
 * "java:jboss/datasources/ExampleDS", like the example datasource that comes preconfigured
 * with WildFly, JBoss EAP or JBoss Fuse Service Works.
 * 
 * @author Daniel Tschan <tschan@puzzle.ch>
 */
@MixInDependencies(required = { NamingMixIn.class, TransactionMixIn.class })
public class H2DataSourceMixIn extends AbstractTestMixIn {

    private static final String DATASOURCE_JNDI_NAME = "java:jboss/datasources/ExampleDS";
    private static final String DATASOURCE_URL = "jdbc:h2:mem:test";
    
    // Used to control H2 lifecycle. H2 DB will be destroyed after last connection close.
    private Connection _connection;

    @Override
    public void initialize() {
        try {

            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL(DATASOURCE_URL);
            ds.setUser("sa");
            ds.setPassword("sa");

            InitialContext initialContext = new InitialContext();
            initialContext.bind(DATASOURCE_JNDI_NAME, ds);

            _connection = getConnection();
        } catch (NamingException e) {
            throw new SwitchYardException(e);
        }
    }

    @Override
    public void uninitialize() {
        try {
            if (!_connection.isClosed()) {
                _connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Open a new connection to the H2 example datasource.
     * 
     * @return the newly opened connection.
     */
    public static Connection getConnection() {
        try {
            return ((JdbcDataSource) new InitialContext().lookup(DATASOURCE_JNDI_NAME)).getConnection();
        } catch (NamingException e) {
            throw new SwitchYardException(e);
        } catch (SQLException e) {
            throw new SwitchYardException(e);
        }
    }
}
