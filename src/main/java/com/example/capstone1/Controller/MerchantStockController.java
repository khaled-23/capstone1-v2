package com.example.capstone1.Controller;

import com.example.capstone1.ApiResponse.ApiResponse;
import com.example.capstone1.Model.MerchantStock;
import com.example.capstone1.Service.MerchantStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/merchant-stock")
@RequiredArgsConstructor
public class MerchantStockController {
    private final MerchantStockService merchantStockService;


    @PostMapping("/add")
    public ResponseEntity addMerchantStock(@RequestBody @Valid MerchantStock merchantStock, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(new ApiResponse("merchant does not exists"));
        }
       String condition = merchantStockService.addMerchantStock(merchantStock);
       return switch (condition){
           case "1" -> ResponseEntity.status(400).body(new ApiResponse("product does not exists"));
           case "2" -> ResponseEntity.status(200).body(new ApiResponse("Merchant stock created"));
           default -> ResponseEntity.status(400).body(new ApiResponse(condition));
       };
    }


    @GetMapping("/merchant-stocks")
    public ResponseEntity getMerchantStocks(){
        ArrayList<MerchantStock> merchantStocks = merchantStockService.getMerchantStocks();
        if(merchantStocks.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("there are no merchant Stock"));
        }else return ResponseEntity.status(200).body(merchantStocks);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateMerchantStock(@PathVariable String id, @RequestBody @Valid MerchantStock merchantStock, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
        }
        String condition = merchantStockService.updateMerchantStock(id,merchantStock);
        return switch (condition){
            case "1" -> ResponseEntity.status(200).body(new ApiResponse("merchant stock updated"));
            case "2" -> ResponseEntity.status(400).body(new ApiResponse("merchant stock not found"));
            default -> throw new IllegalStateException("Unexpected value: " + condition);
        };
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity removeMerchantStock(@PathVariable String id){
        String condition = merchantStockService.removeMerchantStock(id);
        return switch (condition){
            case "0" -> ResponseEntity.status(400).body("merchant not found: "+ id);
            case "1" -> ResponseEntity.status(200).body("merchant stock id: "+id+" is removed");
            default -> throw new IllegalStateException("Unexpected value: " + condition);
        };
    }

    @PutMapping("add-stock/{merchantId}/{productId}/{stock}")
    public ResponseEntity reStock(@PathVariable String merchantId, @PathVariable String productId, @PathVariable int stock){
        String condition = merchantStockService.reStock(merchantId,productId,stock);
        return switch (condition){
            case "0" -> ResponseEntity.status(400).body(new ApiResponse("merchant stock not found"));
            case "1" -> ResponseEntity.status(400).body(new ApiResponse("stock should be positive"));
            case "2" -> ResponseEntity.status(200).body(new ApiResponse("stock added"));
            default -> throw new IllegalStateException("Unexpected value: " + condition);
        };
    }

}
