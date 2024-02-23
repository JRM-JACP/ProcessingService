package org.jacp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saffchen created on 16.10.2023
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionSolutionDto {
    private Long id;
    private Long competitionId;
    private String solution;
}
