import styled from "styled-components";

const Icon = styled.div`
  background-image: url(https://deo.shopeemobile.com/shopee/shopee-pcmall-live-sg/shopmicrofe/6202cbd8f3f78638666d.png);
  background-size: contain;
  height: 120px;
  margin: 0 auto;
  width: 120px;
`
const StyledEmpty = styled.div`
  display:flex;
  align-items:center;
  justify-content:center;
  flex-direction:column;
  width:100%;
`
function Empty({ message, children }) {
  return (
    <StyledEmpty>
      <Icon></Icon>
      <p>{message}</p>
      {children}
    </StyledEmpty>
  )

}

export default Empty;
