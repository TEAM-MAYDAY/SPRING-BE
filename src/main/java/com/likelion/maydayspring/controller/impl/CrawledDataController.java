package com.likelion.maydayspring.controller.impl;

import com.likelion.maydayspring.service.CrawledDataService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crawl")
public class CrawledDataController {
    @Autowired
    private CrawledDataService crawledService;

    @PostMapping("/allData")
    public void saveCrawledData() throws IOException {
        crawledService.crawlSeoulData();
        crawledService.crawlGangwonData();
        crawledService.crawlBusanData();
        crawledService.crawlChungnamData();
        crawledService.crawlJejuData();
    }

    @PostMapping("/testSeoulData")
    public void saveSeoulCrawledData() throws IOException {
        crawledService.crawlSeoulData();
    }
}
