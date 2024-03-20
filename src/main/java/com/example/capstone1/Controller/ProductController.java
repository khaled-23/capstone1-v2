package com.example.capstone1.Controller;

import com.example.capstone1.ApiResponse.ApiResponse;
import com.example.capstone1.Model.Product;
import com.example.capstone1.Service.CategoryService;
import com.example.capstone1.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;


    @PostMapping("/add")
    public ResponseEntity addProduct(@RequestBody @Valid Product product, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }
        String condition = productService.addProduct(product);
        return switch (condition){
            case "1" -> ResponseEntity.status(200).body(new ApiResponse("product added: "+product));
            case "2" -> ResponseEntity.status(400).body(new ApiResponse("category does not exists"));
            default -> throw new IllegalStateException("Unexpected value: " + condition);
        };
    }

    @GetMapping("/products")
    public ResponseEntity getProducts(){
        ArrayList<Product> products = productService.getProducts();
        if(products.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("there are no products"));
        }else return ResponseEntity.status(200).body(products);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateProduct(@PathVariable String id, @RequestBody @Valid Product product,Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }
        String condition = productService.updateProduct(id, product);
        return switch (condition){
            case "1" -> ResponseEntity.status(200).body(new ApiResponse("product: "+id+" updated"));
            case "2" -> ResponseEntity.status(400).body(new ApiResponse("category does not exists"));
            case "3" -> ResponseEntity.status(400).body(new ApiResponse("product with id:"+id+" not found"));
            default -> throw new IllegalStateException("Unexpected value: " + condition);
        };
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity removeProduct(@PathVariable String id){
        String condition = productService.deleteProduct(id);
        return switch (condition){
            case "0" -> ResponseEntity.status(400).body("product: "+id+" not found");
            case "1" -> ResponseEntity.status(200).body("product: "+id+" deleted");
            default -> throw new IllegalStateException("Unexpected value: " + condition);
        };
    }


}
