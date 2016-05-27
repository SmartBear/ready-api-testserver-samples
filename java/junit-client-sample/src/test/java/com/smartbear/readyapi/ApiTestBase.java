package com.smartbear.readyapi;

import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.execution.Execution;
import com.smartbear.readyapi.client.execution.RecipeExecutor;
import com.smartbear.readyapi.client.execution.Scheme;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.result.TestStepResult;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ApiTestBase {

    final static Logger LOG = LoggerFactory.getLogger(ApiTestBase.class);
    static RecipeExecutor executor;

    @BeforeClass
    public static void initExecutor() throws MalformedURLException {
        String hostName = System.getProperty("testserver.host", "http://testserver.readyapi.io:8080");
        String user = System.getProperty("testserver.user", "demoUser");
        String password = System.getProperty("testserver.password", "demoPassword");

        URL url = new URL(hostName);

        executor = new RecipeExecutor(Scheme.valueOf(url.getProtocol().toUpperCase()),
            url.getHost(), url.getPort());
        executor.setCredentials(user, password);
    }

    void executeAndAssert(TestRecipe recipe) {
        LOG.debug("Executing recipe: " + recipe.toString());

        Execution execution = executor.executeRecipe(recipe);

        for (TestStepResult result : execution.getExecutionResult().getTestStepResults()) {
            LOG.debug("Response content for TestStep [" + result.getTestStepName() + "]: " +
                result.getResponseContent());
        }

        assertEquals(Arrays.toString(execution.getErrorMessages().toArray()),
            ProjectResultReport.StatusEnum.FINISHED, execution.getCurrentStatus());
    }

}
