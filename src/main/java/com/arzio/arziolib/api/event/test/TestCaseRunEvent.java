package com.arzio.arziolib.api.event.test;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import com.arzio.arziolib.api.Test;
import com.arzio.arziolib.api.TestCase;

public class TestCaseRunEvent extends TestCaseEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	private List<Test> additionalTests = new ArrayList<>();

	public TestCaseRunEvent(TestCase testCase) {
		super(testCase);
	}
	
	public void addTest(Test test) {
		additionalTests.add(test);
	}
	
	public List<Test> getAdditionalTests() {
		return additionalTests;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
