package com.example.TicketsService.Mapper.common;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstracMapper<E, R> implements Mapper<E, R> {

    @Override
    public List<R> toResponses(List<E> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
