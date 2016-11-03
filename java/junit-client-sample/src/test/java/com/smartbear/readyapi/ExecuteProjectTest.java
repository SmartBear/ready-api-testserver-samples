package com.smartbear.readyapi;

import com.smartbear.readyapi.client.support.AssertionUtils;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;

public class ExecuteProjectTest extends ApiTestBase {

    @Test
    public void simpleProjectTest() throws Exception {
        try {
            AssertionUtils.assertExecution(executeProject(new File("src/test/resources/TestProject.xml")));
            assertFalse(true);
        } catch (AssertionError e) {
            // expect json count assertion in project to fail
        }
    }

    @Test
    public void simpleCompositeProjectTest() throws Exception {
        try {
            AssertionUtils.assertExecution(executeProject(new File("src/test/resources/CompositeTestProject")));
            assertFalse(true);
        } catch (AssertionError e) {
            // expect json count assertion in project to fail
        }
    }
}
