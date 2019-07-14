package com.arzio.arziolib.api.util;

import org.bukkit.command.CommandSender;

import com.arzio.arziolib.api.Test;
import com.arzio.arziolib.api.TestCase;

public class TestException extends Exception {

    private static final long serialVersionUID = -1526572621667345785L;
    private final TestCase testCase;
    private final Test failedTest;
    private final TestExceptionType type;
    
    public static enum TestExceptionType {
        FAILED,
        IGNORED;
    }
    
    public TestException(TestExceptionType type, TestCase testCase, Test failedTest, String str) {
        super(str);
        this.testCase = testCase;
        this.type = type;
        this.failedTest = failedTest;
    }
    
    public TestException(TestExceptionType type, TestCase testCase, Test failedTest) {
        super();
        this.testCase = testCase;
        this.type = type;
        this.failedTest = failedTest;
    }
    
    public TestException(TestExceptionType type, TestCase testCase, Test failedTest, Throwable e) {
        super(e);
        this.testCase = testCase;
        this.type = type;
        this.failedTest = failedTest;
    }
    
    public Test getFailedTest() {
        return failedTest;
    }

    public TestExceptionType getType() {
        return type;
    }

    public TestCase getTestCase() {
        return testCase;
    }

    public void printStackTrace(CommandSender sender) {
        if (this.getType() == TestExceptionType.IGNORED) {
            sender.sendMessage("§6Test "+this.getTestCase()+" got ignored by a plugin.");
            return;
        }
        
        sender.sendMessage("§4Test Case '"+this.getTestCase().getCommandName()+"' failed at Test "+this.getFailedTest().getId()+".");
        if (!this.testCase.getTests().contains(this.getFailedTest())) {
            sender.sendMessage("§4§lNOTE: §cThe Test "+this.getFailedTest().getId()+" was dynamically added from another plugin.");
        }
        sender.sendMessage("§4§oGet more details in the Server Log.");
        sender.sendMessage(" ");
        sender.sendMessage("§eSummary: §f"+this.toString());
        sender.sendMessage("§eStacktrace:");
        
        ThrowableHelper.printStackTrace(this, sender);
    }

}
