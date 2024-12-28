package com.shop.controller;

import com.shop.dto.CartDTO;
import com.shop.dto.MemberDTO;
import com.shop.dto.PaymentDTO;
import com.shop.service.CartService;
import com.shop.service.CouponService;
import com.shop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 결제 관련 Controller
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final CouponService couponService;
    private final CartService cartService;

    /**
     * 상품 결제창 조회
     * @param email
     * @return
     */
    @GetMapping(value = "/getPaymentInfo")
    public Map<String, Object> getPaymentInfo(@RequestHeader(value="email",required = false, defaultValue="") String email){
        MemberDTO member = couponService.selectMyCouponInfo(email);
        CartDTO cart =  cartService.findMyCartList(email);
        // 하나의 Map으로 모든 값을 묶어서 반환
        Map<String, Object> map = new HashMap<>();
        map.put("member", member);
        map.put("totalPrice", cart.getTotalPrice());
        map.put("myCartList", cart.getCartList());
        return map;
    }

    /**
     *  주문정보 저장
     * @param payment
     * @return
     */
    @PostMapping(value = "/savePaymentInfo")
    public ResponseEntity<Void> savePaymentInfo(@RequestBody PaymentDTO payment){
        paymentService.savePayment(payment);
        return ResponseEntity.ok().build();
    }

    /**
     * 결제취소
     * @param paymentDTO
     * @return
     */
    @PostMapping(value = "/refund")
    public ResponseEntity<Void> refund(PaymentDTO paymentDTO) throws IOException {
        PaymentDTO payment = paymentService.paymentInfo(paymentDTO.getPaymentSeq());
        String token = paymentService.getToken("8428328123150472","6lox7VLfDYCFGVDu8Kc39Hml8iqmjB1WsMsZpwxooyMJVUb3xJub0y6Atp2AGqPyU27rLNA9GE3D44sI");
        String amount = paymentService.getPaymentInfo(payment.getImpUid(), token);
        paymentService.refundRequest(token, payment.getImpUid(), amount,"결제 금액 오류");
        //paymentService.updateOrderInfoRefund(paymentDTO.getPaymentSeq());
        return ResponseEntity.ok().build();
    }
}
