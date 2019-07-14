package com.arzio.arziolib.api.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import com.arzio.arziolib.api.TestCase;
import com.arzio.arziolib.api.TestHelper;
import com.arzio.arziolib.api.util.TestException;

public class TestHelperImpl implements TestHelper{

    private List<TestCase> testCases = new ArrayList<>();
    
    @Override
    public void addTestCase(TestCase testCase) {
        testCases.remove(testCase);
        testCases.add(testCase);
    }

    @Override
    public boolean hasTestCase(String commandName) {
        return this.getTestCase(commandName) != null;
    }

    @Override
    public boolean hasTestCase(TestCase testCase) {
        return this.testCases.contains(testCase);
    }

    @Override
    public List<TestCase> getTestCases(Plugin plugin) {
        List<TestCase> testsFromPlugin = new ArrayList<>();
        
        for (TestCase testCase : this.testCases) {
            if (testCase.getPluginFrom() == plugin) {
                testsFromPlugin.add(testCase);
            }
        }
        
        return Collections.unmodifiableList(testsFromPlugin);
    }

    @Override
    public List<TestCase> getTestCases(String group) {
        List<TestCase> testsFromPlugin = new ArrayList<>();
        
        for (TestCase testCase : this.testCases) {
            if (testCase.getGroupName().equals(group)) {
                testsFromPlugin.add(testCase);
            }
        }
        
        return Collections.unmodifiableList(testsFromPlugin);
    }

    @Override
    public List<TestCase> getAllTestCases() {
        List<TestCase> allTests = new ArrayList<>();
        allTests.addAll(this.testCases);
        
        return Collections.unmodifiableList(allTests);
    }

    @Override
    public TestCase getTestCase(String commandName) {
        for (TestCase testCase : this.testCases) {
            if (testCase.getCommandName().equalsIgnoreCase(commandName)) {
                return testCase;
            }
        }
        return null;
    }

    @Override
    public void clearTestCases(Plugin plugin) {
        for (TestCase testCase : this.getTestCases(plugin)) {
            this.removeTestCase(testCase);
        }
    }

    @Override
    public void clearTestCases(String group) {
        for (TestCase testCase : this.getTestCases(group)) {
            this.removeTestCase(testCase);
        }
    }

    @Override
    public void removeTestCase(TestCase testCase) {
        this.testCases.remove(testCase);
    }

    @Override
    public void removeTestCase(String commandName) {
        TestCase testCase = this.getTestCase(commandName);
        
        if (testCase != null) {
            this.removeTestCase(testCase);
        }
    }

    @Override
    public void runTestCases(String group, CommandSender tester) throws TestException {
        this.runTestCases(this.getTestCases(group), tester);
    }

    @Override
    public void runTestCases(Plugin plugin, CommandSender tester) throws TestException {
        this.runTestCases(this.getTestCases(plugin), tester);
    }

    @Override
    public void runTestCases(List<TestCase> testCases, CommandSender tester) throws TestException {
        for (TestCase testCase : testCases) {
            testCase.run(tester);
        }
    }

}
