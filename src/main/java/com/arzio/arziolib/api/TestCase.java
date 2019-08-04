package com.arzio.arziolib.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.event.test.TestCaseRunEvent;
import com.arzio.arziolib.api.util.TestException;
import com.arzio.arziolib.api.util.TestException.TestExceptionType;

public class TestCase {

	private CommandSender currentTester;
	private Plugin pluginFrom;
	private String commandName;
	private String groupName;
	private List<Test> tests = new ArrayList<>();
	private TestCaseStatus lastStatus = TestCaseStatus.NONE;
	
	public static enum TestCaseStatus {
		NONE,
		FAILED,
		IGNORED,
		PASSED;
	}
	
	public TestCase(Plugin pluginFrom, String commandName, Test... testCaseTests) {
		if (testCaseTests == null || testCaseTests.length == 0) {
			throw new IllegalArgumentException("A Test Case must contain at least one Test!");
		}
		
		this.pluginFrom = pluginFrom;
		this.commandName = commandName.toLowerCase();
		this.groupName = "default";
		
		for (Test test : testCaseTests) {
			test.setReferencedTestCase(this);
			this.tests.add(test);
		}
		
		this.reorderTestsId();
	}
	
	public boolean hasRunBefore() {
		return this.getLastStatus() != TestCaseStatus.NONE;
	}
	
	public TestCaseStatus getLastStatus() {
		return lastStatus;
	}

	private void reorderTestsId() {
		int id = 1;
		for (Test test : tests) {
			test.setId(id++);
		}
	}
	
	public TestCase(Plugin pluginFrom, String commandName, String groupName, Test... test) {
		this(pluginFrom, commandName, test);
		this.groupName = groupName;
	}

	public Plugin getPluginFrom() {
		return pluginFrom;
	}

	public void setPluginFrom(Plugin pluginFrom) {
		this.pluginFrom = pluginFrom;
	}

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName.toLowerCase();
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public void run(CommandSender sender) throws TestException {
		this.reorderTestsId();
		
		Player player = null;
		
		if (sender instanceof Player) {
			player = (Player) sender;
		}

		this.setCurrentTester(sender);
		
		TestCaseRunEvent runEvent = new TestCaseRunEvent(this);
		Bukkit.getPluginManager().callEvent(runEvent);
		
		try {
			if (!runEvent.isCancelled()) {
				for (Test test : this.tests) {
					test.safeTest(sender, player, ArzioLib.getInstance().getTestHelper());
				}
				
				for (Test additionalTest : runEvent.getAdditionalTests()) {
					additionalTest.safeTest(sender, player, ArzioLib.getInstance().getTestHelper());
				}
			} else {
				sender.sendMessage("§6The test case "+this.getCommandName()+" got cancelled by a plugin.");
				throw new TestException(TestExceptionType.IGNORED, this, null);
			}
		} catch (TestException e) {
			if (e.getType() == TestExceptionType.FAILED) {
				this.lastStatus = TestCaseStatus.FAILED;
			} else if (e.getType() == TestExceptionType.IGNORED) {
				this.lastStatus = TestCaseStatus.IGNORED;
			}
			
			throw e;
		}
		this.lastStatus = TestCaseStatus.PASSED;
	}
	
	public void setCurrentTester(CommandSender sender) {
		this.currentTester = sender;
	}
	
	public CommandSender getCurrentTester() {
		return this.currentTester;
	}

	public List<Test> getTests() {
		return tests;
	}

	public void setTests(List<Test> tests) {
		this.tests = tests;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commandName == null) ? 0 : commandName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestCase other = (TestCase) obj;
		if (commandName == null) {
			if (other.commandName != null)
				return false;
		} else if (!commandName.equals(other.commandName))
			return false;
		return true;
	}
	
}
