package org.jacp.processor;

/**
 * @author saffchen created on 04.10.2023
 */
public interface ClassCreator {
    void createClass(String imports, String className, String solution);
}