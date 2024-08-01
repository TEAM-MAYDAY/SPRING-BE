package com.likelion.maydayspring.dto.response;

import com.likelion.maydayspring.domain.Location;
import lombok.Builder;

@Builder
public record LocationResponse(
    Long locationId,
    String name,
    String address,
    String region,
    String imageUrl
) {
    public static LocationResponse of(Location location) {
        return LocationResponse.builder()
            .locationId(location.getLocationId())
            .name(location.getName())
            .region(location.getRegion())
            .imageUrl(location.getImageUrl())
            .build();
    }
}
