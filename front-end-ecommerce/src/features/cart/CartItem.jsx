import { CiSquareMinus, CiSquarePlus } from "react-icons/ci";
import styled from "styled-components";
import Table from "../../ui/Table";
import { formatCurrencyVND } from "../../utils/helper";
import { useState } from "react";
import { useCartContext } from "../../context/CartContext";
import { useUpdateCart } from "./useUpdateCart";
import useDeleteCartItem from "./useDeleteCartItem";
import Highlight from "../../ui/Highlight";
import Modal from "../../ui/Modal";
import ConfirmDelete from "../../ui/ConfirmDelete";
import Button from "../../ui/Button";
import toast from "react-hot-toast";

const ProductRow = styled.div`
  display: flex;
  align-items: center;
  padding: 1rem 0;

`;

const ProductInfo = styled.div`
  display: flex;
  align-items: center;
`;

const ProductImage = styled.img`
  width: 5rem;
  height: 5rem;
  object-fit: cover;
  margin-right: 1rem;
`;

const ProductDetails = styled.div`
  flex: 1;
`;

const ProductName = styled.p`
  font-weight: 500;
`;

const ProductCategory = styled.p`
 
  color: #6b7280;
`;

const Price = styled.div`
  font-weight: 600;
  color: var(--primary-color);
  margin-left: 1rem;
`;

const QuantityControl = styled.div`
  display: flex;
  align-items: center;
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
`;

const QuantityButton = styled.button`
  background: none;
  border: none;
  padding: 0.5rem;
  cursor: pointer;
`;
const DeleteButton = styled(Highlight)`
    cursor: pointer;
    text-decoration:underline;
`;

function CartItem({tenantName,isChecked,skuId,image,nameProduct,quantity,modelName, sellPrice,setCountCheck}) {
    const {handleAddCartItemTick, handleUpdateQuantity,handleRemoveCartItemTick} = useCartContext();
    const {updateCartItem} = useUpdateCart()
    const {deleteCartItem} = useDeleteCartItem();
    function handleOnChange(e) {
        if(e.target.checked) {
            setCountCheck(cnt => cnt++);
            handleAddCartItemTick([
                {
                    "tenantName": tenantName,
                    "skuId":skuId,
                    "quantity":quantity,
                    "sellPrice":sellPrice,
                    "image":image,
                    "modelName":modelName,
                    "name":nameProduct
                }
            ])
        } else {
            handleRemoveCartItemTick([skuId])
            setCountCheck(cnt => cnt--);
        }
    }
    function handleOnClicIncrease() {
      quantity++;
      handleUpdateQuantity(skuId,quantity)
      updateCartItem({skuId,quantity});
    }
    function handleOnClickDecrease() {
      quantity--;
      handleUpdateQuantity(skuId,quantity)
      updateCartItem({skuId,quantity});
    }
    function handleOnClickRemove() {
      handleRemoveCartItemTick([skuId])
      setCountCheck(cnt => cnt--);
      deleteCartItem({cartItems:[skuId]})
    }
    
    return (
        <Table.Row>
           <ProductRow>
                <input checked = {isChecked} onChange={(e)=> handleOnChange(e)} type="checkbox" className="mr-4" />
                <ProductImage src={image} alt={nameProduct} />
                <ProductDetails>
                <ProductName>{nameProduct}</ProductName>
                <ProductCategory>Phân Loại Hàng: <span className="font-medium">{modelName}</span></ProductCategory>
                </ProductDetails>
           </ProductRow>
            <div className="line-through text-gray-400 mr-4">{formatCurrencyVND(sellPrice)}</div>
            <Price>{formatCurrencyVND(sellPrice*quantity)}</Price>

            <QuantityControl>
                <QuantityButton disabled ={quantity ===1}   onClick={() => handleOnClickDecrease()}><CiSquareMinus size={16} /></QuantityButton>
                <span className="px-4">{quantity}</span>
                <QuantityButton onClick={() => handleOnClicIncrease()}><CiSquarePlus  size={16} /></QuantityButton>
            </QuantityControl>
            <div onClick={() => handleOnClickRemove()} className="text-red-500 ml-4">
              <DeleteButton>Xóa</DeleteButton>
            </div>
        </Table.Row>
    )
}

export default CartItem
