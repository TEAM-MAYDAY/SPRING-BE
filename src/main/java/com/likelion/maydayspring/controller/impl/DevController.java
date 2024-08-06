package com.likelion.maydayspring.controller.impl;

import com.likelion.maydayspring.domain.Category;
import com.likelion.maydayspring.domain.DescriptionDetail;
import com.likelion.maydayspring.domain.Location;
import com.likelion.maydayspring.dto.response.LocationDetailResponse;
import com.likelion.maydayspring.repository.CategoryRepository;
import com.likelion.maydayspring.repository.DescriptionDetailRepository;
import com.likelion.maydayspring.repository.LocationRepository;
import com.likelion.maydayspring.service.CrawledDataService;
import com.likelion.maydayspring.service.LocationService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dev")
public class DevController {
    @Autowired
    private CrawledDataService crawledService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private DescriptionDetailRepository descriptionDetailRepository;

    @PostMapping("/crawling/allData")
    public void saveCrawledData() throws IOException {
        crawledService.crawlSeoulData();
        crawledService.crawlGangwonData();
        crawledService.crawlBusanData();
        crawledService.crawlChungnamData();
        crawledService.crawlJejuData();
    }

    @PostMapping("/crawling/testSeoulData")
    public void saveSeoulCrawledData() throws IOException {
        crawledService.crawlSeoulData();
    }

    @PostMapping("/locations/saveCategory")
    public ResponseEntity<String> saveLocationCategories() {
        try {
            locationService.saveCategories();
            return ResponseEntity.ok("Categories updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update categories");
        }
    }


    @PostMapping("/locations/saveDescriptionDetails")
    public ResponseEntity<String> saveLocationDescriptionDetails() {
        try {
            locationService.saveDescriptionDetails();
            return ResponseEntity.ok("DescriptionDetails updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update descriptionDetails");
        }
    }

    @PostMapping("/locations/saveDescriptionDetailsEach/{id}")
    public ResponseEntity<String> saveLocationDescriptionDetailsEach(@PathVariable Long id) {
        try {
            locationService.saveDescriptionDetailsEach(id);
            return ResponseEntity.ok("DescriptionDetails updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update descriptionDetails");
        }
    }

    @PostMapping("/locations/customsaveCategory/{id}")
    public ResponseEntity<String> saveCustomCategory(@PathVariable Long id,
        @RequestBody Category category) {
        Location location = locationService.getLocationDetail(id);
        location.setCategory(category);
        category.setLocation(location);
        locationRepository.save(location);
        return ResponseEntity.ok("Categories updated successfully");
    }

    @PostMapping("/locations/customsaveDescriptionDetails/{id}")
    public ResponseEntity<String> saveCustomDescriptionDetails(@PathVariable Long id,
        @RequestBody DescriptionDetail descriptionDetail) {
        Location location = locationService.getLocationDetail(id);
        location.setDescriptionDetail(descriptionDetail);
        descriptionDetail.setLocation(location);
        locationRepository.save(location);
        return ResponseEntity.ok("DescriptionDetails updated successfully");
    }
}
