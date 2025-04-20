import { CiSquareMinus, CiSquarePlus } from "react-icons/ci";
import styled from "styled-components";
import CartItem from "./CartItem";
import  Table  from "../../ui/Table";
import { useCartContext } from "../../context/CartContext";
import { useState } from "react";


const StyledCart = styled.div`
  font-size:1.6rem;
  background-color:var(--color-white);
  margin:20px 0;
  border-radius:5px;
`;

const Label = styled.span`
  background-color:var(--primary-color);
  color: #ffffff;
  font-size: 0.75rem;
  padding: 0.25rem 0.5rem;
  border-radius: 0.375rem;
  margin-right: 0.5rem;
`;
const ProductRow = styled.div`
  display: flex;
  align-items: center;
  padding: 1rem 0;

`;

const ProductInfo = styled.div`
  display: flex;
  align-items: center;
`;

function CartRow({cartItem}) {
    const {tenant,items} = cartItem;
    const {cartItemTick,handleAddCartItemTick, handleRemoveCartItemTick} = useCartContext();
    let cnt = 0;
    let itemCheckIds= []
    items.forEach(item => cartItemTick.forEach(c => {
        if(c.skuId === item.skuId){
            cnt++;
            itemCheckIds.push(c.skuId);
        }
    }))
    const [countCheck,setCountCheck] = useState(cnt)
    function handleOnchange(e) {
        if(e.target.checked) {
            const newItem = items.map(({ skuId, quantity, modelName,sellPrice,product }) => ({
                "tenantName":tenant.name,
                skuId,
                quantity,
                sellPrice,
                modelName,
                "name":product.name,
                "image":product.images[0].imageUrl,
              }));
            handleAddCartItemTick(newItem);              
            setCountCheck(items.length);
        }
        else{
            const removeItems = items.reduce((acc, { skuId }) => {
                acc.push(skuId);
                return acc;
              }, []);
            handleRemoveCartItemTick(removeItems)
            setCountCheck(0)
        }
    }
    return (
       <StyledCart>
         <Table.Row>
            <ProductRow>
                <input checked={cnt=== items.length} onChange={(e) => handleOnchange(e)} type="checkbox" className="mr-4" />
                <ProductInfo>
                <Label>Mall</Label>
                <span>{tenant.name}</span>
                </ProductInfo>
            </ProductRow>
        </Table.Row>
       {items.map((item) => <CartItem key={item.skuId}
            tenantName = {tenant.name} 
            skuId={item.skuId}
            setCountCheck = {setCountCheck}
            isChecked={itemCheckIds.includes(item.skuId)}
            image={item.product.images[0].imageUrl} 
            sellPrice={item.sellPrice}
            nameProduct={item.product.name}
            modelName={item.modelName}
            quantity={item.quantity} 
             />)}
        </StyledCart>
    )
}

export default CartRow
