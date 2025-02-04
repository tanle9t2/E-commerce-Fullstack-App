import { CiSquareMinus, CiSquarePlus } from "react-icons/ci";
import styled from "styled-components";
import Table from "../../ui/Table";
import { formatCurrencyVND } from "../../utils/helper";
import { useState } from "react";
import { useCartContext } from "../../context/CartContext";

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


function CartItem({isChecked,skuId,image,nameProduct,quantity,modelName, sellPrice,setCountCheck}) {
    const {handleAddCartItemTick, handleRemoveCartItemTick} = useCartContext();
    function handleOnChange(e) {
        if(e.target.checked) {
            setCountCheck(cnt => cnt++);
            handleAddCartItemTick([
                {
                    "skuId":skuId,
                    "quantity":quantity,
                    "sellPrice":sellPrice,
                }
            ])
        } else {
            handleRemoveCartItemTick([skuId])
            setCountCheck(cnt => cnt--);
        }
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
                <QuantityButton><CiSquareMinus size={16} /></QuantityButton>
                <span className="px-4">{quantity}</span>
                <QuantityButton><CiSquarePlus  size={16} /></QuantityButton>
            </QuantityControl>

            <div className="text-red-500 ml-4">
                Xóa
            </div>
        </Table.Row>
    )
}

export default CartItem
