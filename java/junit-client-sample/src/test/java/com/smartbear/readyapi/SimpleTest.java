package com.smartbear.readyapi;

import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.execution.Execution;
import com.smartbear.readyapi.client.execution.RecipeExecutor;
import com.smartbear.readyapi.client.execution.Scheme;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.teststeps.TestSteps.getRequest;
import static org.junit.Assert.assertEquals;

public class SimpleTest
{
    private static RecipeExecutor executor;

    @BeforeClass
    public static void initExecutor() throws MalformedURLException {
        String hostName = System.getProperty( "testserver.host", "<your TestServer hostname>" );
        String user = System.getProperty( "testserver.user", "defaultUser" );
        String password = System.getProperty( "testserver.password", "defaultPassword" );

        URL url = new URL( hostName );

        executor = new RecipeExecutor( Scheme.valueOf(url.getProtocol().toUpperCase()),
            url.getHost(), url.getPort() );
        executor.setCredentials( user, password );
    }

    @Test
    public void simpleTest() throws Exception {
        TestRecipe recipe = newTestRecipe()
            .addStep(
                getRequest("https://api.swaggerhub.com/apis")
                    .addQueryParameter("query", "testserver")
                    .assertJsonContent("$.totalCount", "1" )
            )
            .buildTestRecipe();

        Execution execution = executor.executeRecipe(recipe);

        assertEquals(Arrays.toString( execution.getErrorMessages().toArray()),
            ProjectResultReport.StatusEnum.FINISHED, execution.getCurrentStatus());
    }
}
