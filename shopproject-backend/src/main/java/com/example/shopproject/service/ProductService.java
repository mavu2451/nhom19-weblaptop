package com.example.shopproject.service;

import com.example.shopproject.model.Products;
import com.example.shopproject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Products> listAll() {
        return repo.findAll();
    }

    public Products save(Products product) {
        return repo.save(product);
    }

    public Products getId(long id) {
        return repo.findById(id).get();
    }
    public Products updateProduct(Products products){
        Products p = repo.findById(products.getId()).orElse(null);
        p.setName(products.getName());
        p.setInfo(products.getInfo());
        p.setImage(products.getImage());
        p.setPrice(products.getPrice());
        return repo.save(p);
    }
    public String delete(long id) {
        repo.deleteById(id);
        return "Da xoa";
    }


}
