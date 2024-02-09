package com.mikich.beststore.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mikich.beststore.models.Product;

public interface ProductsRepository extends JpaRepository<Product,Integer> {

}
