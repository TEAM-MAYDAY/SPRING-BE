package com.likelion.maydayspring.repository;

import com.likelion.maydayspring.domain.CrawledData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrawledDataRepository extends JpaRepository<CrawledData, Long> {

}
