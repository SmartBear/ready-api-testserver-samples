package com.smartbear.readyapi.testserver;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.execution.Execution;
import com.smartbear.readyapi.client.model.Assertion;
import com.smartbear.readyapi.client.model.GroovyScriptAssertion;
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
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.swagger.util.Json;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@ScenarioScoped
public class GenericRestStepDefs {

    private final CucumberRecipeExecutor executor;
    private final SwaggerCache swaggerCache;
    private String endpoint;
    private String method;
    private String path;
    private String requestBody;
    private RestTestRequestStep testStep;
    private List<Assertion> assertions = Lists.newArrayList();
    private List<Parameter> parameters = Lists.newArrayList();
    private Map<String,String> bodyValues = Maps.newHashMap();
    private Swagger swagger;
    private Operation swaggerOperation;

    @Inject
    public GenericRestStepDefs(CucumberRecipeExecutor executor, SwaggerCache swaggerCache ){
        this.executor = executor;
        this.swaggerCache = swaggerCache;
    }

    @Given("^the Swagger definition at (.*)$")
    public void theSwaggerDefinitionAt( String swaggerUrl ) throws Throwable {

        swagger = swaggerCache.getSwagger( swaggerUrl );

        if( swagger.getHost() != null ) {
            endpoint = swagger.getSchemes().get(0).name().toLowerCase() + "://" + swagger.getHost();
            if( swagger.getBasePath() != null ) {
                endpoint += swagger.getBasePath();
            }
        }
    }

    @Given("^the API running at (.*)$")
    public void theAPIRunningAt( String endpoint ) throws Throwable {
        this.endpoint = endpoint;
    }

    @When("^a (.*) request to ([^ ]*) is made$")
    public void aRequestToPathIsMade( String method, String path ) throws Throwable {
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

    @Then("^the response is (.*)$")
    public void theResponseIs(String responseDescription) throws Throwable {
        if( swaggerOperation == null ){
            throw new Exception( "missing swagger operation for request");
        }

        for( String responseCode : swaggerOperation.getResponses().keySet()){
            Response response = swaggerOperation.getResponses().get(responseCode);
            if( responseDescription.equalsIgnoreCase(response.getDescription())){
                aResponseIsReturned( Integer.parseInt(responseCode));
            }
        }
    }

    @Then("^a (\\d+) response is returned within (\\d+)ms$")
    public void aResponseIsReturnedWithin(int statusCode, int timeout ) throws Throwable {
        testStep = new RestTestRequestStep();
        testStep.setURI( endpoint + path );
        testStep.setMethod( method.toUpperCase() );
        if( requestBody != null ) {
            testStep.setRequestBody(requestBody);
        }

        if( !bodyValues.isEmpty()){
            StringWriter writer = new StringWriter();
            Json.mapper().writer().writeValue( writer, bodyValues );
            testStep.setRequestBody( writer.toString() );
            testStep.setMediaType("application/json");
        }

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

        String type = path.contains( "{" + name + "}") ? "PATH" : "QUERY";
        parameters.add(createParameter(type, name, value));
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

    @And("^([^ ]*) is (.*)$")
    public void bodyParameterIs( String name, String value ) throws Throwable {

        if( swaggerOperation != null ){
            for( io.swagger.models.parameters.Parameter parameter : swaggerOperation.getParameters()){
               if( parameter.getName().equalsIgnoreCase( name )){
                   if( parameter.getIn().equals("query")){
                       parameters.add( createParameter( "QUERY", name, value ));
                   }
                   else if( parameter.getIn().equals("path")){
                       parameters.add( createParameter( "PATH", name, value ));
                   }
                   else if( parameter.getIn().equals("header")){
                       parameters.add( createParameter( "HEADER", name, value ));
                   }
                   else if( parameter.getIn().equals("body")){
                       requestBody = value;
                   }

                   return;
               }
            }
        }

        bodyValues.put( name, value );
    }

    @When("^a request to ([^ ]*) is made$")
    public void aRequestToOperationIsMade( String operationId ) throws Throwable {
        if( swagger == null ){
            throw new Exception( "Missing Swagger definition");
        }

        for( String resourcePath : swagger.getPaths().keySet()){
            Path path = swagger.getPath( resourcePath );
            for(HttpMethod httpMethod : path.getOperationMap().keySet()){
                Operation operation = path.getOperationMap().get(httpMethod);
                if( operationId.equalsIgnoreCase(operation.getOperationId())){
                    method = httpMethod.name().toUpperCase();
                    this.path = resourcePath;
                    swaggerOperation = operation;
                }
            }
        }

        if( swaggerOperation == null ){
            throw new Exception( "Could not find operation [" + operationId + "] in Swagger definition");
        }
    }

    @And("^the request expects (.*)")
    public void theRequestExpects( String format ) throws Throwable {
        format = expandFormat(format);
        theHeaderIs( "Accept", format );
    }

    private String expandFormat(String format) {
        if( format.indexOf('/') == -1 ){
            return "application/" + format;
        }

        return format;
    }

    @And("^the response type is (.*)$")
    public void theResponseTypeIs( String format ) throws Throwable {
        GroovyScriptAssertion scriptAssertion = new GroovyScriptAssertion();
        scriptAssertion.setType( "Script Assertion" );
        scriptAssertion.setScript(
            "assert messageExchange.responseHeaders[\"Content-Type\"].contains( \"" + expandFormat( format ) + "\")" );

        assertions.add( scriptAssertion );
    }

    @And("^the response contains a (.*) header$")
    public void theResponseContainsHeader( String header ) throws Throwable {
        GroovyScriptAssertion scriptAssertion = new GroovyScriptAssertion();
        scriptAssertion.setType( "Script Assertion" );
        scriptAssertion.setScript(
            "assert messageExchange.responseHeaders.containsKey(\"" + header + "\")" );

        assertions.add( scriptAssertion );
    }

    @And("^the response (.*) header is (.*)$")
    public void theResponseTypeIs( String header, String value ) throws Throwable {
        GroovyScriptAssertion scriptAssertion = new GroovyScriptAssertion();
        scriptAssertion.setType( "Script Assertion" );
        scriptAssertion.setScript(
            "assert messageExchange.responseHeaders[\"" + header + "\"].contains( \"" + value + "\")" );

        assertions.add( scriptAssertion );
    }
}
