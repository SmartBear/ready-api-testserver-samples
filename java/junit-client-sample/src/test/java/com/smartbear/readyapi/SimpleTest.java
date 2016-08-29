package com.smartbear.readyapi;

import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.execution.Execution;
import com.smartbear.readyapi.client.teststeps.soaprequest.SoapRequestStepBuilder;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.teststeps.TestSteps.getRequest;
import static com.smartbear.readyapi.client.teststeps.TestSteps.soapRequest;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class SimpleTest {

    @Test
    public void simpleCountTest() throws Exception {
        TestRecipe recipe = newTestRecipe()
            .addStep(
                getRequest("https://api.swaggerhub.com/apis")
                    .addQueryParameter("query", "testserver")
                    .assertJsonContent("$.totalCount", "3")
            )
            .buildTestRecipe();

        TestServerSupport.executeAndAssert(recipe);
    }

    @Test
    public void simpleTest() throws Exception {
        TestRecipe recipe = newTestRecipe()
            .addStep(
                getRequest("https://api.swaggerhub.com/apis")
                    .assertValidStatusCodes(200)
            )
            .buildTestRecipe();

        TestServerSupport.executeAndAssert(recipe);
    }

    @Test
    public void simpleSoapTest() throws Exception {

        SoapRequestStepBuilder soapRequest = soapRequest(new URL("http://www.webservicex.com/globalweather.asmx?WSDL"))
            .forBinding("GlobalWeatherSoap12")
            .forOperation("GetWeather")
            .withParameter("CountryName", "Sweden")
            .withPathParameter("//*:CityName", "Stockholm")
            .assertSoapOkResponse()
            .assertSchemaCompliance();

        TestRecipe recipe = newTestRecipe().addStep( soapRequest ).buildTestRecipe();
        TestServerSupport.executeAndAssert(recipe);
    }

    @Test
    public void simpleProjectTest() throws Exception {
        Execution execution = TestServerSupport.executeProject(new File("src/test/resources/TestProject.xml"));

        assertNotNull( execution );

        try {
            TestServerSupport.assertExecution(execution);
            assertFalse( true );
        }
        catch( AssertionError e ){
        }
    }

    @Test
    public void simpleCompositeProjectTest() throws Exception {
        Execution execution = TestServerSupport.executeProject(new File("src/test/resources/CompositeTestProject"));
        assertNotNull( execution );

        try {
            TestServerSupport.assertExecution(execution);
            assertFalse( true );
        }
        catch( AssertionError e ){
        }
    }
}
