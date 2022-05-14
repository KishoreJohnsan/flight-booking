package com.flightapp.authservice.repo;

import com.flightapp.authservice.entity.UserDet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserDet, String> {
    Optional<UserDet> findByUsername(String username);

}
