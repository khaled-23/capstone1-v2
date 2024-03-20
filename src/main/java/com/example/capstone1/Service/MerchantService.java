package com.example.capstone1.Service;

import com.example.capstone1.Model.Merchant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MerchantService {
    ArrayList<Merchant> merchants = new ArrayList<>();
    private final MerchantStockService merchantStockService;



    public String addMerchant(Merchant merchant, Errors errors){
        if(errors.hasErrors()){
            return "0";
        }
        merchants.add(merchant);
        return "1";
    }
    public ArrayList<Merchant> getMerchants(){
        return merchants;
    }

    public String updateMerchant(String id, Merchant merchant, Errors errors){
        if(errors.hasErrors()){
            return "0";
        }
        for(int i=0; i<merchants.size(); i++){
            if(merchants.get(i).getId().equalsIgnoreCase(id)){
                merchants.set(i,merchant);
                return "1";
            }
        }
        return "2";
    }
    public String removeMerchant(String id){
        for(int i=0; i<merchants.size(); i++){
            if(merchants.get(i).getId().equalsIgnoreCase(id)){
                merchants.remove(i);
                merchantStockService.remove(id);//remove merchantStock when removing merchant
                return "1";
            }
        }
        return "0";
    }




    public boolean doesMerchantExists(String id) {
        for(Merchant merchant:merchants){
            if(merchant.getId().equalsIgnoreCase(id)){
                return true;
            }
        }
        return false;
    }
}
