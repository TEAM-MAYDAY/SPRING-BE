package com.likelion.maydayspring.repository;

import com.likelion.maydayspring.domain.Location;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByName(String name);
}
