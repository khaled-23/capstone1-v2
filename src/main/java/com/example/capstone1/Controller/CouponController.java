package com.example.capstone1.Controller;

import com.example.capstone1.ApiResponse.ApiResponse;
import com.example.capstone1.Model.Coupon;
import com.example.capstone1.Service.CouponService;
import com.example.capstone1.Service.MerchantStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/coupon")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @PostMapping("/add/{merchantId}/{productId}")
    public ResponseEntity addCoupon(@PathVariable String merchantId, @PathVariable String productId
            ,@RequestBody @Valid Coupon coupon, Errors errors){
        String condition = couponService.addCoupon(coupon, merchantId ,productId, errors);
        return switch (condition) {
            case "0" -> ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
            case "1" -> ResponseEntity.status(400).body(new ApiResponse("merchantStock doesn't exists"));
            default -> ResponseEntity.status(200).body(new ApiResponse("coupon added"));
        };

    }
    @GetMapping("/coupons")
    public ResponseEntity getCoupons(){
        ArrayList<Coupon> coupons = couponService.getCoupons();
        if(coupons.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("there are no coupons"));
        }else return ResponseEntity.status(200).body(coupons);
    }
}
