package com.arzio.arziolib.api.event.test;

import org.bukkit.event.Event;

import com.arzio.arziolib.api.TestCase;

public abstract class TestCaseEvent extends Event {

    private final TestCase testCase;
    
    public TestCaseEvent(TestCase testCase) {
        this.testCase = testCase;
    }
    
    public TestCase getTestCase() {
        return this.testCase;
    }
}
