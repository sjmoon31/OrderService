package com.shop.controller;

import com.shop.dto.OrderInfoDTO;
import com.shop.service.OrderInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class OrderController {
    private final OrderInfoService orderInfoService;

    /**
     * 주문내역 조회
     * @param email
     * @param page
     * @return
     */
    @GetMapping("/getMyOrderList")
    public Map<String, Object> getMyOrderList(@RequestHeader(value="email",required = false, defaultValue="") String email
            , @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<OrderInfoDTO> myOrderList = orderInfoService.selectOrderInfoList(page, 10,email);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("myOrderList", myOrderList);
        return resultMap;
    }
}
