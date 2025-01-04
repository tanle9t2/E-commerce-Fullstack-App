import styled from "styled-components"
import Row from "../../ui/Row"
import ProductItem from "./ProductItem"
import {useProduct} from"./useProduct"
import Spinner from "../../ui/Spinner"
const fakeData = [
    {
      "imageUrl":"https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lkv87kkc54gt68_tn.webp",
      "title":"Bông tai bạc đính đá HeliSilver Tiny Diamond S925 - Khuyên tai bạc size đá 2mm, 3mm, 4mm, 5mm, 6mm",
      "rating":4.9,
      "price":39.000,
      "totalSell":"4k",
    },
    {
      "imageUrl":"https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lkv87kkc54gt68_tn.webp",
      "title":"Bông tai bạc đính đá HeliSilver Tiny Diamond S925 - Khuyên tai bạc size đá 2mm, 3mm, 4mm, 5mm, 6mm",
      "rating":4.9,
      "price":39.000,
      "totalSell":"4k",
    },
    {
      "imageUrl":"https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lkv87kkc54gt68_tn.webp",
      "title":"Bông tai bạc đính đá HeliSilver Tiny Diamond S925 - Khuyên tai bạc size đá 2mm, 3mm, 4mm, 5mm, 6mm",
      "rating":4.9,
      "price":39.000,
      "totalSell":"4k",
    },
    {
        "imageUrl":"https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lkv87kkc54gt68_tn.webp",
        "title":"Bông tai bạc đính đá HeliSilver Tiny Diamond S925 - Khuyên tai bạc size đá 2mm, 3mm, 4mm, 5mm, 6mm",
        "rating":4.9,
        "price":39.000,
        "totalSell":"4k",
      },
      {
        "imageUrl":"https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lkv87kkc54gt68_tn.webp",
        "title":"Bông tai bạc đính đá HeliSilver Tiny Diamond S925 - Khuyên tai bạc size đá 2mm, 3mm, 4mm, 5mm, 6mm",
        "rating":4.9,
        "price":39.000,
        "totalSell":"4k",
      },
      {
        "imageUrl":"https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lkv87kkc54gt68_tn.webp",
        "title":"Bông tai bạc đính đá HeliSilver Tiny Diamond S925 - Khuyên tai bạc size đá 2mm, 3mm, 4mm, 5mm, 6mm",
        "rating":4.9,
        "price":39.000,
        "totalSell":"4k",
      },
      {
        "imageUrl":"https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lkv87kkc54gt68_tn.webp",
        "title":"Bông tai bạc đính đá HeliSilver Tiny Diamond S925 - Khuyên tai bạc size đá 2mm, 3mm, 4mm, 5mm, 6mm",
        "rating":4.9,
        "price":39.000,
        "totalSell":"4k",
      },
      {
        "imageUrl":"https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lkv87kkc54gt68_tn.webp",
        "title":"Bông tai bạc đính đá HeliSilver Tiny Diamond S925 - Khuyên tai bạc size đá 2mm, 3mm, 4mm, 5mm, 6mm",
        "rating":4.9,
        "price":39.000,
        "totalSell":"4k",
      },
      {
        "imageUrl":"https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lkv87kkc54gt68_tn.webp",
        "title":"Bông tai bạc đính đá HeliSilver Tiny Diamond S925 - Khuyên tai bạc size đá 2mm, 3mm, 4mm, 5mm, 6mm",
        "rating":4.9,
        "price":39.000,
        "totalSell":"4k",
      }
      
    
  ]
const StyledProductDiplay = styled.div`
    display: grid;
    grid-template-columns: repeat(6, 1fr);
    gap: 16px;
`
function ProductDisplay() {
    const { products, isLoading} = useProduct();
    if(isLoading) return <Spinner/>
  
    return (
        <StyledProductDiplay>
           {products.map(product => <ProductItem key={product.id} product={product}/>)}
        </StyledProductDiplay>
    )
}

export default ProductDisplay
