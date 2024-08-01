package com.likelion.maydayspring.service;

import com.likelion.maydayspring.domain.Location;
import com.likelion.maydayspring.exception.BaseException;
import com.likelion.maydayspring.exception.ErrorCode;
import com.likelion.maydayspring.repository.LocationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationService {
    private final LocationRepository locationRepository;
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocationDetail(Long locationId) {
        return locationRepository.findById(locationId)
            .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_LOCATION_ID));
    }
}
