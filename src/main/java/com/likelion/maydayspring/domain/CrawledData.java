package com.likelion.maydayspring.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class CrawledData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataId;
    @Transient
    private Document document;
    @Transient
    private String data;

    private String url;
    private String region;
    private LocalDateTime crawledAt;

    public void setDocument(Document document) {
        this.document = document;
        this.data = document.html();
    }

    public Document getDocument() {
        return Jsoup.parse(this.data);
    }
}
