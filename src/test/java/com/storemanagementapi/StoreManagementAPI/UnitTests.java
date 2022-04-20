package com.storemanagementapi.StoreManagementAPI;

import com.storemanagementapi.StoreManagementAPI.datamodel.Product;
import com.storemanagementapi.StoreManagementAPI.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
public class UnitTests {
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    ProductRepository repository;

    @Test
    public void should_find_no_products_if_repository_is_empty(){
        Iterable products = repository.findAll();
        assertThat(products).isEmpty();
    }

    @Test
    public void should_store_a_product(){
        Product product = repository.save(new Product("Test Product", "Test Desc",2,true,1));
        assertThat(product).hasFieldOrPropertyWithValue("name", "Test Product");
        assertThat(product).hasFieldOrPropertyWithValue("description", "Test Desc");
        assertThat(product).hasFieldOrPropertyWithValue("price",2.0);
        assertThat(product).hasFieldOrPropertyWithValue("stock", true);
        assertThat(product).hasFieldOrPropertyWithValue("count",1);
    }

    @Test
    public void should_find_all_products(){
        Product prod1 = repository.save(new Product("Product#1", "Desc#1",2,true,1));
        testEntityManager.persist(prod1);
        Product prod2 = repository.save(new Product("Product#2", "Desc#1",4,true,3));
        testEntityManager.persist(prod2);
        Product prod3 = repository.save(new Product("Product#3", "Desc#1",3,true,5));
        testEntityManager.persist(prod3);
        Iterable products = repository.findAll();
        assertThat(products).hasSize(3).contains(prod1,prod2,prod3);
    }

    @Test
    public void should_find_product_by_id(){
        Product prod1 = repository.save(new Product("Product#1", "Desc#1",2,true,1));
        testEntityManager.persist(prod1);
        Product foundProduct = repository.findById(prod1.getId()).get();
        assertThat(foundProduct).isEqualTo(prod1);
    }

    @Test
    public void should_find_products_by_name() {
        Product prod1 = repository.save(new Product("Prod1", "Test Desc",2,true,1));
        testEntityManager.persist(prod1);
        Product prod2 = repository.save(new Product("Product2", "Test Desc",2,true,1));
        testEntityManager.persist(prod2);
        Product prod3 = repository.save(new Product("Product3", "Test Desc",2,true,1));
        testEntityManager.persist(prod3);
        Optional products = repository.findByName("Prod1");
        assertThat(products).contains(prod1);
    }

    @Test
    public void should_update_product_by_id() {
        Product prod1 = repository.save(new Product("Product#1", "Desc#1",2,true,1));
        testEntityManager.persist(prod1);
        Product prod2 = repository.save(new Product("Product#2", "Desc#1",4,true,3));
        testEntityManager.persist(prod2);
        Product updatedProd = new Product("updatedProd","updatedDesc",2,true,1);
        Product prod = repository.findById(prod2.getId()).get();
        prod.setName(updatedProd.getName());
        prod.setDescription(updatedProd.getDescription());
        prod.setInStock(updatedProd.isInStock());
        prod.setPrice(updatedProd.getPrice());
        prod.setCount(updatedProd.getCount());
        repository.save(prod);
        Product checkProd = repository.findById(prod2.getId()).get();

        assertThat(checkProd.getId()).isEqualTo(prod2.getId());
        assertThat(checkProd.getName()).isEqualTo(updatedProd.getName());
        assertThat(checkProd.getDescription()).isEqualTo(updatedProd.getDescription());
        assertThat(checkProd.isInStock()).isEqualTo(updatedProd.isInStock());
        assertThat(checkProd.getPrice()).isEqualTo(updatedProd.getPrice());
        assertThat(checkProd.getCount()).isEqualTo(updatedProd.getCount());
    }

    @Test
    public void should_delete_product_by_id() {
        Product prod1 = repository.save(new Product("Prod1", "Test Desc1",1,true,1));
        testEntityManager.persist(prod1);
        Product prod2 = repository.save(new Product("Product2", "Test Desc2",2,true,1));
        testEntityManager.persist(prod2);
        Product prod3 = repository.save(new Product("Product3", "Test Desc3",3,true,1));
        testEntityManager.persist(prod3);
        repository.deleteById(prod1.getId());
        Iterable products = repository.findAll();
        assertThat(products).hasSize(2).contains(prod2,prod3);
    }

    @Test
    public void should_delete_all_products() {
        testEntityManager.persist(new Product("Prod1", "Test Desc1",1,true,1));
        testEntityManager.persist(new Product("Prod2", "Test Desc2",2,true,1));
        repository.deleteAll();
        assertThat(repository.findAll().isEmpty());
    }
}
