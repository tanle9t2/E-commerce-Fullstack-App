import React, { useState } from 'react';
import styled from 'styled-components';
import Logo from '../../ui/Logo';
import { CiSquareMinus, CiSquarePlus } from 'react-icons/ci';
import Table from '../../ui/Table';
import CartRow from './CartRow';
import { useCart } from './useCart';
import Spinner from '../../ui/Spinner'
import Button from '../../ui/Button'
import { useCartContext } from '../../context/CartContext';

const Container = styled.div`
    padding: var(--padding-container);
  font-family: Arial, sans-serif;
  background-color: var(--color-grey-50);
`;

const Header = styled.header`
  display: flex;
  justify-content: space-between;
  align-items: center;


  border-bottom: 1px solid #ddd;
  background-color: var(--color-white);
  padding: var(--padding-container);
`;
const SearchBar = styled.input`
  width: 400px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px 0 0 4px;
`;

const SearchButton = styled.button`
  background-color: #ee4d2d;
  border: none;
  color: white;
  padding: 10px 20px;
  border-radius: 0 4px 4px 0;
  cursor: pointer;
`;
const Title = styled.h2`
  font-size: 1.25rem;
  font-weight: 600;
  display:flex;
  align-items:center;
`;
const StickyPayment = styled.div`
  display:flex;
  justify-content:space-between;
  width:100%;
  background-color:var(--white-color);
`
const ShopeeCart = () => {
  const {isLoading ,cart} = useCart();
  const {cartItemTick,handleAddCartItemTick, handleRemoveCartItemTick} = useCartContext();
  if(isLoading) return <Spinner/>
  function handleOnChange(e) {
    if(e.target.checked) {
      const newItems = cart.shopOrders.flatMap(shop =>
        shop.items.map(({ skuId, quantiy, sellPrice }) => ({
          skuId,
          quantiy,
          sellPrice
        }))
      );
    handleAddCartItemTick(newItems);
    } else {
      const removeItems = cart.shopOrders.flatMap(shop =>
        shop.items.reduce((acc, { skuId }) => {
          acc.push(skuId);
          return acc;
        }, [])
      );      
    handleRemoveCartItemTick(removeItems);
    } 
  }
  const totalItems = cart.shopOrders.reduce((acc, data) => acc + data.items.length, 0);
  const isCheckedAll = totalItems === cartItemTick.length;

  return (
   <>
    <Header>
        <Logo opacity={false}/>
        <div>
          <SearchBar placeholder="SALE SHOP MỚi Đến 100.000Đ" />
          <SearchButton>🔍
          </SearchButton>
        </div>
      </Header>
    <Container>
    <Table columns="3.5fr 1fr 1fr 0.5fr 1fr">
          <Table.Header>
              <Title> <input checked= {isCheckedAll} onChange={(e) => handleOnChange(e)} type="checkbox" className="mr-4" /> Sản Phẩm</Title>
              <Title>Đơn Giá</Title>
              <Title>Số Tiền</Title>
              <Title>Số Lượng</Title>
              <Title>Thao Tác</Title>
          </Table.Header> 

          <Table.Body
          data={cart.shopOrders}
          render={(cartItem) => (
            <CartRow key={cartItem.id} cartItem={cartItem} />
          )}
        />
        <Table.Footer>
          <StickyPayment>
          <Title> <input type="checkbox" className="mr-4" /> Chọn tất cả</Title>
          <Title> Xóa</Title>
          <Title>Số Lượng</Title>
          <Title>Tổng thanh toán (0 Sản phẩm):₫0</Title>
            <Button size ="large">Thanh toán</Button>
          </StickyPayment>
        </Table.Footer>
        </Table>
        
     </Container>
    </>
  );
};

export default ShopeeCart; 
