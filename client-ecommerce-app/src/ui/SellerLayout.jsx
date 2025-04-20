import styled from "styled-components";
import SiderBarSeller from "./SiderBarSeller";

export const Container = styled.div`
  display: flex;
  height: 100vh;
`;
function SellerLayout() {
    return (
        <Container>
            <SiderBarSeller />
        </Container>
    )
}

export default SellerLayout
