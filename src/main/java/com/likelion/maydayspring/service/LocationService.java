package com.likelion.maydayspring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.maydayspring.domain.Category;
import com.likelion.maydayspring.domain.DescriptionDetail;
import com.likelion.maydayspring.domain.Location;
import com.likelion.maydayspring.dto.request.OfficeRequest;
import com.likelion.maydayspring.dto.response.DescriptionDetailResponse;
import com.likelion.maydayspring.dto.response.FilterResponse;
import com.likelion.maydayspring.enums.OfficeType;
import com.likelion.maydayspring.exception.BaseException;
import com.likelion.maydayspring.exception.ErrorCode;
import com.likelion.maydayspring.properties.FilterProperties;
import com.likelion.maydayspring.repository.CategoryRepository;
import com.likelion.maydayspring.repository.DescriptionDetailRepository;
import com.likelion.maydayspring.repository.LocationRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class LocationService {
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final RestTemplate restTemplate;
    private final DescriptionDetailRepository descriptionDetailRepository;
    private final FilterProperties filterProperties;


    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocationDetail(Long locationId) {
        return locationRepository.findById(locationId)
            .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_LOCATION_ID));
    }

    @Transactional
    public void saveCategories() {
        List<Location> locations = locationRepository.findAll();

        Map<String, List<Location>> locationsByRegion = locations.stream()
            .collect(Collectors.groupingBy(Location::getRegion));

        for (Map.Entry<String, List<Location>> entry : locationsByRegion.entrySet()) {
            String region = entry.getKey();
            List<Location> regionLocations = entry.getValue();

            List<OfficeRequest> officeRequests = regionLocations.stream()
                .map(location -> new OfficeRequest(location.getName(), location.getDescription()))
                .collect(Collectors.toList());

            Map<String, List<OfficeRequest>> requestBody = new HashMap<>();
            requestBody.put("offices", officeRequests);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, List<OfficeRequest>>> requestEntity = new HttpEntity<>(requestBody, headers);

            try {
                ResponseEntity<FilterResponse[]> responseEntity = restTemplate.exchange(
                    filterProperties.getCategoryUrl(),
                    HttpMethod.POST,
                    requestEntity,
                    FilterResponse[].class);

                if (responseEntity.getStatusCode() == HttpStatus.OK) {
                    FilterResponse[] filterResponses = responseEntity.getBody();
                    if (filterResponses != null) {
                        for (Location location : regionLocations) {
                            updateCategory(location, filterResponses);
                            locationRepository.save(location);
                        }
                    } else {
                    }
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void updateCategory(Location location, FilterResponse[] filterResponses) {
        Category category = location.getCategory();
        if (category == null) {
            category = new Category();
            location.setCategory(category);
        }

        for (FilterResponse filterResponse : filterResponses) {
            switch (filterResponse.filter()) {
                case "Monitor":
                    category.setMonitor(getBooleanStatus(filterResponse, location.getName()));
                    break;
                case "ConferenceRoom":
                    category.setConferenceRoom(getBooleanStatus(filterResponse, location.getName()));
                    break;
                case "OfficeType":
                    category.setOfficeType(getOfficeTypeStatus(filterResponse, location.getName()));
                    break;
                case "Parking":
                    category.setParking(getBooleanStatus(filterResponse, location.getName()));
                    break;
                case "PhoneBooth":
                    category.setPhoneBooth(getBooleanStatus(filterResponse, location.getName()));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown filter: " + filterResponse.filter());
            }
        }

        if (category.getOfficeType() == null) {
            category.setOfficeType(OfficeType.UNCLASSIFIED);
        }
        location.setCategory(category);
        categoryRepository.save(category);
    }

    private boolean getBooleanStatus(FilterResponse filterResponse, String locationName) {
        return filterResponse.data().stream()
            .filter(data -> data.names().contains(locationName))
            .findFirst()
            .map(data -> "Y".equals(data.status()))
            .orElse(false);
    }

    private OfficeType getOfficeTypeStatus(FilterResponse filterResponse, String locationName) {
        return filterResponse.data().stream()
            .filter(data -> data.names().contains(locationName))
            .findFirst()
            .map(data -> OfficeType.fromString(data.status()))
            .orElse(OfficeType.UNCLASSIFIED);
    }
    @Transactional
    public void saveDescriptionDetails() {
        List<Location> locations = locationRepository.findAll();

        Map<String, List<Location>> locationsByRegion = locations.stream()
            .collect(Collectors.groupingBy(Location::getRegion));

        for (Map.Entry<String, List<Location>> entry : locationsByRegion.entrySet()) {
            String region = entry.getKey();
            List<Location> regionLocations = entry.getValue();

            List<OfficeRequest> officeRequests = regionLocations.stream()
                .map(location -> new OfficeRequest(location.getName(), location.getDescription()))
                .collect(Collectors.toList());

            Map<String, List<OfficeRequest>> requestBody = new HashMap<>();
            requestBody.put("offices", officeRequests);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, List<OfficeRequest>>> requestEntity = new HttpEntity<>(
                requestBody, headers);

            try {
                ResponseEntity<String> responseEntity = restTemplate.exchange(
                    filterProperties.getDescriptionDetailUrl(),
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

                if (responseEntity.getStatusCode() == HttpStatus.OK) {
                    String responseBody = responseEntity.getBody();
                    ObjectMapper objectMapper = new ObjectMapper();
                    DescriptionDetailResponse[] detailResponses = objectMapper.readValue(responseBody, DescriptionDetailResponse[].class);

                    for (DescriptionDetailResponse detailResponse : detailResponses) {
                        updateDescriptionDetail(detailResponse);
                    }
                    System.out.println("Categories updated successfully for region: " + region);
                } else {
                    System.err.println("Failed to get response from FastAPI server for region " + region + ": " + responseEntity.getStatusCode());
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error occurred while processing region: " + region);
            }
        }
    }

    @Transactional
    public void saveDescriptionDetailsEach(Long locationId) {
        Location location = locationRepository.findById(locationId)
            .orElseThrow(() -> new IllegalArgumentException("Location not found for id: " + locationId));

        OfficeRequest officeRequest = new OfficeRequest(location.getName(), location.getDescription());

        Map<String, List<OfficeRequest>> requestBody = new HashMap<>();
        requestBody.put("offices", List.of(officeRequest));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, List<OfficeRequest>>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                filterProperties.getDescriptionDetailUrl(),
                HttpMethod.POST,
                requestEntity,
                String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String responseBody = responseEntity.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                DescriptionDetailResponse[] detailResponses = objectMapper.readValue(responseBody, DescriptionDetailResponse[].class);

                for (DescriptionDetailResponse detailResponse : detailResponses) {
                    updateDescriptionDetailEach(location, detailResponse);
                }
                System.out.println("Description details updated successfully for location: " + location.getName());
            } else {
                System.err.println("Failed to get response from FastAPI server for location " + location.getName() + ": " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error occurred while processing location: " + location.getName());
        }
    }

    private void updateDescriptionDetailEach(Location location, DescriptionDetailResponse detailResponse) {
        if (location != null) {
            DescriptionDetail descriptionDetail = location.getDescriptionDetail();
            if (descriptionDetail == null) {
                descriptionDetail = new DescriptionDetail();
                location.setDescriptionDetail(descriptionDetail);
            }

            descriptionDetail.setPhoneNumber(detailResponse.getPhoneNumber() != null ? detailResponse.getPhoneNumber() : "확인요망");
            descriptionDetail.setOperatingTime(detailResponse.getOperatingTime() != null ? detailResponse.getOperatingTime() : "확인요망");
            descriptionDetail.setLocationIntroduction(detailResponse.getLocationIntroduction() != null ? detailResponse.getLocationIntroduction() : "확인요망");
            descriptionDetail.setProvidedDetails(String.join(", ", detailResponse.getProvidedDetails()));

            descriptionDetailRepository.save(descriptionDetail);
            locationRepository.save(location);
        } else {
            System.err.println("Location not found for name: " + detailResponse.getName());
        }
    }

    private void updateDescriptionDetail(DescriptionDetailResponse detailResponse) {
        Location location = locationRepository.findByName(detailResponse.getName()).get();
        if (location != null) {
            DescriptionDetail descriptionDetail = location.getDescriptionDetail();
            if (descriptionDetail == null) {
                descriptionDetail = new DescriptionDetail();
                location.setDescriptionDetail(descriptionDetail);
            }

            descriptionDetail.setPhoneNumber(detailResponse.getPhoneNumber() != null ? detailResponse.getPhoneNumber() : "확인요망");
            descriptionDetail.setOperatingTime(detailResponse.getOperatingTime() != null ? detailResponse.getOperatingTime() : "확인요망");
            descriptionDetail.setLocationIntroduction(detailResponse.getLocationIntroduction() != null ? detailResponse.getLocationIntroduction() : "확인요망");
            descriptionDetail.setProvidedDetails(String.join(", ", detailResponse.getProvidedDetails()));

            descriptionDetailRepository.save(descriptionDetail);
            locationRepository.save(location);
        } else {
            System.err.println("Location not found for name: " + detailResponse.getName());
        }
    }

    private String getStringStatus(FilterResponse filterResponse, String locationName) {
        return filterResponse.data().stream()
            .filter(data -> data.names().contains(locationName))
            .findFirst()
            .map(FilterResponse.Data::status)
            .orElse("확인요망");
    }
}
