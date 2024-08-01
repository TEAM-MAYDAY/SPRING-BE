package com.likelion.maydayspring.repository;

import com.likelion.maydayspring.domain.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByname(String name);

    Optional<Users> findByid(String id);
}
