package com.example.capstone1.Service;

import com.example.capstone1.ApiResponse.ApiResponse;
import com.example.capstone1.Model.MerchantStock;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MerchantStockService {
    ArrayList<MerchantStock> merchantStocks = new ArrayList<>();
    private final MerchantService merchantService;
    private final ProductService productService;

    public String addMerchantStock(MerchantStock merchantStock, Errors errors){
        if(errors.hasErrors()){
            return errors.getFieldError().getDefaultMessage();
        }
        boolean doesMerchantExists = merchantService.doesMerchantExists(merchantStock.getMerchantId());
        if(!doesMerchantExists){
            return "0";
        }
        boolean doesProductExists = productService.doesProductExists(merchantStock.getProductId());
        if(!doesProductExists){
            return "1";
        }
        merchantStocks.add(merchantStock);
        return "2";
    }

    public ArrayList<MerchantStock> getMerchantStocks(){
        return merchantStocks;
    }

    public String updateMerchantStock(String id, MerchantStock merchantStock, Errors errors){
        if(errors.hasErrors()){
            return "0";
        }
        for(int i=0; i<merchantStocks.size(); i++){
            if(merchantStocks.get(i).getId().equalsIgnoreCase(id)){
                merchantStocks.set(i,merchantStock);
                return "1";
            }
        }
        return "2";
    }

    public String removeMerchantStock(String id){
        for(int i=0; i<merchantStocks.size(); i++){
            if(merchantStocks.get(i).getId().equalsIgnoreCase(id)){
                merchantStocks.remove(i);
                return "1";
            }
        }
        return "0";
    }

    public boolean doesMerchantAndProductExists(String merchantId, String productId){
        for(int i=0; i<merchantStocks.size(); i++){
            if(merchantStocks.get(i).getMerchantId().equalsIgnoreCase(merchantId)
                    &&merchantStocks.get(i).getProductId().equalsIgnoreCase(productId)){
                return true;
            }
        }
        return false;
    }

    public String reStock(String merchantId, String productId, int stock){
        boolean doesMerchantAndProductExists = doesMerchantAndProductExists(merchantId,productId);
        if(!doesMerchantAndProductExists){
            return "0";
        }
        boolean isPositive = 0<stock;
        if(!isPositive){
            return "1";
        }
        for(int i=0; i<merchantStocks.size(); i++){
            if(merchantStocks.get(i).getMerchantId().equalsIgnoreCase(merchantId)
                    &&merchantStocks.get(i).getProductId().equalsIgnoreCase(productId)){
                merchantStocks.get(i).setStock(merchantStocks.get(i).getStock()+stock);
                return "2";
            }
        }
        return "null";
    }
    public boolean hasStock(String merchantId, String productId){
        for (MerchantStock merchantStock : merchantStocks) {
            if (merchantStock.getMerchantId().equalsIgnoreCase(merchantId)
                    && merchantStock.getProductId().equalsIgnoreCase(productId)
                    && merchantStock.getStock() > 0) {
                return true;
            }
        }
        return false;
    }

    public void userOrdered(String merchantId, String productId) {
        for(int i=0; i<merchantStocks.size(); i++){
            if(merchantStocks.get(i).getMerchantId().equalsIgnoreCase(merchantId)
                    &&merchantStocks.get(i).getProductId().equalsIgnoreCase(productId)){
                merchantStocks.get(i).setStock(merchantStocks.get(i).getStock()-1);
            }
        }
    }

    public void remove(String merchantId) {
        for(int i=0; i<merchantStocks.size();i++){
            if(merchantStocks.get(i).getMerchantId().equalsIgnoreCase(merchantId)){
                merchantStocks.remove(i);
                break;
            }
        }
    }
}