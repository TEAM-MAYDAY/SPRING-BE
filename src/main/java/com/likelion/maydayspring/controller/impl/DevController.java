package com.likelion.maydayspring.controller.impl;

import com.likelion.maydayspring.service.CrawledDataService;
import com.likelion.maydayspring.service.LocationService;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dev")
public class DevController {
    @Autowired
    private CrawledDataService crawledService;

    @Autowired
    private LocationService locationService;

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
}
