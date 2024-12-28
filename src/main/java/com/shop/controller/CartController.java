package com.shop.controller;

import com.shop.dto.CartDTO;
import com.shop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 장바구니 관련 Controller
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    /**
     * 장바구니 목록 조회
     * @return
     */
    @GetMapping("/getCartList")
    public Map<String, Object> getCartList(@RequestHeader(value="email",required = false, defaultValue="") String email) {
        CartDTO cartDTO = cartService.findMyCartList(email);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("totalPrice", cartDTO.getTotalPrice());
        resultMap.put("myCartList", cartDTO.getCartList());
        return resultMap;
    }

    /**
     * 장바구니 추가
     * @param cart
     * @return
     */
    @PostMapping("/addCartInfo")
    public ResponseEntity<Void> addCartInfo(@RequestHeader(value="email",required = false, defaultValue="") String email
            , @RequestBody CartDTO cart){
        cartService.addCart(cart.getProductStockSeq(), cart.getQuantity(), email);
        return ResponseEntity.ok().build();
    }

    /**
     * 장바구니 삭제
     * @param cart
     * @return
     */
    @PostMapping("/removeCartInfo")
    public ResponseEntity<Void> removeCartInfo(@RequestBody CartDTO cart) {
        cartService.deleteCartInfo(cart);
        return ResponseEntity.ok().build();
    }

    /**
     * 장바구니 수량 변경
     * @param cart
     * @return
     */
    @PostMapping("/updateCartQuantity")
    public ResponseEntity<Void> updateCartQuantity(@RequestBody CartDTO cart) {
        cartService.updateProductQuantity(cart);
        return ResponseEntity.ok().build();
    }
}
