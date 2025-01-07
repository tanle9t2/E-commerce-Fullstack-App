import styled from "styled-components";
import Card from "../../ui/Card";
import {  useNavigate } from "react-router-dom";


function ProductItem({product}) {
    const navigate = useNavigate();
    const { id,images, name, price, rating, totalSell } = product;
    const imageUrl = images?.length ? images[0].imageUrl : null;
    return <Card handleOnClick={() => navigate(`/product/${id}`)} id = {id} imageUrl={imageUrl} title={name} price={price} rating={5} totalSell={"4k"}/>
}

export default ProductItem
