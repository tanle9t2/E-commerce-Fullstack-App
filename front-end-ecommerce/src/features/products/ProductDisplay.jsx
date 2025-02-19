
import ProductItem from "./ProductItem"
import {useProducts} from"./useProducts"
import Spinner from "../../ui/Spinner"
import Section from "../../ui/Section"

function ProductDisplay() {
    const { products, isLoading} = useProducts();
    if(isLoading) return <Spinner/>
  
    return (
        <Section>
           {products.map(product => <ProductItem key={product.id} product={product}/>)}
        </Section>
    )
}

export default ProductDisplay
