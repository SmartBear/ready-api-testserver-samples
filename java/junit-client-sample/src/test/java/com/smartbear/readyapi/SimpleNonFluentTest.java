package com.smartbear.readyapi;

import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.execution.Execution;
import com.smartbear.readyapi.client.model.Assertion;
import com.smartbear.readyapi.client.model.JsonPathContentAssertion;
import com.smartbear.readyapi.client.model.Parameter;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.RestTestRequestStep;
import com.smartbear.readyapi.client.model.TestCase;
import com.smartbear.readyapi.client.model.TestStep;
import com.smartbear.readyapi.client.model.ValidHttpStatusCodesAssertion;
import com.smartbear.readyapi.client.teststeps.TestStepTypes;
import com.smartbear.readyapi.client.teststeps.TestSteps;
import com.smartbear.readyapi.client.teststeps.restrequest.BaseRestRequest;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * same tests as in SimpleTest but without using the fluent interfaces
 */

public class SimpleNonFluentTest extends ApiTestBase {

    @Test
    public void simpleCountTest() throws Exception {

        RestTestRequestStep restTestRequestStep = new RestTestRequestStep();
        restTestRequestStep.setURI( "https://api.swaggerhub.com/apis" );
        restTestRequestStep.setMethod( TestSteps.HttpMethod.GET.name() );
        restTestRequestStep.setType(TestStepTypes.REST_REQUEST.getName());

        Parameter parameter = new Parameter();
        parameter.setName( "query");
        parameter.setValue( "testserver");
        parameter.setType(BaseRestRequest.ParameterType.QUERY.name() );
        restTestRequestStep.setParameters( Arrays.asList(parameter));

        JsonPathContentAssertion assertion = new JsonPathContentAssertion();
        assertion.setJsonPath( "$.totalCount");
        assertion.setExpectedContent( "2");
        assertion.setType("JsonPath Match");

        restTestRequestStep.setAssertions(Arrays.<Assertion>asList( assertion ));

        TestCase testCase = new TestCase();
        testCase.setFailTestCaseOnError( true );
        testCase.setTestSteps(Arrays.<TestStep>asList( restTestRequestStep ));

        TestRecipe recipe = new TestRecipe( testCase );

        Execution execution = executor.executeRecipe(recipe);

        assertEquals(Arrays.toString( execution.getErrorMessages().toArray()),
            ProjectResultReport.StatusEnum.FINISHED, execution.getCurrentStatus());
    }

    @Test
    public void simpleTest() throws Exception {

        RestTestRequestStep restTestRequestStep = new RestTestRequestStep();
        restTestRequestStep.setURI( "https://api.swaggerhub.com/apis" );
        restTestRequestStep.setMethod( TestSteps.HttpMethod.GET.name() );
        restTestRequestStep.setType(TestStepTypes.REST_REQUEST.getName());

        ValidHttpStatusCodesAssertion assertion = new ValidHttpStatusCodesAssertion();
        assertion.setValidStatusCodes( Arrays.asList( 200 ));
        assertion.setType("Valid HTTP Status Codes");

        restTestRequestStep.setAssertions(Arrays.<Assertion>asList( assertion ));

        TestCase testCase = new TestCase();
        testCase.setFailTestCaseOnError( true );
        testCase.setTestSteps(Arrays.<TestStep>asList( restTestRequestStep ));

        TestRecipe recipe = new TestRecipe( testCase );

        Execution execution = executor.executeRecipe(recipe);

        assertEquals(Arrays.toString( execution.getErrorMessages().toArray()),
            ProjectResultReport.StatusEnum.FINISHED, execution.getCurrentStatus());
    }
}