package com.likelion.maydayspring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.maydayspring.domain.CrawledData;
import com.likelion.maydayspring.domain.Location;
import com.likelion.maydayspring.repository.CrawledDataRepository;
import com.likelion.maydayspring.repository.LocationRepository;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrawledDataService {
    @Autowired
    private CrawledDataRepository repository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void crawlSeoulData() throws IOException {
        String seoulUrl = "https://worcation.sba.kr/S02/1";
        String region = "seoul";
        //메인 페이지
        checkSsl();
        Connection con = Jsoup.connect(seoulUrl);
        Document doc = con.get();


        Elements elements = doc.select("div.__pdt");
        List<Location> locations = new ArrayList<>();

        for (Element element : elements) {
            Location info = Location.builder()
                .name(element.select("p.subject a").text())
                .address(element.select("p.add").text())
                .createdAt(LocalDateTime.now())
                .region(element.select("span.loc").text()) //한국어임
                .servicePeriod(element.select("div.during span").text())
                .imageUrl(element.select("div.img a i").attr("style").replace("background-image:url(", "").replace(");", ""))
                .build();

            locationRepository.save(info);
        }
        CrawledData crawledData = new CrawledData();
        crawledData.setDocument(doc);
        crawledData.setCrawledAt(LocalDateTime.now());
        crawledData.setUrl(seoulUrl);
        crawledData.setRegion(region);
        repository.save(crawledData);
    }

    public void crawlGangwonData() throws IOException {
        String gangwonUrl = "https://worcation.co.kr/gw";
        checkSsl();
        Connection con = Jsoup.connect(gangwonUrl);
        Document doc = con.get();
    }

    public void crawlBusanData() throws IOException {
        String busanUrl = "https://www.busaness.com/tourist_spot";
        checkSsl();
        Connection con = Jsoup.connect(busanUrl);
        Document doc = con.get();
    }

    public void crawlChungnamData() throws IOException {
        String chungnamUrl = "https://www.cacf.or.kr/site/program/index.php";
        checkSsl();
        Connection con = Jsoup.connect(chungnamUrl);
        Document doc = con.get();
    }

    public void crawlJejuData() throws IOException {
        String privateJejuUrl = "https://jejuworkation.or.kr/Supportproject";
        String publicJejuUrl = "https://jejuworkation.or.kr/islandworklabjeju";
        checkSsl();
        Connection con = Jsoup.connect(privateJejuUrl);
        Document doc = con.get();
    }

    public void checkSsl() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
