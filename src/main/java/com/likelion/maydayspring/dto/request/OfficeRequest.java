package com.likelion.maydayspring.dto.request;

import lombok.Builder;

@Builder
public record OfficeRequest(
    String name,
    String description
) {

}


