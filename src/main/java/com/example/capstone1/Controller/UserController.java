package com.example.capstone1.Controller;

import com.example.capstone1.ApiResponse.ApiResponse;
import com.example.capstone1.Model.Product;
import com.example.capstone1.Model.User;
import com.example.capstone1.Model.UserOrder;
import com.example.capstone1.Service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PostMapping("/add")
    public ResponseEntity addUser(@RequestBody @Valid User user, Errors errors){
        String condition = userService.addUser(user, errors);
        return switch (condition){
            case "0" -> ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
            case "1" -> ResponseEntity.status(200).body(new ApiResponse("user added"));
            default -> throw new IllegalStateException("Unexpected value: " + condition);
        };
    }

    @GetMapping("/users")
    public ResponseEntity getUsers(){
        ArrayList<User> users = userService.getUsers();
        if(users.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("there are no users"));
        }else return ResponseEntity.status(200).body(users);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateUser(@PathVariable String id, @RequestBody @Valid User user, Errors errors){
        String condition = userService.updateUser(id,user, errors);
        return switch (condition){
            case "0" -> ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
            case "1" -> ResponseEntity.status(200).body(new ApiResponse("user is updated: "+id));
            case "2" -> ResponseEntity.status(400).body(new ApiResponse("user not found"));
            default -> throw new IllegalStateException("Unexpected value: " + condition);
        };
    }
    @DeleteMapping("/remove/{id}")
    public ResponseEntity removeUser(@PathVariable String id){
        String condition = userService.removeUser(id);
        return switch (condition){
            case "0" -> ResponseEntity.status(400).body("user not found");
            case "1" -> ResponseEntity.status(200).body("user removed: "+id);
            default -> throw new IllegalStateException("Unexpected value: " + condition);
        };
    }

    @PostMapping("/buy/{userId}/{merchantId}/{productId}")
    public ResponseEntity buyProduct(@PathVariable String userId ,@PathVariable String merchantId, @PathVariable String productId){
        String message = userService.buyProduct(userId,merchantId,productId);
        return switch (message) {
            case "user does not exists" -> ResponseEntity.status(400).body(new ApiResponse("user not found"));
            case "merchantStock does not exists" ->
                    ResponseEntity.status(400).body(new ApiResponse("merchant or product does not exists"));
            case "merchant does not have stock" ->
                    ResponseEntity.status(400).body(new ApiResponse("merchant does not have stock"));
            case "user does not have enough credit" ->
                    ResponseEntity.status(400).body(new ApiResponse("user does not have enough credit"));
            default -> ResponseEntity.status(200).body(new ApiResponse(message));
        };
    }

    @GetMapping("/orders/{userId}")
    public ResponseEntity getOrders(@PathVariable String userId){
        ArrayList<UserOrder> userOrders = userService.getOrderHistory(userId);
        return ResponseEntity.status(200).body(userOrders);
    }

    @PostMapping("/buy/{userId}/{merchantId}/{productId}/{couponKey}")
    public ResponseEntity buyWithCoupon(@PathVariable String userId, @PathVariable String merchantId,
                                             @PathVariable String productId,@PathVariable String couponKey){
        String message = userService.buyWithCoupon(userId,merchantId,productId,couponKey);
        return switch (message) {
            case "user does not exists" -> ResponseEntity.status(400).body(new ApiResponse("user not found"));
            case "merchantStock does not exists" ->
                    ResponseEntity.status(400).body(new ApiResponse("merchant or product does not exists"));
            case "merchant does not have stock" ->
                    ResponseEntity.status(400).body(new ApiResponse("merchant does not have stock"));
            case "user does not have enough credit" ->
                    ResponseEntity.status(400).body(new ApiResponse("user does not have enough credit"));
            case "coupon is invalid" -> ResponseEntity.status(400).body(new ApiResponse("coupon is invalid"));
            default -> ResponseEntity.status(200).body(new ApiResponse(message));
        };

    }
}
