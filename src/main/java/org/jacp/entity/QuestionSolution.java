package org.jacp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saffchen created on 31.08.2023
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionSolution {

    private Long id;
    private String problem;
    private String solution;
    private String tags;
    private String difficulty;

}