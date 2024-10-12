package com.tanle.e_commerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tanle.e_commerce.entities.Cart;
import com.tanle.e_commerce.entities.CartItem;
import com.tanle.e_commerce.entities.Tenant;
import com.tanle.e_commerce.entities.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartDTO {
    private int id;
    private List<GroupCartItemDTO> shopOrders;
    private UserDTO user;

    public GroupCartItemDTO createGroupCartItemDTO(TenantDTO tenant, List<CartItemDTO> itemDTOS) {
        return new GroupCartItemDTO(tenant,itemDTOS);
    }
    @Data
    public class GroupCartItemDTO {
        private TenantDTO tenant;
        private  List<CartItemDTO> items;
        public GroupCartItemDTO() {
            this(null,null);
        }
        public GroupCartItemDTO(TenantDTO tenant, List<CartItemDTO> items) {
            this.tenant = tenant;
            this.items = items;
        }
        public CartDTO builder(int id, User user, List<CartItem> cartItem) {
            List<GroupCartItemDTO> groupCartItemDTOS = new ArrayList<>();
            var object = cartItem.stream()
                    .collect(Collectors.groupingBy(item -> item.getSku().getProduct().getTenant()));
            for(var x : object.entrySet()) {
                List<CartItemDTO> cartItemDTOS = x.getValue().stream()
                                .map(CartItem::converDTO)
                                .collect(Collectors.toList());
                groupCartItemDTOS.add(new GroupCartItemDTO(x.getKey().converDTO(),cartItemDTOS));
            }
            return new CartDTO(id,groupCartItemDTOS,user.convertDTO());
        };
    }

}
