package com.nakao.resolvemate.infrastructure.audit;

import com.nakao.resolvemate.domain.util.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl<T> implements LogService<T> {

    @Override
    public void info(T source, String message) {
        Logger sourceLogger = LoggerFactory.getLogger(source.getClass());
        sourceLogger.info(message);
    }

    @Override
    public void warn(T source, String message) {
        Logger sourceLogger = LoggerFactory.getLogger(source.getClass());
        sourceLogger.warn(message);
    }

    @Override
    public void error(T source, String message) {
        Logger sourceLogger = LoggerFactory.getLogger(source.getClass());
        sourceLogger.error(message);
    }

}
