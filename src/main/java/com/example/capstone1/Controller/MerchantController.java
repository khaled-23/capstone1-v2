package com.example.capstone1.Controller;

import com.example.capstone1.ApiResponse.ApiResponse;
import com.example.capstone1.Model.Merchant;
import com.example.capstone1.Service.MerchantService;
import com.example.capstone1.Service.MerchantStockService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/merchant")
@RequiredArgsConstructor
public class MerchantController {
    private final MerchantService merchantService;


    @PostMapping("/add")
    public ResponseEntity addMerchant(@RequestBody @Valid Merchant merchant, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }
        merchantService.addMerchant(merchant);
        return ResponseEntity.status(200).body(new ApiResponse("merchant added: "+merchant));
    }

    @GetMapping("/merchants")
    public ResponseEntity getMerchants(){
        ArrayList<Merchant> merchants = merchantService.getMerchants();
        if(merchants.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("there are no merchants"));
        }else return ResponseEntity.status(200).body(merchants);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateMerchant(@PathVariable String id, @RequestBody @Valid Merchant merchant, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }
        String condition = merchantService.updateMerchant(id,merchant);
        return switch (condition){
            case "1" -> ResponseEntity.status(200).body(new ApiResponse("merchant updated: "+id+" "+merchant));
            case "2" -> ResponseEntity.status(400).body(new ApiResponse("merchant not found: "+ id));
            default -> throw new IllegalStateException("Unexpected value: " + condition);
        };
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity removeMerchant(@PathVariable String id){
        String condition = merchantService.removeMerchant(id);
        return switch (condition){
            case "0" -> ResponseEntity.status(400).body(new ApiResponse("no merchant with id: "+id));
            case "1" -> ResponseEntity.status(200).body(new ApiResponse("merchant removed: "+id));
            default -> throw new IllegalStateException("Unexpected value: " + condition);
        };
    }


}
