package com.likelion.maydayspring.dto.response;

import com.likelion.maydayspring.domain.Location;
import lombok.Builder;

@Builder
public record LocationDetailResponse(
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
    Boolean phoneBooth,
    String phoneNumber,
    String operatingTime,
    String locationIntroduction,
    String providedDetails
) {
    public static LocationDetailResponse of(Location location) {
        return LocationDetailResponse.builder()
            .locationId(location.getLocationId())
            .name(location.getName())
            .address(location.getAddress())
            .region(location.getRegion())
            .imageUrl(location.getImageUrl())
            .servicePeriod(location.getServicePeriod())
            .monitor(location.getCategory().isMonitor())
            .conferenceRoom(location.getCategory().isConferenceRoom())
            .officeType(location.getCategory().getOfficeType().getValue())
            .parking(location.getCategory().isParking())
            .phoneBooth(location.getCategory().isPhoneBooth())
            .phoneNumber(location.getDescriptionDetail().getPhoneNumber())
            .operatingTime(location.getDescriptionDetail().getOperatingTime())
            .locationIntroduction(location.getDescriptionDetail().getLocationIntroduction())
            .providedDetails(location.getDescriptionDetail().getProvidedDetails())
            .build();
    }
}

