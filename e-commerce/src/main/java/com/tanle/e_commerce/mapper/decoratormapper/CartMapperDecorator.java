package com.tanle.e_commerce.mapper.decoratormapper;

import com.tanle.e_commerce.Repository.Jpa.UserRepository;
import com.tanle.e_commerce.dto.CartDTO;
import com.tanle.e_commerce.dto.CartItemDTO;
import com.tanle.e_commerce.entities.Cart;
import com.tanle.e_commerce.entities.CartItem;
import com.tanle.e_commerce.mapper.CartItemMapper;
import com.tanle.e_commerce.mapper.CartMapper;
import com.tanle.e_commerce.mapper.TenantMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public abstract class CartMapperDecorator implements CartMapper {
    @Autowired
    private CartMapper delegate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantMapper tenantMapper;
    @Autowired
    private CartItemMapper cartItemMapper;

    @Override
    public CartDTO convertDTO(Cart cart) {
        CartDTO cartDTO = delegate.convertDTO(cart);
        List<CartDTO.GroupCartItemDTO> groupCartItemDTOS = new ArrayList<>();
        var object = cart.getCartItems().stream()
                .collect(Collectors.groupingBy(item -> item.getSku().getProduct().getTenant()));
        for (var x : object.entrySet()) {
            List<CartItemDTO> cartItemDTOS = x.getValue().stream()
                    .map(c -> cartItemMapper.convertDTO(c))
                    .collect(Collectors.toList());
            CartDTO.GroupCartItemDTO groupCartItemDTO = cartDTO.createGroupCartItemDTO(tenantMapper.convertDTO(x.getKey())
                    , cartItemDTOS);
            groupCartItemDTOS.add(groupCartItemDTO);
        }
        cartDTO.setShopOrders(groupCartItemDTOS);
        return cartDTO;
    }

    @Override
    public Cart converEntity(CartDTO cartDTO) {
        return delegate.converEntity(cartDTO);
    }
}
