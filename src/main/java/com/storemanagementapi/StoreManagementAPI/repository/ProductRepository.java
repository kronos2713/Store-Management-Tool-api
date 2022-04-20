package com.storemanagementapi.StoreManagementAPI.repository;


import com.storemanagementapi.StoreManagementAPI.datamodel.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {

}
