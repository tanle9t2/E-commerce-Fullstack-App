import React, { useState } from "react";
import styled from "styled-components";

import { useCart } from "./useCart";
import { HiOutlineShoppingCart } from "react-icons/hi";
import Button from "../../ui/Button";
import { formatCurrencyVND } from "../../utils/helper";
import { useNavigate } from "react-router-dom";
import { useAuthContext } from "../../context/AuthContext";

// Styled Components
const CartContainer = styled.div`
  position: relative;
  color:var(--black-color);
  display:flex;
  justify-content:center;
  margin-left:20px;

  &::after {
    content: "";
    position: absolute;
    top: 18px;
    left: 50%;
    transform: translateX(-50%);
    width: 54px;
    height: 32px;
  }
`;

const CartButton = styled.button`
  background: none;
  border: none;
  cursor: pointer;
  position: relative;
  font-size: 24px;
`;

const CartCount = styled.span`
     position: absolute;
    top: -8px;
    right: -23px;
    background: var(--color-white);
    color: var(--primary-color);
    font-size: 12px;
    border-radius: 50%;
    padding: 10px 13px;
`;
const Dropdown = styled.div`
  position: absolute;
  z-index:10;
  right: 0;
  top: 40px;
  width: 400px;
  background: white;
  border-radius: 8px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  display: none;
  ${CartContainer}:hover & {
    display: block;
  }
`;

const Header = styled.div`
  background: #f8f8f8;
  padding: 12px;
  font-weight: bold;
  color: gray;
`;

const ItemList = styled.ul`
  list-style: none;
  margin: 0;
  padding: 0;
`;

const Item = styled.li`
  display: flex;
  align-items: start;
  padding: 10px;
  border-bottom: 1px solid #eee;
  &:hover {
    background-color: #f5f5f5;  /* Example hover effect: change background color */
    cursor: pointer;  /* Change cursor to pointer */
  }
`;

const ItemImage = styled.img`
  width: 60px;
  height: 60;
  margin-right: 10px;
  border:1px solid var(--line-color);
`;


const ItemName = styled.p`
    color:var(--black-color);
    font-size: 14px;
    margin: 0;
    white-space: nowrap;        /* Prevents text from wrapping */
    overflow: hidden;          /* Hides the text that overflows */
    text-overflow: ellipsis;   /* Adds ellipsis when the text overflows */
`;

const ItemPrice = styled.span`
  font-size: 14px;
  color: red;
  font-weight: bold;
`;

const Footer = styled.div`
  padding: 10px;
  font-size: 14px;
  justify-content:space-around;
  color: gray;
  display:flex;
  align-items:center;
`;
const EmptyImage = styled.div`
    height: 12.125rem;
    width: 12.75rem;
    background-image: url(https://deo.shopeemobile.com/shopee/shopee-pcmall-live-sg/cart/9bdd8040b334d31946f4.png);
    background-position: 50%;
    background-repeat: no-repeat;
    background-size: cover;
    margin:10px 0; 
`
const Outline = styled.div`
  width:100%;
  display:flex;
  flex-direction:column;
  justify-content:center;
  align-items:center;
  padding:50px;
`
export default function ShoppingCart() {
  const { isLoading, cart } = useCart();
  const navigate = useNavigate();
  if (isLoading) return;
  const cartItems = cart.shopOrders.flatMap(shop =>
    shop.items.reduce((acc, item) => {
      acc.push(item);
      return acc;
    }, [])
  )

  return (
    <CartContainer>
      <CartButton onClick={() => navigate('/cart')}>
        <HiOutlineShoppingCart color="#ffff" fontSize={28} />
        <CartCount>{cartItems.length}</CartCount>
      </CartButton>
      <Dropdown>
        {cartItems.length
          ?
          <>
            <Header>Sản Phẩm Mới Thêm</Header>
            <ItemList>
              {cartItems.map((item, index) => (
                <Item onClick={() => navigate(`/product/${item.product.id}`)} key={index}>
                  <ItemImage src={item.product.images[0].imageUrl} alt={item.product.name} />
                  <ItemName>{item.product.name}</ItemName>
                  <ItemPrice>{formatCurrencyVND(item.sellPrice)}</ItemPrice>

                </Item>
              ))}
            </ItemList>
            <Footer>
              <p>{cartItems.length} Thêm Hàng Vào Giỏ</p>
              <Button onClick={() => navigate("/cart")}>Xem Giỏ Hàng</Button>
            </Footer>
          </>
          : <Outline>
            <EmptyImage />
            <p>Chưa có sản phẩm</p>
          </Outline>
        }
      </Dropdown>

    </CartContainer>
  );
}
