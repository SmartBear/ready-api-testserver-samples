package com.smartbear.readyapi.testserver;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.execution.Execution;
import com.smartbear.readyapi.client.model.Assertion;
import com.smartbear.readyapi.client.model.Parameter;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.ResponseSLAAssertion;
import com.smartbear.readyapi.client.model.RestTestRequestStep;
import com.smartbear.readyapi.client.model.SimpleContainsAssertion;
import com.smartbear.readyapi.client.model.TestCase;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi.client.model.ValidHttpStatusCodesAssertion;
import com.smartbear.readyapi.client.teststeps.TestStepTypes;
import com.smartbear.readyapi.client.teststeps.TestSteps;
import com.smartbear.readyapi.testserver.CucumberRecipeExecutor;
import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@ScenarioScoped
public class GenericRestStepDefs {

    private final CucumberRecipeExecutor executor;
    private String endpoint;
    private String method;
    private String path;
    private String requestBody;
    private RestTestRequestStep testStep;
    private List<Assertion> assertions = Lists.newArrayList();
    private List<Parameter> parameters = Lists.newArrayList();

    @Inject
    public GenericRestStepDefs(CucumberRecipeExecutor executor ){
        this.executor = executor;
    }

    @Given("^the API running at (.*)$")
    public void theAPIRunningAt( String endpoint ) throws Throwable {
        this.endpoint = endpoint;
    }

    @When("^a (.*) request to (.*) is made$")
    public void aPOSTRequestToPetIsMade( String method, String path ) throws Throwable {
        this.method = method;
        this.path = path;
    }

    @And("^the request body is$")
    public void theRequestBodyIs( String requestBody ) throws Throwable {
        this.requestBody = requestBody;
    }

    @Then("^a (\\d+) response is returned$")
    public void aResponseIsReturned(int statusCode) throws Throwable {
        aResponseIsReturnedWithin( statusCode, 0 );
    }

    @Then("^a (\\d+) response is returned within (\\d+)ms$")
    public void aResponseIsReturnedWithin(int statusCode, int timeout ) throws Throwable {
        testStep = new RestTestRequestStep();
        testStep.setURI( endpoint + path );
        testStep.setMethod( method.toUpperCase() );
        testStep.setRequestBody( requestBody );
        testStep.setType(TestStepTypes.REST_REQUEST.getName());

        ValidHttpStatusCodesAssertion httpStatusCodesAssertion = new ValidHttpStatusCodesAssertion();
        httpStatusCodesAssertion.setValidStatusCodes( Arrays.asList(statusCode) );
        httpStatusCodesAssertion.setType("Valid HTTP Status Codes" );
        assertions.add( httpStatusCodesAssertion );

        if( timeout > 0 ) {
            ResponseSLAAssertion slaAssertion = new ResponseSLAAssertion();
            slaAssertion.setMaxResponseTime(timeout);
            slaAssertion.setType("Response SLA");
            assertions.add( slaAssertion );
        }

        executor.setTestStep( testStep );
    }

    @And("^the response body contains$")
    public void theResponseBodyIs( String responseBody ) throws Throwable {
        SimpleContainsAssertion contentAssertion = new SimpleContainsAssertion();
        contentAssertion.setToken( responseBody.trim() );
        contentAssertion.setType( "Contains");
        assertions.add( contentAssertion );
    }

    @After
    public void runTest(){
        if( !parameters.isEmpty()){
            executor.setParameters( parameters );
        }

        if( !assertions.isEmpty()) {
            executor.setAssertions(assertions);
        }

        executor.runTestCase();
    }

    @And("^the (.*) parameter is (.*)$")
    public void theParameterIs( String name, String value ) throws Throwable {
        parameters.add( createParameter( "QUERY", name, value) );
    }

    @And("^the (.*) header is (.*)$")
    public void theHeaderIs( String name, String value ) throws Throwable {
        parameters.add( createParameter( "HEADER", name, value) );
    }

    private Parameter createParameter( String type, String name, String value ){
        Parameter parameter = new Parameter();
        parameter.setType(type);
        parameter.setName( name );
        parameter.setValue( value );

        return parameter;
    }

    @And("^the type is (.*)$")
    public void theTypeIs( String type) throws Throwable {

        if( testStep != null ){
            testStep.setEncoding( type );
        }
    }
}
