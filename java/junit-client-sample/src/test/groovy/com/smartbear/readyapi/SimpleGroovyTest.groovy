package com.smartbear.readyapi

import com.smartbear.readyapi.client.TestRecipe

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe
import static com.smartbear.readyapi.client.teststeps.TestSteps.getRequest

class SimpleGroovyTest extends GroovyTestCase {

    public void testSimpleCount() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(
                getRequest("https://api.swaggerhub.com/apis")
                        .addQueryParameter("query", "testserver")
                        .assertJsonContent('$.totalCount', "3")
        ).buildTestRecipe();

        TestServerSupport.executeAndAssert(recipe);
    }
}