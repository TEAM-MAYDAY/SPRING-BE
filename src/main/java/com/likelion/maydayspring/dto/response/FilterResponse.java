package com.likelion.maydayspring.dto.response;

import lombok.Builder;
import java.util.List;

@Builder
public record FilterResponse(
    String filter,
    List<Data> data
) {
    @Builder
    public record Data(
        String status,
        List<String> names
    ) {

    }
}
