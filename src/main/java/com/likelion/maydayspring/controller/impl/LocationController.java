package com.likelion.maydayspring.controller.impl;

import com.likelion.maydayspring.controller.LocationApi;
import com.likelion.maydayspring.domain.Location;
import com.likelion.maydayspring.dto.response.ListResponse;
import com.likelion.maydayspring.dto.response.LocationDetailResponse;
import com.likelion.maydayspring.dto.response.LocationResponse;
import com.likelion.maydayspring.service.LocationService;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LocationController implements LocationApi {
    private final LocationService locationService;
    @Override
    public ResponseEntity<ListResponse<LocationResponse>> getAllLocations() {
        List<LocationResponse> locations =  locationService.getAllLocations().stream()
            .map(LocationResponse::of)
            .collect(Collectors.toList());
        ListResponse<LocationResponse> response = ListResponse.from(locations);
        return ResponseEntity.ok(response);

    }

    @Override
    public ResponseEntity<LocationDetailResponse> getLocationDetail(Long locationId) {
        Location location = locationService.getLocationDetail(locationId);
        LocationDetailResponse response = LocationDetailResponse.of(location);
        return ResponseEntity.ok(response);
    }


}
