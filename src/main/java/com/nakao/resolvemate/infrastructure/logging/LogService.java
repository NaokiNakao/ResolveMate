package com.nakao.resolvemate.infrastructure.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class LogService<T> {

    @Async
    public void info(T source, String message) {
        Logger sourceLogger = LoggerFactory.getLogger(source.getClass());
        sourceLogger.info(message);
    }

    @Async
    public void warn(T source, String message) {
        Logger sourceLogger = LoggerFactory.getLogger(source.getClass());
        sourceLogger.warn(message);
    }

    @Async
    public void error(T source, String message) {
        Logger sourceLogger = LoggerFactory.getLogger(source.getClass());
        sourceLogger.error(message);
    }

}
