package com.smartbear.readyapi;

import com.smartbear.readyapi.client.execution.RecipeExecutor;
import com.smartbear.readyapi.client.execution.Scheme;
import org.junit.BeforeClass;

import java.net.MalformedURLException;
import java.net.URL;

public class ApiTestBase {

    static RecipeExecutor executor;

    @BeforeClass
    public static void initExecutor() throws MalformedURLException {
        String hostName = System.getProperty( "testserver.host", "http://testserver.readyapi.io:8080" );
        String user = System.getProperty( "testserver.user", "demoUser" );
        String password = System.getProperty( "testserver.password", "demoPassword" );

        URL url = new URL( hostName );

        SimpleNonFluentTest.executor = new RecipeExecutor( Scheme.valueOf(url.getProtocol().toUpperCase()),
            url.getHost(), url.getPort() );
        SimpleNonFluentTest.executor.setCredentials( user, password );
    }
}
