
import ProductItem from "./ProductItem"
import {useProducts} from"./useProducts"
import Spinner from "../../ui/Spinner"
import Section from "../../ui/Section"
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
