package com.likelion.maydayspring.service;

import static com.mysql.cj.conf.PropertyKey.logger;

import com.likelion.maydayspring.domain.Category;
import com.likelion.maydayspring.domain.DescriptionDetail;
import com.likelion.maydayspring.domain.Location;
import com.likelion.maydayspring.dto.request.OfficeRequest;
import com.likelion.maydayspring.dto.response.FilterResponse;
import com.likelion.maydayspring.enums.OfficeType;
import com.likelion.maydayspring.exception.BaseException;
import com.likelion.maydayspring.exception.ErrorCode;
import com.likelion.maydayspring.properties.FilterProperties;
import com.likelion.maydayspring.repository.CategoryRepository;
import com.likelion.maydayspring.repository.DescriptionDetailRepository;
import com.likelion.maydayspring.repository.LocationRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
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
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    private static final String CATEGORY_FILTER_URL = "http://ec2-3-36-88-229.ap-northeast-2.compute.amazonaws.com:8000/filter_office";
    private static final String DESCRIPTION_DETAIL_FILTER_URL = "http://ec2-3-36-88-229.ap-northeast-2.compute.amazonaws.com:8000/description_office";


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
                        System.out.println("Categories updated successfully for region: " + region);
                    } else {
                        System.err.println("Received null response for region: " + region);
                    }
                } else {
                    System.err.println("Failed to get response from FastAPI server for region " + region + ": " + responseEntity.getStatusCode());
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error occurred while processing region: " + region);
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
                ResponseEntity<FilterResponse[]> responseEntity = restTemplate.exchange(
                    filterProperties.getDescriptionDetailUrl(),
                    HttpMethod.POST,
                    requestEntity,
                    FilterResponse[].class);

                if (responseEntity.getStatusCode() == HttpStatus.OK) {
                    FilterResponse[] filterResponses = responseEntity.getBody();
                    if (filterResponses != null) {
                        for (Location location : regionLocations) {
                            updateDescriptionDetail(location, filterResponses);
                            locationRepository.save(location);
                        }
                        System.out.println("Categories updated successfully for region: " + region);
                    } else {
                        System.err.println("Received null response for region: " + region);
                    }
                } else {
                    System.err.println(
                        "Failed to get response from FastAPI server for region " + region + ": "
                            + responseEntity.getStatusCode());
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error occurred while processing region: " + region);
            }
        }
    }

    private void updateDescriptionDetail(Location location, FilterResponse[] filterResponses) {
        DescriptionDetail descriptionDetail = location.getDescriptionDetail();
        if (descriptionDetail == null) {
            descriptionDetail = new DescriptionDetail();
            location.setDescriptionDetail(descriptionDetail);
        }

        for (FilterResponse filterResponse : filterResponses) {
            switch (filterResponse.filter()) {
                case "PhoneNumber":
                    descriptionDetail.setPhoneNumber(getStringStatus(filterResponse, location.getName()));
                    break;
                case "OperatingTime":
                    descriptionDetail.setOperatingTime(getStringStatus(filterResponse, location.getName()));
                    break;
                case "LocationIntroduction":
                    descriptionDetail.setLocationIntroduction(getStringStatus(filterResponse, location.getName()));
                    break;
                case "ProvidedDetails":
                    descriptionDetail.setProvidedDetails(getStringStatus(filterResponse, location.getName()));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown filter: " + filterResponse.filter());
            }
        }

        if (descriptionDetail.getPhoneNumber() == null) {
            descriptionDetail.setPhoneNumber("확인요망");
        }
        if (descriptionDetail.getOperatingTime() == null) {
            descriptionDetail.setOperatingTime("확인요망");
        }
        if (descriptionDetail.getLocationIntroduction() == null) {
            descriptionDetail.setLocationIntroduction("확인요망");
        }
        if (descriptionDetail.getProvidedDetails() == null) {
            descriptionDetail.setProvidedDetails("확인요망");
        }
        descriptionDetailRepository.save(descriptionDetail);
    }

    private String getStringStatus(FilterResponse filterResponse, String locationName) {
        return filterResponse.data().stream()
            .filter(data -> data.names().contains(locationName))
            .findFirst()
            .map(FilterResponse.Data::status)
            .orElse("확인요망");
    }
}
