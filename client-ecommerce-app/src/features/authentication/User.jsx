import styled from "styled-components";
import Avatar from "../../ui/Avatar";
import { useNavigate } from "react-router-dom";
import { useAuthContext } from "../../context/AuthContext";
import Spinner from "../../ui/Spinner";
import { useLogout } from "./useLogout";

const StyledUser = styled.div`
    display:flex;
    align-items:center;
`;


const DropdownContainer = styled.div`
  position: relative;
  display: inline-block;
  &::after {
    content: "";
    position: absolute;
    top: 18px;
    left: 50%;
  
    transform: translateX(-50%);
    width: 90px;
    height: 32px;
  }
`;
const DropdownMenu = styled.div`
  color:var(--black-color);
  position: absolute;
  top: 100%;
  z-index:10;
  right: 0;
  margin-top: 8px;
  width: 180px;
  background: white;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
  display:none;
  ${DropdownContainer}:hover & {
    display: block;
  }
  overflow:hidden;
`;

const MenuItem = styled.div`
  padding: 12px;
  cursor: pointer;
  font-size: 14px;
  transition: background 0.2s;

  &:hover {
    background-color: #f5f5f5;
    color:var(--primary-color)
  }
`;
function User({ fullName, avatar }) {
  const { handleLogout } = useAuthContext()
  const naviate = useNavigate();
  const { isLoading: logoutLoading, logout } = useLogout()
  if (logoutLoading) return <Spinner />
  function handleOnClickLogout() {
    logout(
      {},
      {
        onSuccess: () => {
          handleLogout();
        }
      });
  }
  return (

    <DropdownContainer>
      <StyledUser>
        <Avatar url={avatar} />
        <span>{fullName}</span>
      </StyledUser>


      <DropdownMenu>
        <MenuItem onClick={() => naviate('/user/account/profile')}>Tài Khoản Của Tôi</MenuItem>
        <MenuItem onClick={() => naviate('/user/account/purchase')}>Đơn Mua</MenuItem>
        <MenuItem onClick={() => handleOnClickLogout()}>Đăng Xuất</MenuItem>
      </DropdownMenu>

    </DropdownContainer>
  )
}

export default User
