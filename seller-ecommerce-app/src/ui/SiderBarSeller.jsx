import React, { useState } from "react";
import styled from "styled-components";
import { FaChevronDown, FaChevronRight } from "react-icons/fa";
import { Link } from "react-router-dom";

// Styled Components
const SidebarContainer = styled.div`
  width: 250px;
  background: var(--white-color);
  padding: 10px;
  font-family: Arial, sans-serif;
`;

const MenuItem = styled.div`
  padding: 10px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #333;
  border-radius: 5px;
  
  &:hover {
    background: #e9ecef;
  }
`;

const SubMenu = styled.div`
  max-height: ${({ isOpen }) => (isOpen ? "500px" : "0")};
  overflow: hidden;
  transition: max-height 0.3s ease-in-out;
  padding-left: 20px;
`;

const SubMenuItem = styled.div`
  padding: 8px 10px;
  font-size: 14px;
  color: #555;
  cursor: pointer;

  &:hover {
    background: #dee2e6;
    border-radius: 3px;
  }
`;

function SideBarSeller() {
  const [openMenus, setOpenMenus] = useState({
    vanChuyen: true,
    donHang: true,
    sanPham: true,
    marketing: true,
    buyerMan: true,
  });

  const toggleMenu = (menu) => {
    setOpenMenus((prev) => ({ ...prev, [menu]: !prev[menu] }));
  };
  return (
    <SidebarContainer>
      <MenuItem onClick={() => toggleMenu("vanChuyen")} isOpen={openMenus.vanChuyen}>
        Vận chuyển {openMenus.vanChuyen ? <FaChevronDown /> : <FaChevronRight />}
      </MenuItem>
      <SubMenu isOpen={openMenus.vanChuyen}>
        <SubMenuItem>Quản Lý Vận Chuyển</SubMenuItem>
        <SubMenuItem>Giao Hàng Loạt</SubMenuItem>
        <SubMenuItem>Cài Đặt Vận Chuyển</SubMenuItem>
      </SubMenu>

      <MenuItem onClick={() => toggleMenu("donHang")} isOpen={openMenus.donHang}>
        Quản Lý Đơn Hàng {openMenus.donHang ? <FaChevronDown /> : <FaChevronRight />}
      </MenuItem>
      <SubMenu isOpen={openMenus.donHang}>
        <SubMenuItem>Tất Cả</SubMenuItem>
        <SubMenuItem>Đơn Hủy</SubMenuItem>
        <SubMenuItem>Trả Hàng/Hoàn Tiền</SubMenuItem>
      </SubMenu>

      <MenuItem onClick={() => toggleMenu("sanPham")} isOpen={openMenus.sanPham}>
        Quản Lý Sản Phẩm {openMenus.sanPham ? <FaChevronDown /> : <FaChevronRight />}
      </MenuItem>
      <SubMenu isOpen={openMenus.sanPham}>
        <Link to="/portal/lists"><SubMenuItem>Tất Cả Sản Phẩm</SubMenuItem></Link>
        <Link to="/portal/new"><SubMenuItem>Thêm Sản Phẩm</SubMenuItem></Link>
        <SubMenuItem>Sản Phẩm Vi Phạm</SubMenuItem>
        <SubMenuItem>Quản Lý Tồn Kho</SubMenuItem>
      </SubMenu>

      <MenuItem onClick={() => toggleMenu("marketing")} isOpen={openMenus.marketing}>
        Kênh Marketing {openMenus.marketing ? <FaChevronDown /> : <FaChevronRight />}
      </MenuItem>
      <SubMenu isOpen={openMenus.marketing}>
        <SubMenuItem>Kênh Marketing</SubMenuItem>
        <SubMenuItem>Quảng Cáo Shopee</SubMenuItem>
      </SubMenu>

      <MenuItem onClick={() => toggleMenu("buyerMan")} isOpen={openMenus.buyerMan}>
        ps_menu_buyer_man {openMenus.buyerMan ? <FaChevronDown /> : <FaChevronRight />}
      </MenuItem>
      <SubMenu isOpen={openMenus.buyerMan}>
        <SubMenuItem>Tin Nhắn Quảng Bá</SubMenuItem>
        <SubMenuItem>ps_menu_buyer_group</SubMenuItem>
        <SubMenuItem>ps_menu_buyer_list</SubMenuItem>
      </SubMenu>
    </SidebarContainer >
  );
};


export default SideBarSeller;
