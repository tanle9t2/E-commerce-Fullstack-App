import styled from "styled-components";

const Img = styled.img`
  height: 4.6rem;
  width: auto;
`;
const StyledLogo = styled.div`
  text-align: center;
`;
function Logo({opacity=true}) {

    const imageUrl =  opacity
    ? "https://storage.googleapis.com/ops-shopee-files-live/live/affiliate-blog/2019/05/logo-full-white.png" 
    : "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fe/Shopee.svg/2560px-Shopee.svg.png"
    return (
        <StyledLogo>
            <Img src={imageUrl}/>
        </StyledLogo>
    )
}

export default Logo
