package com.nakao.resolvemate.domain.util;

public interface LogService<T> {

    void info(T source, String message);

    void warn(T source, String message);

    void error(T source, String message);

}
