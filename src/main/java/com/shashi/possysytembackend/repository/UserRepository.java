package com.shashi.possysytembackend.repository;

import com.shashi.possysytembackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);
    boolean existsByUserName(String userName);
    Optional<User> findByUserNameAndIsActiveTrue(String userName);
}
