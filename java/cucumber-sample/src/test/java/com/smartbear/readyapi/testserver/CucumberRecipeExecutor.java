package com.smartbear.readyapi.testserver;

import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.execution.Execution;
import com.smartbear.readyapi.client.execution.RecipeExecutor;
import com.smartbear.readyapi.client.execution.Scheme;
import com.smartbear.readyapi.client.model.Assertion;
import com.smartbear.readyapi.client.model.Parameter;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import com.smartbear.readyapi.client.model.RestTestRequestStep;
import com.smartbear.readyapi.client.model.TestCase;
import com.smartbear.readyapi.client.model.TestStep;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CucumberRecipeExecutor extends RecipeExecutor {

    private RestTestRequestStep testStep;

    public CucumberRecipeExecutor() {
        super(Scheme.HTTP, "testserver.readyapi.io", 8080 );

        String user = System.getProperty( "testserver.user", "demoUser" );
        String password = System.getProperty( "testserver.password", "demoPassword" );

        setCredentials( user, password );
    }

    public void runTestCase() {
        if( testStep != null ) {
            TestCase testCase = new TestCase();
            testCase.setFailTestCaseOnError(true);
            testCase.setTestSteps(Arrays.<TestStep>asList(testStep));
            testStep = null;

            TestRecipe recipe = new TestRecipe(testCase);
            Execution execution = executeRecipe(recipe);

            assertEquals(Arrays.toString(execution.getErrorMessages().toArray()),
                ProjectResultReport.StatusEnum.FINISHED, execution.getCurrentStatus());
        }
    }

    public void setTestStep(RestTestRequestStep testStep) {
        this.testStep = testStep;
    }

    public RestTestRequestStep getTestStep() {
        return testStep;
    }

    public void setAssertions(List<Assertion> assertions) {
        if( testStep != null ) {
            testStep.setAssertions(assertions);
        }
    }

    public void setParameters(List<Parameter> parameters) {
        if( testStep != null ){
            testStep.setParameters( parameters );
        }
    }
}
