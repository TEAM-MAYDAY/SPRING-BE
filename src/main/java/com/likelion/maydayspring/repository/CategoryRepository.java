package com.likelion.maydayspring.repository;

import com.likelion.maydayspring.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
