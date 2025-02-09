import styled from "styled-components"
import Button from "../../ui/Button"
import { useNavigate } from "react-router-dom"

const StyledEmptyCart = styled.div`
    display:flex;
    flex-direction:column;
    align-items:center;
    height:60vh;
    justify-content:center;
`
const EmptyImage = styled.div`
    height: 12.125rem;
    width: 12.75rem;
    background-image: url(https://deo.shopeemobile.com/shopee/shopee-pcmall-live-sg/cart/9bdd8040b334d31946f4.png);
    background-position: 50%;
    background-repeat: no-repeat;
    background-size: cover;
    margin:10px 0; 
`
function EmptyCart() {
    const navigate = useNavigate()
    return (
        <StyledEmptyCart>
            <EmptyImage/>
            <p className="mb-5">Giỏ hàng của bạn còn trống</p>
            <Button onClick={() => navigate("/")} size = "large">Mua ngay</Button>
        </StyledEmptyCart>
    )
}

export default EmptyCart
