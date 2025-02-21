import { Outlet, useLocation, useNavigate } from "react-router-dom";
import styled from "styled-components";

const ProfileContainer = styled.div`
  display: flex;
  padding: 20px;
  background-color: #f8f8f8;
`;

const Sidebar = styled.div`
  width: 250px;
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-right: 20px;
`;

const SidebarItem = styled.div`
  padding: 10px 0;
  cursor: pointer;
  color: ${(props) => (props.active ? "#ff4d4f" : "#333")};
  font-weight: ${(props) => (props.active ? "bold" : "normal")};

  &:hover {
    color: #ff4d4f;
  }
`;

function ProfileLayOut() {
    const navigate =useNavigate();
    const location = useLocation();
    return (<ProfileContainer>
    <Sidebar>
        <SidebarItem 
            onClick={() => navigate('/user/account/profile')} 
            active={location.pathname === "/user/account/profile"}
        >
            Hồ Sơ
        </SidebarItem>
        <SidebarItem 
            onClick={() => navigate('/user/account/bank')} 
            active={location.pathname === "/user/account/bank"}
        >
            Ngân Hàng
        </SidebarItem>
        <SidebarItem 
            onClick={() => navigate('/user/account/address')} 
            active={location.pathname === "/user/account/address"}
        >
            Địa Chỉ
        </SidebarItem>
        <SidebarItem 
            onClick={() => navigate('/user/account/password')} 
            active={location.pathname === "/user/account/password"}
        >
            Đổi Mật Khẩu
        </SidebarItem>
        <SidebarItem 
            onClick={() => navigate('/user/account/purchase')} 
            active={location.pathname === "/user/account/purchase"}
        >
            Đơn mua
        </SidebarItem>
    </Sidebar>
    <Outlet/>
</ProfileContainer>)
}

export default ProfileLayOut
