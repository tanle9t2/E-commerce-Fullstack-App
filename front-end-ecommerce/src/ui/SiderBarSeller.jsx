import styled from "styled-components";

export const SidebarContainer = styled.div`
  width: 250px;
  background: #f8f8f8;
  padding: 20px;
  border-right: 1px solid #ddd;
`;

export const MenuItem = styled.div`
  padding: 10px;
  color: #333;
  cursor: pointer;

  &:hover {
    background: #e0e0e0;
  }
`;
function SiderBarSeller() {
    return (
        <SidebarContainer>
            <MenuItem>Vân chuyển</MenuItem>
            <MenuItem>Quản Lý Vân Chuyển</MenuItem>
            <MenuItem>Quản Lý Đơn Hàng</MenuItem>
            <MenuItem>Cài Đặt Vân Chuyển</MenuItem>
            <MenuItem>Quản Lý Sản Phẩm</MenuItem>
            <MenuItem>Thêm Sản Phẩm</MenuItem>
            <MenuItem>Sản Phẩm VIP</MenuItem>
            <MenuItem>Quản Lý Kho</MenuItem>
            <MenuItem>Ken Marketing</MenuItem>
            <MenuItem>Quản Cá Shopee</MenuItem>
            <MenuItem>ps_menu_buyer_man</MenuItem>
            <MenuItem>ps_menu_buyer_group</MenuItem>
            <MenuItem>ps_menu_buyer_list</MenuItem>
            <MenuItem>Đánh Thư</MenuItem>
            <MenuItem>Số TK Shopee</MenuItem>
            <MenuItem>Fast Escrow</MenuItem>
            <MenuItem>Tải Khoản Năm Hạng</MenuItem>
        </SidebarContainer>
    )
}

export default SiderBarSeller
