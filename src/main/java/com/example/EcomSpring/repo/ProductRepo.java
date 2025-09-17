package com.example.EcomSpring.repo;

import com.example.EcomSpring.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

    List<Product> findByNameContainingOrDescriptionContainingOrBrandContainingOrCategoryContaining(
            String nameKeyword,
            String descKeyword,
            String brandKeyword,
            String categoryKeyword
    );
}
