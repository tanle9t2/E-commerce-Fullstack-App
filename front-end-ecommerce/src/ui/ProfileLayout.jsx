import { Outlet } from "react-router-dom";
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
    return (
     <ProfileContainer>
        <Sidebar>
            <SidebarItem active>Hồ Sơ</SidebarItem>
            <SidebarItem>Ngân Hàng</SidebarItem>
            <SidebarItem>Địa Chỉ</SidebarItem>
            <SidebarItem>Đổi Mật Khẩu</SidebarItem>
        </Sidebar>
        <Outlet/>
    </ProfileContainer>
    )
}

export default ProfileLayOut
