import styled from "styled-components";
import { formatCurrencyVND } from "../../utils/helper";

const StyledOrderItem = styled.div`
  display: flex;
  align-items: center;
  border-bottom:1px solid var(--line-color);
  margin:15px 0;
`;


const ProductImage = styled.img`
  width: 80px;
  height: 80px;
  margin-right: 15px;
`;

const ProductDetails = styled.div`
    flex: 1;
    display:flex;
    align-items:center;
    justify-content:space-between;
`;

const ProductTitle = styled.a`
  font-size: 1.6rem;
  font-weight:500;
`;

const ProductPrice = styled.p`
  color: red;
  font-weight: bold;
`;
const ProductVariation = styled.p`
    color:var(--secondary-color);
    font-size:1.4rem;
`
function OrderItem({ item }) {

  const { image, productId, productName, variation, quantity, price } = item;
  return (
    <StyledOrderItem>
      <ProductImage src={image} alt={productName} />
      <ProductDetails>
        <div>
          <ProductTitle href={`/product/${productId}`}>{productName}</ProductTitle>
          <ProductVariation>{variation}</ProductVariation>
          <p>x{quantity}</p>
        </div>
        <div>
          <ProductPrice>{formatCurrencyVND(price)}</ProductPrice>
        </div>
      </ProductDetails>
    </StyledOrderItem>
  )
}

export default OrderItem
