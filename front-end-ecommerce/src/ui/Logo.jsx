import styled from "styled-components";

const Img = styled.img`
  height: 4.6rem;
  width: auto;
`;
const StyledLogo = styled.div`
  text-align: center;
`;
function Logo() {
    return (
        <StyledLogo>
            <Img src="https://storage.googleapis.com/ops-shopee-files-live/live/affiliate-blog/2019/05/logo-full-white.png"/>
        </StyledLogo>
    )
}

export default Logo
