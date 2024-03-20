package com.example.capstone1.Service;

import com.example.capstone1.Model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ProductService {
    ArrayList<Product> products = new ArrayList<>();
    private final CategoryService categoryService;
    public String addProduct(Product product){
        boolean doesCategoryExist = categoryService.doesCategoryExist(product.getCategoryId());
        if(!doesCategoryExist){
            return "2";
        }
        products.add(product);
        return "1";
    }

    public ArrayList<Product> getProducts(){
        return products;
    }

    public String updateProduct(String id, Product product){
        boolean doesCategoryExist = categoryService.doesCategoryExist(product.getCategoryId());
        if(!doesCategoryExist){
            return "2";
        }

        for(int i=0;i<products.size();i++){
            if(products.get(i).getId().equalsIgnoreCase(id)){
                products.set(i,product);
                return "1";
            }
        }
        return "3";
    }
    public String deleteProduct(String id){
        for(int i=0; i< products.size();i++){
            if(products.get(i).getId().equalsIgnoreCase(id)){
                products.remove(i);
                return "1";
            }
        }
        return "0";
    }
    public double getProductPrice(String productId){
        for(int i=0; i<products.size();i++){
            if(products.get(i).getId().equalsIgnoreCase(productId)){
                return products.get(i).getPrice();
            }
        }
        return 0;
    }

    public Product getProduct(String productId){
        for (Product product : products) {
            if (product.getId().equalsIgnoreCase(productId)) {
                return product;
            }
        }
        return null;
    }
    public boolean doesProductExists(String id){
        for(Product product:products){
            if(product.getId().equalsIgnoreCase(id)){
                return true;
            }
        }
        return false;
    }

    public Product getProductCopy(String productId) {
        for (Product product : products) {
            if (product.getId().equalsIgnoreCase(productId)) {
                return new Product(product.getId(),product.getName(),product.getPrice(),product.getCategoryId());
            }
        }
        return null;
    }
}
