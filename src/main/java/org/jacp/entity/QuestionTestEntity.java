package org.jacp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saffchen created on 24.10.2023
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionTestEntity {
    private String imports;
    private String testImports;
    private String test;
}
