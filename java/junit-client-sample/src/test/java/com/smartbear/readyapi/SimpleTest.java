package com.smartbear.readyapi;

import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.execution.Execution;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.result.TestStepResult;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Arrays;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.teststeps.TestSteps.getRequest;
import static com.smartbear.readyapi.client.teststeps.TestSteps.soapRequest;
import static org.junit.Assert.assertEquals;

public class SimpleTest extends ApiTestBase
{
    private final static Logger LOG = LoggerFactory.getLogger( SimpleTest.class );

    @Test
    public void simpleCountTest() throws Exception {
        TestRecipe recipe = newTestRecipe()
            .addStep(
                getRequest("https://api.swaggerhub.com/apis")
                    .addQueryParameter("query", "testserver")
                    .assertJsonContent("$.totalCount", "3" )
            )
            .buildTestRecipe();

        executeAndAssert(recipe);
    }

    private void executeAndAssert(TestRecipe recipe) {
        LOG.debug("Executing recipe: " + recipe.toString());

        Execution execution = executor.executeRecipe(recipe);

        for(TestStepResult result : execution.getExecutionResult().getTestStepResults()){
            LOG.debug( "Response content for TestStep [" + result.getTestStepName() + "]: " +
                result.getResponseContent() );
        }

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

        executeAndAssert(recipe);
    }

    @Test
    public void simpleSoapTest() throws Exception {
        TestRecipe recipe = newTestRecipe()
            .addStep(soapRequest( new URL("http://www.webservicex.com/globalweather.asmx?WSDL"))
                .named("Soap Rulez")
                .forBinding("GlobalWeatherSoap12")
                .forOperation("GetWeather")
                .withParameter("CountryName", "Sweden")
                .withPathParameter("//*:CityName", "Stockholm")
                .assertSoapOkResponse()
                .assertSchemaCompliance()

            )
            .buildTestRecipe();

        executeAndAssert(recipe);
    }
}
