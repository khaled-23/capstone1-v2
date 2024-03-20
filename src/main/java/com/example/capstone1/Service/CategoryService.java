package com.example.capstone1.Service;

import com.example.capstone1.Model.Category;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.ArrayList;

@Service
public class CategoryService {
    ArrayList<Category> categories = new ArrayList<>();

    public String addCategory(Category category, Errors errors){
        if(errors.hasErrors()){
            return "0";
        }
        categories.add(category);
        return "1";
    }
    public ArrayList<Category> getCategories(){
        return categories;
    }
    public String updateCategory(String id, Category category, Errors errors){
        if(errors.hasErrors()){
            return "2";
        }
        for(int i=0; i<categories.size(); i++){
            if(categories.get(i).getId().equalsIgnoreCase(id)){
                categories.set(i,category);
                return "1";
            }
        }
        return "0";
    }
    public String removeCategory(String id){
        for(int i=0;i<categories.size(); i++){
            if(categories.get(i).getId().equalsIgnoreCase(id)){
                categories.remove(i);
                return "1";
            }
        }
        return "0";
    }

    public boolean doesCategoryExist(String categoryId) {
        for(Category category:categories){
            if(category.getId().equalsIgnoreCase(categoryId)){
                return true;
            }
        }
        return false;
    }
}
