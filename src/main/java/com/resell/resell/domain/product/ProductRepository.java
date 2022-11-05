package com.resell.resell.domain.product;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, SearchProductRepository {

    boolean existsByModelNumber(String modelNumber);

    @Override
    @EntityGraph(attributePaths = {"trades", "brand"})
    Optional<Product> findById(Long id);

}
