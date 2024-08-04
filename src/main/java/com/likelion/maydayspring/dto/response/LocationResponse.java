package com.likelion.maydayspring.dto.response;

import com.likelion.maydayspring.domain.Location;
import lombok.Builder;

@Builder
public record LocationResponse(
    Long locationId,
    String name,
    String address,
    String region,
    String imageUrl,
    String servicePeriod,
    Boolean monitor,
    Boolean conferenceRoom,
    String officeType,
    Boolean parking,
    Boolean phoneBooth
) {
    public static LocationResponse of(Location location) {
        return LocationResponse.builder()
            .locationId(location.getLocationId())
            .name(location.getName())
            .region(location.getRegion())
            .imageUrl(location.getImageUrl())
            .servicePeriod(location.getServicePeriod())
            .monitor(location.getCategory().isMonitor())
            .conferenceRoom(location.getCategory().isConferenceRoom())
            .officeType(location.getCategory().getOfficeType().getValue())
            .parking(location.getCategory().isParking())
            .phoneBooth(location.getCategory().isPhoneBooth())
            .build();
    }
}
