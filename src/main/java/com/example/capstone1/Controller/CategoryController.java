package com.example.capstone1.Controller;

import com.example.capstone1.ApiResponse.ApiResponse;
import com.example.capstone1.Model.Category;
import com.example.capstone1.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity addCategory(@RequestBody @Valid Category category, Errors errors){
        String condition = categoryService.addCategory(category, errors);
        return switch (condition){
            case "0" -> ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
            case "1" -> ResponseEntity.status(200).body(new ApiResponse("category added"));
            default -> throw new IllegalStateException("Unexpected value: " + condition);
        };
    }

    @GetMapping("/categories")
    public ResponseEntity getCategories(){
        ArrayList<Category> categories = categoryService.getCategories();
        if(categories.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("category is empty"));
        }else return ResponseEntity.status(200).body(categories);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity updateCategory(@PathVariable String id, @RequestBody @Valid Category category, Errors errors){
        String condition = categoryService.updateCategory(id, category, errors);
        return switch (condition){
            case "0" -> ResponseEntity.status(400).body(new ApiResponse("category not found: "+id));
            case "1" -> ResponseEntity.status(200).body(new ApiResponse("category updated: "+id));
            case "2" -> ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
            default -> throw new IllegalStateException("Unexpected value: " + condition);
        };
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCategory(@PathVariable String id){
        String condition = categoryService.removeCategory(id);
        return switch (condition){
            case "0" -> ResponseEntity.status(400).body(new ApiResponse("category not found: "+ id));
            case "1" ->ResponseEntity.status(200).body(new ApiResponse("category removed: "+id));
            default -> throw new IllegalStateException("Unexpected value: " + condition);
        };
    }
}
