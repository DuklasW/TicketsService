package com.example.TicketsService.Mapper.common;

import java.util.List;

public interface Mapper<E, R> {
    R toResponse(E Entity);
    List<R> toResponses(List<E> entities);
}
