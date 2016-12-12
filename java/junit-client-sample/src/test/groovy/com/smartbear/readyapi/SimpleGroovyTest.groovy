package com.smartbear.readyapi

import com.smartbear.readyapi.client.TestRecipe

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe
import static com.smartbear.readyapi.client.teststeps.TestSteps.getRequest

class SimpleGroovyTest extends GroovyTestCase {
    public void testSimpleCount() throws Exception {

        ApiTestBase.initExecutor()

        TestRecipe recipe = newTestRecipe()
                .addStep(
                getRequest("https://api.swaggerhub.com/apis")
                        .addQueryParameter("query", "testserver")
                        .assertJsonContent('$.totalCount', "4")
        ).buildTestRecipe();

        ApiTestBase.executeAndAssert(recipe);
    }
}