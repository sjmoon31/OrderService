package com.shop.service;

import com.shop.common.ModelMapperUtil;
import com.shop.domain.Cart;
import com.shop.domain.Member;
import com.shop.domain.Product;
import com.shop.domain.ProductStock;
import com.shop.dto.CartDTO;
import com.shop.dto.ProductDTO;
import com.shop.repository.CartRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.ProductRepository;
import com.shop.repository.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductStockRepository productStockRepository;
    private final ProductRepository productRepository;

    /**
     * 장바구니 추가
     * @param productStockSeq
     * @param quantity
     * @param email
     */
    public void addCart(Long productStockSeq, int quantity, String email){
        LocalDateTime nowDate = LocalDateTime.now();
        Cart cart = new Cart();
        Member member = memberRepository.findByEmail(email).get();
        Optional<ProductStock> productStock = productStockRepository.findById(productStockSeq);
        cart.createCart(member, productStock.get(), quantity, nowDate);
        cartRepository.save(cart);
    }

    /**
     * 장바구니 삭제
     * @param cartDTO
     */
    @Transactional
    public void deleteCartInfo(CartDTO cartDTO){
        Cart cart = cartRepository.findById(cartDTO.getCartSeq()).get();
        cartRepository.delete(cart);
    }

    /**
     * 장바구니 목록 조회
     * @param email
     * @return
     */
    public CartDTO findMyCartList(String email){
        int total = 0;
        Member member = memberRepository.findByEmail(email).get();
        List<Cart> myCartList = cartRepository.myCartList(member);
        List<CartDTO> cartList = ModelMapperUtil.mapAll(myCartList,CartDTO.class);

        for(int i = 0; i < cartList.size(); i++){
            Cart cart = myCartList.get(i);
            Product product = productRepository.selectProduct(cart.getProductStock().getProduct().getProductSeq(), 0L);
            ProductDTO productDTO = ModelMapperUtil.map(product, ProductDTO.class);
            cartList.get(i).setProductDTO(productDTO);
            total += product.getPrice() * cart.getQuantity();
        }

        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartList(cartList);
        cartDTO.setTotalPrice(total);
        return cartDTO;
    }

    /**
     * 장바구니 수량 변경
     * @param cartDTO
     * @return
     */
    @Transactional
    public long updateProductQuantity(CartDTO cartDTO){
        return cartRepository.updateProductQuantity(cartDTO.getCartSeq(), cartDTO.getQuantity());
    }
}
