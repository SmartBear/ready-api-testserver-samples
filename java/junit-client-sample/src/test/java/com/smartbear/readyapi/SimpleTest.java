package com.smartbear.readyapi;

import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.execution.Execution;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import org.junit.Test;

import java.net.URL;
import java.util.Arrays;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.teststeps.TestSteps.getRequest;
import static com.smartbear.readyapi.client.teststeps.TestSteps.soapRequest;
import static org.junit.Assert.assertEquals;

public class SimpleTest extends ApiTestBase
{
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
