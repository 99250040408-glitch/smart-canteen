package com.kalasalingam.canteen.repository;

import com.kalasalingam.canteen.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByTokenNo(String tokenNo);

    @Query("SELECT MAX(t.id) FROM Token t")
    Long findMaxId();
}
