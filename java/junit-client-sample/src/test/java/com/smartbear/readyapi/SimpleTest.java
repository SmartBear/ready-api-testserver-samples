package com.smartbear.readyapi;

import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.execution.Execution;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import org.junit.Test;

import java.util.Arrays;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.teststeps.TestSteps.getRequest;
import static org.junit.Assert.assertEquals;

public class SimpleTest extends ApiTestBase
{
    @Test
    public void simpleCountTest() throws Exception {
        TestRecipe recipe = newTestRecipe()
            .addStep(
                getRequest("https://api.swaggerhub.com/apis")
                    .addQueryParameter("query", "testserver")
                    .assertJsonContent("$.totalCount", "2" )
            )
            .buildTestRecipe();

        Execution execution = executor.executeRecipe(recipe);

        assertEquals(Arrays.toString( execution.getErrorMessages().toArray()),
            ProjectResultReport.StatusEnum.FINISHED, execution.getCurrentStatus());
    }

    @Test
    public void simpleTest() throws Exception {
        TestRecipe recipe = newTestRecipe()
            .addStep(
                getRequest("https://api.swaggerhub.com/apis")
                    .assertValidStatusCodes( 200 )
            )
            .buildTestRecipe();

        Execution execution = executor.executeRecipe(recipe);

        assertEquals(Arrays.toString( execution.getErrorMessages().toArray()),
            ProjectResultReport.StatusEnum.FINISHED, execution.getCurrentStatus());
    }
}
