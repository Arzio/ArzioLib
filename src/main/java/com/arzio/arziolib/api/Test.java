package com.arzio.arziolib.api;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.arzio.arziolib.api.util.TestException;
import com.arzio.arziolib.api.util.TestException.TestExceptionType;

public abstract class Test {
	
	private TestCase referencedTestCase;
	private int id;
	
	public abstract void test(CommandSender tester, Player player, TestHelper helper) throws Throwable;
	
	public void setReferencedTestCase(TestCase testCase) {
		this.referencedTestCase = testCase;
	}
	
	public TestCase getReferencedTestCase() throws IllegalStateException {
		if (this.referencedTestCase == null) {
			throw new IllegalStateException("The referenced test case is null!");
		}
		return this.referencedTestCase;
	}
	
	public void send(Object... objects) {
		for (Object o : objects) {
			this.getReferencedTestCase().getCurrentTester().sendMessage(o.toString());
		}
	}
	
	public void fail(String reason) throws TestException {
		throw new TestException(TestExceptionType.FAILED, this.getReferencedTestCase(), this, reason);
	}
	
	public void fail() throws TestException {
		throw new TestException(TestExceptionType.FAILED, this.getReferencedTestCase(), this);
	}
	
	public void fakeSafe(Runnable runnable) throws Throwable {
		try {
			runnable.run();
		} catch (Throwable t) {
			throw new TestException(TestExceptionType.FAILED, this.getReferencedTestCase(), this, t);
		}
	}
	
	public void safeTest(CommandSender tester, Player player, TestHelper helper) throws TestException {
		try {
			this.test(tester, player, helper);
		} catch (Throwable t) {
			if (t instanceof TestException) {
				throw (TestException) t;
			} else {
				throw new TestException(TestExceptionType.FAILED, this.getReferencedTestCase(), this, t);
			}
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((referencedTestCase == null) ? 0 : referencedTestCase.hashCode());
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
		Test other = (Test) obj;
		if (id != other.id)
			return false;
		if (referencedTestCase == null) {
			if (other.referencedTestCase != null)
				return false;
		} else if (!referencedTestCase.equals(other.referencedTestCase))
			return false;
		return true;
	}
	
}
