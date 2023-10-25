package org.jacp.mapper;

import org.jacp.dto.QuestionSolutionDto;
import org.jacp.dto.QuestionTestDto;
import org.jacp.entity.QuestionEntity;
import org.jacp.entity.QuestionTestEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

/**
 * @author saffchen created on 16.10.2023
 */

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface QuestionMapper {
    QuestionSolutionDto questionDto(QuestionEntity question);
    QuestionTestDto testDto(QuestionTestEntity questionTest);
}
