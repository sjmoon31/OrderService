package com.shop.service;

import com.shop.common.ModelMapperUtil;
import com.shop.domain.Member;
import com.shop.domain.OrderInfo;
import com.shop.domain.Product;
import com.shop.dto.OrderInfoDTO;
import com.shop.dto.ProductDTO;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderInfoService {
    private final OrderInfoRepository orderInfoRepository;
    private final MemberRepository memberRepository;

    /**
     * 주문내역조회
     * @param start
     * @param limit
     * @param email
     * @return
     */
    public Page<OrderInfoDTO> selectOrderInfoList(int start, int limit, String email){
        Member member = memberRepository.findByEmail(email).get();
        PageRequest pageRequest = PageRequest.of(start-1, limit);
        Page<OrderInfo> result = orderInfoRepository.selectOrderList(pageRequest,member.getMemberSeq());
        List<OrderInfoDTO> list = ModelMapperUtil.mapAll(result.getContent(), OrderInfoDTO.class);
        int total = result.getTotalPages();
        if (total > 0) {
            pageRequest = PageRequest.of((total-1), limit);
        }
        for(int i=0; i < result.getContent().size(); i++){
            Product product = result.getContent().get(i).getProductStock().getProduct();
            list.get(i).setProductDTO(ModelMapperUtil.map(product, ProductDTO.class));
        }
        return new PageImpl<>(list, pageRequest, total);
    }
}
