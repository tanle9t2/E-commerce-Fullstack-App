import styled from "styled-components";
import Card from "../../ui/Card";
import {  useNavigate } from "react-router-dom";


function ProductItem({product}) {
    const navigate = useNavigate();
    const { id,images, name, minPrice, rating, totalSell } = product;
    const imageUrl = images?.length ? images[0].imageUrl : null;
    return <Card handleOnClick={() => navigate(`/product/${id}`)} totalSell ={totalSell} id = {id} imageUrl={imageUrl} title={name} price={minPrice} rating={5} />
}

export default ProductItem
