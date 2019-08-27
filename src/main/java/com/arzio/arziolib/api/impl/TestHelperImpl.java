package com.arzio.arziolib.api.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.arzio.arziolib.api.Test;
import com.arzio.arziolib.api.TestCase;
import com.arzio.arziolib.api.TestCommand;
import com.arzio.arziolib.api.TestHelper;
import com.arzio.arziolib.api.util.TestException;
import com.arzio.arziolib.api.util.reflection.MethodParameters;

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

	@Override
	public void addTestCases(Plugin plugin, final Object from) {
		Validate.notNull(plugin, "Plugin must not be null!");
		Validate.notNull(from, "Object instance must not be null!");

		for (final Method method : from.getClass().getDeclaredMethods()){
			if (method.isAnnotationPresent(TestCommand.class)){
				TestCommand annotation = method.getAnnotation(TestCommand.class);
				
				TestCase testCase = new TestCase(plugin, annotation.commandName(), new Test() {

					@Override
					public void test(CommandSender tester, Player player, TestHelper helper) throws Throwable {
						MethodParameters parameters = MethodParameters.getFrom(method);
						parameters.setParameter(CommandSender.class, tester);
						parameters.setParameter(Player.class, player);
						parameters.setParameter(TestHelper.class, helper);

						method.invoke(from, parameters.getValues());
					}

				});

				this.addTestCase(testCase);
			}
			
		}
		
	}

}
