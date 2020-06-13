package com.parkit.parkingsystem.util.test;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MockedAppender extends AbstractAppender {

    public List<LogEvent> logs = new ArrayList<>();

    public MockedAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties) {
        super(name, filter, layout, ignoreExceptions, properties);
    }

    public MockedAppender() {
        super("MockedAppend", null,null);
    }

    @Override
    public void append(LogEvent logEvent) {
        logs.add(logEvent);
    }
}
