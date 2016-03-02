package com.smartbear.readyapi.testserver;

import com.google.inject.Singleton;
import com.smartbear.readyapi.client.execution.RecipeExecutor;
import com.smartbear.readyapi.client.execution.Scheme;

@Singleton
public class CucumberRecipeExecutor extends RecipeExecutor {

    public CucumberRecipeExecutor() {
        super(Scheme.HTTP, "testserver.readyapi.io", 8080 );

        String user = System.getProperty( "testserver.user", "demoUser" );
        String password = System.getProperty( "testserver.password", "demoPassword" );

        setCredentials( user, password );
    }
}
