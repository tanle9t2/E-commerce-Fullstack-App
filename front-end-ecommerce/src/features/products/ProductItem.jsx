import styled from "styled-components";
import Card from "../../ui/Card";


function ProductItem({product}) {
    console.log(product)
    const { images, name, price, rating, totalSell } = product;
    const imageUrl = images?.length ? images[0].imageUrl : null;
    return <Card imageUrl={imageUrl} title={name} price={price} rating={5} totalSell={"4k"}/>
}

export default ProductItem
