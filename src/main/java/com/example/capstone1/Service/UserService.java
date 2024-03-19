package com.example.capstone1.Service;


import com.example.capstone1.ApiResponse.ApiResponse;
import com.example.capstone1.Model.Product;
import com.example.capstone1.Model.User;
import com.example.capstone1.Model.UserOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserService {
    private final MerchantStockService merchantStockService;
    private final ProductService productService;
    private final UserOrderService userOrderService;
    private final CouponService couponService;
    ArrayList<User> users = new ArrayList<>();


    public void addUser(User user){
        users.add(user);
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public boolean isUpdated(String id, User user){
        for(int i=0; i<users.size(); i++){
            if(users.get(i).getId().equalsIgnoreCase(id)){
                users.set(i,user);
                return true;
            }
        }
        return false;
    }
    public boolean isRemoved(String id){
        for(int i=0; i<users.size(); i++){
            if(users.get(i).getId().equalsIgnoreCase(id)){
                users.remove(i);
                return true;
            }
        }
        return false;
    }

    public String buyProduct(String userId, String merchantId, String productId){
        boolean isExists = isExists(userId);
        if(!isExists){
            return "user does not exists";
        }
        boolean doesMerchantAndProductExists = merchantStockService.doesMerchantAndProductExists(merchantId, productId);
        if(!doesMerchantAndProductExists){
            return "merchantStock does not exists";
        }
        boolean hasStock = merchantStockService.hasStock(merchantId,productId);
        if(!hasStock){
            return "merchant does not have stock";
        }
        double productPrice = productService.getProductPrice(productId);
        boolean userHasEnoughBalance = productPrice <= getUserBalance(userId);
        if(!userHasEnoughBalance){
            return "user does not have enough credit";}
        Product product = productService.getProductCopy(productId);
        order(userId,productPrice);
        userOrderService.addOrder(new UserOrder("generated!", userId, LocalDateTime.now(),product)); //order history
        merchantStockService.userOrdered(merchantId, productId);//reduce stock by 1
        couponService.reduceUses(productId);
        return "product bought";
    }
    public String buyWithCoupon(String userId, String merchantId,String productId, String couponKey){
        boolean isExists = isExists(userId);
        if(!isExists){
            return "user does not exists";
        }
        boolean doesMerchantAndProductExists = merchantStockService.doesMerchantAndProductExists(merchantId, productId);
        if(!doesMerchantAndProductExists){
            return "merchantStock does not exists";
        }
        boolean hasStock = merchantStockService.hasStock(merchantId,productId);
        if(!hasStock){
            return "merchant does not have stock";
        }
        double productPrice = productService.getProductPrice(productId);
        double couponPercent = couponService.getCouponPercent(couponKey);
        double productPriceWithDiscount = couponService.calculatePrice(productPrice, couponPercent);
        boolean userHasEnoughBalance = productPriceWithDiscount <= getUserBalance(userId);
        if(!userHasEnoughBalance){
            return "user does not have enough credit";}
        boolean isCouponValid = couponService.isCouponValid(merchantId,productId,couponKey);
        if(!isCouponValid){return "coupon is invalid";}
        Product product = productService.getProductCopy(productId);
        product.setPrice(productPriceWithDiscount);//to save product in order history with price after discount
        order(userId,productPriceWithDiscount);
        userOrderService.addOrder(new UserOrder("generated!", userId, LocalDateTime.now(),product)); //order history
        merchantStockService.userOrdered(merchantId, productId);//reduce stock by 1
        couponService.reduceUses(couponKey);
        return "product bought";

    }
    public boolean isExists(String userId){
        for(User user : users){
            if(user.getId().equalsIgnoreCase(userId)){
                return true;
            }
        }
        return false;
    }
    public double getUserBalance(String userId){
        for(User user:users){
            if(user.getId().equalsIgnoreCase(userId)){
                return user.getBalance();
            }
        }
        return 0;
    }

    public void order(String userId,double price){
        for(int i=0;i<users.size();i++){
            if(users.get(i).getId().equalsIgnoreCase(userId)){
                users.get(i).setBalance(users.get(i).getBalance()-price);
            }
        }
    }
    public ArrayList<UserOrder> getOrderHistory(String userId) {
        return userOrderService.getUserOrders(userId);
    }
}
