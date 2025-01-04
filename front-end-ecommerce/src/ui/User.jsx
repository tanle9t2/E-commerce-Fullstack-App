import styled from "styled-components";

const Img = styled.img`
  height: 2.5rem;
  width: auto;
  border-radius:50%;
  margin-right:10px 
`;
const StyledUser =styled.div`
    display:flex;
    align-items:center;
`
function User() {
    return (
       <StyledUser>
        <Img src= {"https://down-vn.img.susercontent.com/file/d89311deb8b93d360d50a0e9389c6fdd_tn"}/>
        <span>Letan</span>
       </StyledUser>
    )
}

export default User
