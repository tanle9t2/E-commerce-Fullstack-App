import styled from "styled-components";
import Avatar from "./Avatar";

const StyledUser =styled.div`
    display:flex;
    align-items:center;
`
function User() {
    return (
       <StyledUser>
        <Avatar src= {"https://down-vn.img.susercontent.com/file/d89311deb8b93d360d50a0e9389c6fdd_tn"}/>
        <span>Letan</span>
       </StyledUser>
    )
}

export default User
