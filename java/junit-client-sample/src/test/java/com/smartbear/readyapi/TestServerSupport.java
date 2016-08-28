package com.smartbear.readyapi;

import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.execution.Execution;
import com.smartbear.readyapi.client.execution.RecipeExecutor;
import com.smartbear.readyapi.client.execution.Scheme;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.result.TestStepResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestServerSupport {

    final static Logger LOG = LoggerFactory.getLogger(TestServerSupport.class);
    static RecipeExecutor executor;

    public static void initExecutor() throws MalformedURLException {
        String hostName = System.getProperty("testserver.host", "http://testserver.readyapi.io:8080");
        String user = System.getProperty("testserver.user", "demoUser");
        String password = System.getProperty("testserver.password", "demoPassword");

        URL url = new URL(hostName);

        executor = new RecipeExecutor(Scheme.valueOf(url.getProtocol().toUpperCase()),
            url.getHost(), url.getPort());
        executor.setCredentials(user, password);
    }

    public static void executeAndAssert(TestRecipe recipe) throws Exception {
        LOG.debug("Executing recipe: " + recipe.toString());
        assertExecution(getExecutor().executeRecipe(recipe));
    }

    public static void assertExecution(Execution execution) {
        assertNotNull( execution );

        for (TestStepResult result : execution.getExecutionResult().getTestStepResults()) {
            LOG.debug("Response content for TestStep [" + result.getTestStepName() + "]: " +
                result.getResponseContent());
        }

        assertEquals(Arrays.toString(execution.getErrorMessages().toArray()),
            ProjectResultReport.StatusEnum.FINISHED, execution.getCurrentStatus());
    }

    public static Execution executeRecipe(TestRecipe recipe) throws Exception {
        return getExecutor().executeRecipe(recipe);
    }

    public static Execution executeProject(File file) throws Exception {
        return getExecutor().executeProject(file);
    }

    public static synchronized RecipeExecutor getExecutor() throws Exception {
        if (executor == null) {
            initExecutor();
        }
        return executor;
    }
}
