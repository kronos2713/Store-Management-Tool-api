package com.storemanagementapi.StoreManagementAPI.controller;


import com.storemanagementapi.StoreManagementAPI.datamodel.Product;
import com.storemanagementapi.StoreManagementAPI.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);
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
                LOG.info("No Products in DB");
                return new ResponseEntity<>(products, HttpStatus.OK);
            }
            LOG.info("Products returned");
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
                LOG.info("Product found by id");
                return new ResponseEntity<>(_product.get(),HttpStatus.OK);
            }else {
                LOG.info("No such id in DB");
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
                LOG.info("No product with provided name exists in DB");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            LOG.info("Product found");
            return new ResponseEntity<>(_product.get(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody Product product){
        try {
            if (!productRepository.existsByName(product.getName())) {
                Product _product = productRepository
                        .save(new Product(product.getName(), product.getDescription(), product.getPrice(), true, product.getCount()));
                LOG.info("Product created");
                return new ResponseEntity<>(_product, HttpStatus.CREATED);
            }else{
                LOG.warn("Product is already in DB");
                return new ResponseEntity<>(HttpStatus.IM_USED);
            }
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
               LOG.info("Product successfully updated");
               return new ResponseEntity<>(productRepository.save(_product), HttpStatus.ACCEPTED);
           }else {
               LOG.info("Product doesn't exist in DB");
               return new ResponseEntity<>(HttpStatus.NOT_FOUND);
           }
       }catch (Exception e){
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") long id){
        try {
            if (productRepository.existsById(id)) {
                productRepository.deleteById(id);
                LOG.info("Product successfully deleted");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else {
                LOG.warn("Product id isn't in DB");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/products")
    public ResponseEntity<HttpStatus> deleteAllProducts(){
        try {
            productRepository.deleteAll();
            LOG.info("All products where deleted");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
