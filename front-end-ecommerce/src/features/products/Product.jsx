import styled from "styled-components"
import { useProduct } from "./useProduct"
import Spinner from "../../ui/Spinner"
import Section from "../../ui/Section";
import SlideImage from "../../ui/SlideImage";
import Separator from "../../ui/Seperator";
import StartRaing from "../../ui/StartRaing";
import { formatCurrencyVND } from "../../utils/helper";
import TextWithLabel from "../../ui/TextWithLabel";
import Button from "../../ui/Button";
import ButtonGroup from "../../ui/ButtonGroup";
import NumericInput from "../../ui/NumericInput";
import { useEffect, useReducer, useRef, useState } from "react";
import ProductDescription from "./ProductDescription";
import TenantInfor from "../tenant/TenantInfor";
import ProductRate from "./ProductRate";
import {useAddCartItem} from "../cart/useAddCartItem"
import ErrorText from "../../ui/ErrorText";
import { useNavigate } from "react-router-dom";
import { useCartContext } from "../../context/CartContext";
const HeaderProduct= styled.div`
    color: var(--color-blue-700);
`
const StyledSpan = styled.span`
    color:var(--black-color);
`;
const Information = styled.div`
    font-size:1.8rem;
`
const ImageArea = styled.div`
    
`
const Text = styled.span`
    display:flex;
    align-items:center;
    margin: 20px;
`
const Price = styled.div`
    color:var(--primary-color);
    font-weight:bold;
    font-size:2.5rem;
    background-color:var(--color-grey-50);
    padding:20px;
`
const Background = styled.div`
    margin:20px 0;
    padding:20px;
    background-color: ${(props) => props.error !==0 ? `#fff5f5!important` :`none`};
`
const initOptionsState ={

}
function reducer(state,action) {
    switch (action.type) {
        case "addOption":
            return {
                ...state,
                [action.payload.key]: action.payload.id
                };
        case "removeOption": 
            return Object.fromEntries(
                Object.entries(state).filter(([key]) => key !== action.payload.key)
            );
            
        default:
          throw new Error("Unknow action");
    }  
}
function Product() {
    const {isLoading, product} = useProduct()
    const [state, dispatch] = useReducer(reducer, initOptionsState);
    const [skuState,setSkuState] = useState({});
    const { addCartItem} = useAddCartItem();
    const [quantity, setQuantity] = useState(1); // Initial value is 1
    const [error,setError] = useState(false);
    const navigate = useNavigate();
    const {handleAddCartItemTick} = useCartContext();
    useEffect(() => {
        if(!isLoading && product) {
            const price = product.minPrice ===product.maxPrice ?product.minPrice :[product.minPrice,product.maxPrice];
            setSkuState(() => ({
                "price": price,
                "stock": product.stock
              }));
        }
    },[isLoading,product])
    useEffect(() => {
        if (!isLoading && product) {
            const isSelected = Object.keys(state).length === Object.keys(product.options).length;
    
            if (isSelected) {
                // Sort and prepare the state for comparison
                const sortedState = Object.keys(state)
                    .sort()
                    .reduce((acc, key) => {
                        acc[key] = state[key];
                        return acc;
                    }, {});
    
                // Convert the sorted state to an array of values
                const arrayValue = Object.values(sortedState);
    
                // Find the SKU that matches the selected options
                const matchingSku = product.skus.find(s => {
                    return arrayValue.every((value, index) => value === s.optionValueIndex[index]);
                });
    
                if (matchingSku) {
                    setSkuState({
                        skuId: matchingSku.skuId,
                        price: matchingSku.skuPrice,
                        stock: matchingSku.skuStock
                    });
                }
            }
        }
    }, [state, isLoading, product]);
    if(isLoading) return <Spinner/>
    const {name, category, description,images,options,totalSell,stock,tenantId} = product;
    const activeQuantity =Object.keys(state).length === Object.keys(product.options).length
    function handleOnClickOption(key,id) {
        const isPresent = key in state && state[key] === id
        if(!isPresent) {
            dispatch({type:"addOption", payload:{
                key,
                id
            }})
        } else {
            dispatch({type:"removeOption",payload:{
                key
            }})
        }
    }
    function handleOnClickAddCartItem() {
        if(activeQuantity) {
            addCartItem({cartId:1,skuId:skuState.skuId, quantity:quantity})
            setError(false)
            return;
        }
        setError(true)
    }
    function handleOnClickBuyNow() {
        if(activeQuantity) {
            addCartItem({cartId:1,skuId:skuState.skuId, quantity:quantity}, {
                onSettled: () => {
                    handleAddCartItemTick([
                        {
                            "skuId":skuState.skuId,
                            "quantity":quantity,
                            "sellPrice":skuState.price,
                        }
                    ])
                    navigate('/cart')
                }
            })
            
        }
        setError(true)
    }
    return (
        <>
            <HeaderProduct>
                {category.pathCategory.map(p => <a href="#">{p} {">"} </a>)}
                <StyledSpan>
                    {name}
                </StyledSpan>
            </HeaderProduct>
            <Section padding columns ={2} bgcolor={'#fff'}>
                <ImageArea><SlideImage isButton = {images.length > 5} images={images}/></ImageArea>
                <Information>
                    <h1 className="text-4xl">{name}</h1>
                    <div className="flex items-center">
                        <Text>
                            4.9 <StartRaing count={Math.ceil(4.9)}/>
                        </Text>
                        <Separator/>
                        <Text>
                            0 đánh giá
                        </Text>
                        <Separator/>
                        <Text>
                            {totalSell} đã bán
                        </Text>
                    </div>
                    <Price>
                        {!Array.isArray(skuState.price) ? formatCurrencyVND(skuState.price) : skuState.price?.map(p => formatCurrencyVND(p)).join(" ~ ")}
                    </Price>
                    <Background error = {error ? 1 : 0}>
                        {options && Object.entries(options).map(([key, value]) => (
                                <TextWithLabel key={key} label={key}>
                                    {value.optionValues?.map((op, index) => (
                                        <Button 
                                            key={op.id} 
                                            active={state?.[value.id] === index ? index + 1 : 0} 
                                            onClick={() => handleOnClickOption(value.id, index)}  
                                            variation="select" 
                                            size="medium"
                                        >
                                            {op.name}
                                        </Button>
                                    ))} 
                                </TextWithLabel>
                            ))}

                        <TextWithLabel label="Số lượng">
                            <NumericInput 
                                value={quantity} 
                                setValue={setQuantity} 
                                maxValue={skuState?.stock ?? 0} 
                                active={!activeQuantity}
                            >
                                {`${skuState?.stock ?? 0} sản phẩm có sẵn`}
                            </NumericInput>
                        </TextWithLabel>
                       {error &&  <ErrorText>Vui lòng chọn phân loại hàng</ErrorText>}
                     </Background>   
                   <ButtonGroup>
                        <Button onClick={() => handleOnClickAddCartItem()} variation ="select">Thêm vào giỏ hàng</Button>
                        <Button onClick={() =>handleOnClickBuyNow()}>Mua ngay</Button>
                   </ButtonGroup>
                </Information>
            </Section>
            <TenantInfor tenantId = {tenantId}/>
            <ProductDescription description={description} stock={stock} category={category.pathCategory}/>
            <ProductRate/>
        </>
    )
}

export default Product
