package com.storemanagementapi.StoreManagementAPI.controller;


import com.storemanagementapi.StoreManagementAPI.datamodel.Product;
import com.storemanagementapi.StoreManagementAPI.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam(required = false) String name){
        try {
            List<Product> products = new ArrayList<>();
            if (name == null){
                productRepository.findAll().forEach(products :: add);
            }
            if (products.isEmpty()){
                return new ResponseEntity<>(products, HttpStatus.OK);
            }
            return new ResponseEntity<>(products,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") long id){
        Optional<Product> _product = productRepository.findById(id);
        try {
            if (_product.isPresent()){
                return new ResponseEntity<>(_product.get(),HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products/name")
    public ResponseEntity<Product> getByName(@RequestParam String name){
        try {
            Optional<Product> _product = productRepository.findByName(name);
            if (_product.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(_product.get(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody Product product){
        try {
            Product _product = productRepository
                    .save(new Product(product.getName(), product.getDescription(), product.getPrice(), true,product.getCount()));
            return new ResponseEntity<>(_product, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id")long id, @RequestBody Product product){
       try {
           Optional<Product> productData = productRepository.findById(id);
           if (productData.isPresent()){
               Product _product = productData.get();
               _product.setName(product.getName());
               _product.setDescription(product.getDescription());
               _product.setPrice(product.getPrice());
               _product.setInStock(true);
               _product.setCount(product.getCount());
               return new ResponseEntity<>(productRepository.save(_product), HttpStatus.ACCEPTED);
           }else {
               return new ResponseEntity<>(HttpStatus.NOT_FOUND);
           }
       }catch (Exception e){
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") long id){
        try {
            productRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/products")
    public ResponseEntity<HttpStatus> deleteAllProducts(){
        try {
            productRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
