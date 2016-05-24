package com.smartbear.readyapi;

import com.smartbear.readyapi.client.TestRecipe;
import com.smartbear.readyapi.client.execution.Execution;
import com.smartbear.readyapi.client.model.ProjectResultReport;
import org.junit.Test;

import java.util.Arrays;

import static com.smartbear.readyapi.client.TestRecipeBuilder.newTestRecipe;
import static com.smartbear.readyapi.client.teststeps.TestSteps.dataGenDataSource;
import static com.smartbear.readyapi.client.teststeps.TestSteps.getRequest;
import static com.smartbear.readyapi.client.teststeps.datasource.datagen.DataGenerators.cityTypeProperty;
import static com.smartbear.readyapi.client.teststeps.datasource.datagen.DataGenerators.mac48ComputerAddressTypeProperty;
import static com.smartbear.readyapi.client.teststeps.datasource.datagen.DataGenerators.randomIntegerTypeProperty;
import static org.junit.Assert.assertEquals;

public class DataGenDataSourceTest extends ApiTestBase {

    @Test
    public void createRecipeForAllDataGenerators() throws Exception {
        TestRecipe recipe = newTestRecipe()
                .addStep(dataGenDataSource()
                        .withNumberOfRows(34)
                        .withProperties(
                                cityTypeProperty("cityProperty")
                                        .duplicatedBy(2)
                        )
                        .andProperty(mac48ComputerAddressTypeProperty("computerAddressProperty"))
                        .andProperty(randomIntegerTypeProperty("integerProperty")
                        )
                        .named("DataSourceStep")
                        .addTestStep(getRequest("http://www.google.se/")
                                .addQueryParameter("a", "${DataSourceStep#cityProperty}")
                                .addQueryParameter("b", "${DataSourceStep#computerAddressProperty}")
                                .addQueryParameter("c", "${DataSourceStep#integerProperty}"))
                )
                .buildTestRecipe();
        System.out.println(recipe);
        Execution execution = executor.executeRecipe(recipe);

        assertEquals(Arrays.toString(execution.getErrorMessages().toArray()),
                ProjectResultReport.StatusEnum.FINISHED, execution.getCurrentStatus());
    }
}
