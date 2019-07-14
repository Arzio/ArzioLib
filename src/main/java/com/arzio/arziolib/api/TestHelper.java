package com.arzio.arziolib.api;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import com.arzio.arziolib.api.util.TestException;

public interface TestHelper {
    public void addTestCase(TestCase testCase);

    public boolean hasTestCase(String commandName);
    public boolean hasTestCase(TestCase testCase);
    
    public List<TestCase> getAllTestCases();
    public List<TestCase> getTestCases(Plugin plugin);
    public List<TestCase> getTestCases(String group);
    
    public TestCase getTestCase(String commandName);
    
    public void clearTestCases(Plugin plugin);
    public void clearTestCases(String group);
    
    public void removeTestCase(TestCase testCase);
    public void removeTestCase(String commandName);
    
    public void runTestCases(String group, CommandSender tester) throws TestException;
    public void runTestCases(Plugin plugin, CommandSender tester) throws TestException;
    public void runTestCases(List<TestCase> testCases, CommandSender tester) throws TestException;
}
