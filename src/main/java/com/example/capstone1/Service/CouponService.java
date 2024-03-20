package com.example.capstone1.Service;

import com.example.capstone1.ApiResponse.ApiResponse;
import com.example.capstone1.Model.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CouponService {
    ArrayList<Coupon> coupons = new ArrayList<>();
    private final MerchantStockService merchantStockService;


    public ArrayList<Coupon> getCoupons() {
        return coupons;
    }

    public String addCoupon(Coupon coupon, String merchantId, String productId) {
        boolean doesMerchantAndProductExists = merchantStockService.doesMerchantAndProductExists(merchantId, productId);
        if(!doesMerchantAndProductExists){
            return "1";//merchant doesn't exists
        }
        coupons.add(coupon);
        return "2";
    }

    //checks if coupon is for the merchant
    public boolean isCouponValid(String merchantId, String productId, String couponKey) {
        for(Coupon coupon:coupons){
            if(coupon.getMerchantId().equalsIgnoreCase(merchantId)
            &&coupon.getProductId().equalsIgnoreCase(productId)
            &&coupon.getCouponKey().equalsIgnoreCase(couponKey)
            &&coupon.getUses()>0){
                return true;
            }
        }
        return false;
    }

    public int getCouponPercent(String couponKey) {
        for(Coupon coupon:coupons){
            if(coupon.getCouponKey().equalsIgnoreCase(couponKey)){
                return coupon.getPercent();
            }
        }
        return 0;
    }

    public double calculatePrice(double productPrice, double couponPercent) {
        return productPrice - productPrice * (couponPercent / 100);
    }

    public void reduceUses(String couponKey) {
        for(Coupon coupon:coupons){
            if(coupon.getCouponKey().equalsIgnoreCase(couponKey)){
                coupon.setUses(coupon.getUses()-1);
                break;
            }
        }
    }
}
