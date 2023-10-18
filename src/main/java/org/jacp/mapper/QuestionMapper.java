package org.jacp.mapper;

import org.jacp.dto.QuestionDto;
import org.jacp.entity.QuestionEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

/**
 * @author saffchen created on 16.10.2023
 */

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface QuestionMapper {
    QuestionDto questionDto(QuestionEntity question);
}
