package com.example.capstone1.Service;

import com.example.capstone1.Model.Cart;
import com.example.capstone1.Model.Product;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CartService {
    ArrayList<Cart> carts = new ArrayList<>();
    private final MerchantStockService merchantStockService;
    private final ProductService productService;
    private final UserService userService;

    public String addToCart(String userId, String merchantId, String productId) {
        boolean doesUserExists = userService.isExists(userId);
        if(!doesUserExists){
            return "0";
        }
        boolean doesStockExists = merchantStockService.doesMerchantAndProductExists(merchantId,productId);
        if(!doesStockExists){
            return "1";
        }
        Product product = productService.getProduct(productId);
        carts.add(new Cart(userId,merchantId,product));
        return "default";
    }

    public ArrayList<Cart> getCart(String userId) {
        ArrayList<Cart> userCart = new ArrayList<>();
        for(Cart cart:carts){
            if(cart.getUserId().equalsIgnoreCase(userId)){
                userCart.add(cart);
            }
        }
        return userCart;
    }

    public String removeFromCart(String userId, String merchantId, String productId) {
        for(int i=0; i<carts.size();i++){
            if(carts.get(i).getUserId().equalsIgnoreCase(userId)
            && carts.get(i).getMerchantId().equalsIgnoreCase(merchantId)
            && carts.get(i).getProduct().getId().equalsIgnoreCase(productId)){
                carts.remove(i);
                return "1";
            }
        }
        return "0";
    }
}
