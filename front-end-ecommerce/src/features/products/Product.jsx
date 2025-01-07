import styled from "styled-components"
import { useProduct } from "./useProduct"
import Spinner from "../../ui/Spinner"
import Section from "../../ui/Section";
import SlideImage from "../../ui/SlideImage";
import { CiStar } from "react-icons/ci";
import Separator from "../../ui/Seperator";
import StartRaing from "../../ui/StartRaing";
import { formatCurrencyVND } from "../../utils/helper";
import TextWithLabel from "../../ui/TextWithLabel";
import Button from "../../ui/Button";
import ButtonGroup from "../../ui/ButtonGroup";
import NumericInput from "../../ui/NumericInput";
import { useEffect, useReducer, useState } from "react";
import { useSKU } from "./useSKU";

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
    useEffect(() => {
        if(!isLoading && product) {
            setSkuState(() => ({
                "price": product.price,
                "stock": product.stock
              }));
        }
    },[isLoading,product])
    useEffect(() => {
       if(!isLoading && product) {
            const isSelected = Object.keys(state).length === Object.keys(product.options).length;
            if(isSelected) {
                const arrayValue =Object.values(state);
                const sku = product.skus.filter(s => JSON.stringify(s.optionValueIndex) === JSON.stringify(arrayValue))
                console.log(arrayValue)
                setSkuState(() => ({
                    "price": sku[0].skuPrice,
                    "stock": sku[0].skuStock
                }));
        }
       }
    },[state,isLoading,product])
    if( isLoading) return <Spinner/>
    const {name, category, images,options} = product;
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
    return (
        <>
            <HeaderProduct>
                {category.pathCategory.map(p => <a href="#">{p} {">"} </a>)}
                <StyledSpan>
                    {name}
                </StyledSpan>
            </HeaderProduct>
            <Section padding columns ={2} bgcolor={'#fff'}>
                <ImageArea><SlideImage images={images}/></ImageArea>
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
                            618 đã bán
                        </Text>
                    </div>
                    <Price>
                        {formatCurrencyVND(skuState.price)}
                    </Price>
                   {
                     Object.entries(options).map(([key, value]) => (
                        <TextWithLabel key={key} label={key}>
                           {value.optionValues.map((op,index) => 
                             <Button active ={state[key] === index ? index+1 : 0} onClick={() =>handleOnClickOption(key,index)}  variation ="select" size ="medium" key={op.id}>
                            {op.name}
                        </Button>
                           )} 
                        </TextWithLabel>
                      ))
                   }
                   <TextWithLabel label="Số lượng">
                        <NumericInput active ={!activeQuantity}>{`${skuState.stock} sản phẩm có sẵn`}</NumericInput>
                   </TextWithLabel>
                </Information>
            </Section>
        </>
    )
}

export default Product
