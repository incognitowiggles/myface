package com.colinvipurs.application.usecases;

/**
 * Interface that must be implemented by any command that can be interpreted and executed
 */
public interface UseCase<T> {
    T execute();
}
