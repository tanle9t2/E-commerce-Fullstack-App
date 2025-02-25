import React, { useRef, useState } from 'react';
import styled from 'styled-components';
import Logo from '../../ui/Logo';
import { CiSquareMinus, CiSquarePlus } from 'react-icons/ci';
import Table from '../../ui/Table';
import CartRow from './CartRow';
import { useCart } from './useCart';
import Spinner from '../../ui/Spinner'
import Button from '../../ui/Button'
import { useCartContext } from '../../context/CartContext';
import { formatCurrencyVND } from '../../utils/helper';
import Highlight from '../../ui/Highlight';
import EmptyCart from './EmptyCart';
import Modal from '../../ui/Modal';
import ConfirmDelete from '../../ui/ConfirmDelete';
import useDeleteCartItem from './useDeleteCartItem';
import { useNavigate } from 'react-router-dom';
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
const Title2 = styled.button`
  font-size: 1.6rem;
  font-weight: 600;
  display:flex;
  align-items:center;
  cursor: pointer;
`;
const StickyPayment = styled.div`
  display:flex;
  justify-content:space-between;
  width:100%;
  font-size:1.8rem !important;
  background-color:var(--white-color);
`;
const ShopeeCart = () => {
  const navigate = useNavigate();
  const {isLoading ,cart} = useCart();
  const {cartItemTick,handleAddCartItemTick, handleRemoveAll} = useCartContext();
  const {isLoading:isDeleting, deleteCartItem} = useDeleteCartItem()
  const checkAllUseRef = useRef(null);
  if(isLoading) return <Spinner/>
  function handleOnChange(e) {
    if(e.target.checked) {
      const newItems = cart.shopOrders.flatMap(shop =>
        shop.items.map(({skuId, quantity, modelName,sellPrice,product }) => ({
          "tenantName": shop.tenant.name,
          skuId,
          quantity,
          sellPrice,
          modelName,
          "name":product.name,
          "image": product.images[0].imageUrl
        }))
      );
    handleAddCartItemTick(newItems);
    } else {
      handleRemoveAll();
    } 
  }
  function handleOnClickAll() {
    if (checkAllUseRef.current) {
      checkAllUseRef.current.click();
    }
  }
  function handleOnRemove() {
    const cartItems = cart.shopOrders.flatMap(shop =>
      shop.items.reduce((acc,{skuId}) => {
        acc.push(skuId)
        return acc;
      },[]));
    deleteCartItem({cartId:1,cartItems})
    handleRemoveAll();
  }
  const totalItems = cart.shopOrders.reduce((acc, data) => acc + data.items.length, 0);
  const isCheckedAll = totalItems === cartItemTick.length;
  const totalPrice = cartItemTick.reduce((acc,{sellPrice,quantity}) =>acc + sellPrice*quantity,0)
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
      {
        cart.shopOrders.length 
        ? 
        <>
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
            <Title2 >
               <input 
                ref={checkAllUseRef}
                checked= {isCheckedAll} 
                onChange={(e) => handleOnChange(e)} type="checkbox" className="mr-4" />
                <span onClick={() => handleOnClickAll()}>  Chọn tất cả ({totalItems})</span>
            </Title2>
            <Modal>
              <Modal.OpenButton opens="delete">
                <Title2 disabled ={!cartItemTick.length}> Xóa</Title2>
              </Modal.OpenButton>
              <Modal.Window name="delete">
                <ConfirmDelete
                  resourceName="sản phẩm"
                  quantity={cartItemTick.length}
                  onConfirm={() => handleOnRemove()}
                  disabled={isDeleting}
                />
                </Modal.Window>
            </Modal>
            
            <Title2>Tổng thanh toán ({cartItemTick.length} Sản phẩm): <Highlight> {formatCurrencyVND(totalPrice)}</Highlight></Title2>
              <Button onClick={() => navigate("/checkout")} size ="large">Thanh toán</Button>
            </StickyPayment>
          </Table.Footer>
          </Table>
        </>
        : <EmptyCart/>
      }
     </Container>
    </>
  );
};

export default ShopeeCart; 
