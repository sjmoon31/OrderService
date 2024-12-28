package com.shop.service;

import com.shop.common.ModelMapperUtil;
import com.shop.domain.Member;
import com.shop.domain.Product;
import com.shop.dto.HeartDTO;
import com.shop.dto.ProductDTO;
import com.shop.repository.MemberRepository;
import com.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    /**
     * 상품정보조회
     * @param productSeq
     * @param email
     * @return
     */
    public ProductDTO selectProductInfo(Long productSeq, String email){
        Member member = memberRepository.findByEmail(email).get();
        Product productInfo = productRepository.selectProduct(productSeq, member.getMemberSeq());
        ProductDTO product = ModelMapperUtil.map(productInfo, ProductDTO.class);
        HeartDTO heart = Optional.ofNullable(product)
                .map(ProductDTO::getHeartList)
                .orElse(null)
                .stream()
                .filter(heartDTO -> heartDTO.getMemberDTO() != null && heartDTO.getMemberDTO().getMemberSeq() == member.getMemberSeq())
                .findFirst()
                .orElse(null);
        product.setHeart(heart);
        return product;
    }
}
