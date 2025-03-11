import React, { useState } from 'react';
import styled from 'styled-components';
import Button from '../../ui/Button';
import ProductSearch from '../products/ProductSearch';
import { useTenants } from './useTenants';
import Spinner from '../../ui/Spinner';
import { PAGE_SIZE_PRODUCT_TENANT } from '../../utils/constant';
import { calculateDayDifference } from '../../utils/helper';
import { Avatar } from '@mui/material';
import LayoutWithSideBar from '../../ui/LayoutWithSideBar';
import TenantMenuCategory from './TenantMenuCategory';
import { useParams, useSearchParams } from 'react-router-dom';
import { useSearchProduct } from '../search/useSearchProduct';
import { useChatContext } from '../../context/ChatContext';
import { set } from 'react-hook-form';

// Header Section
const Header = styled.header`
  color: var(--black-color);
  padding: 10px 20px;

  display: grid;
  grid-template-columns:0.4fr 0.6fr;
  align-items: center;
`;
const SellerName = styled.span`
  font-weight: bold;
`;

const Stats = styled.div`
  display: grid;
  grid-template-columns:0.5fr 0.5fr;
  margin-left:10px;
`;

const StatItem = styled.span`
  font-size: 14px;
  margin:10px;
`;

const ChatButton = styled(Button)`
  border: 1px solid white;
  color: white;
  padding: 5px 10px;
  border-radius: 5px;
  cursor: pointer;
`;

// Navigation Section
const Nav = styled.nav`
  display: flex;
  justify-content: space-around;
  gap: 20px;
  padding: 10px 0;
  background-color: #f8f8f8;
  border-bottom: 1px solid #ddd;
`;

const NavItem = styled.a`
  text-decoration: none;
  color: #333;
  font-weight: ${props => (props.active ? 'bold' : 'normal')};
  color: ${props => (props.active ? '#ff4500' : '#333')};
`;


const Tenant = () => {
  const { isLoading, tenantInfor, categories } = useTenants();
  const [searchParams, setSearchParams] = useSearchParams();
  const { setIsChatOpen, handleSelectedUser } = useChatContext()
  const [activeCategory, setActiveCateogry] = useState(null)
  const { isLoading: gettingProducts, products, count } = useSearchProduct()
  function handleOnClickCategory(id, lft, rgt) {
    setActiveCateogry({ id, lft, rgt })
    searchParams.set("lft", lft)
    searchParams.set("rgt", rgt)
    searchParams.set("category", id)
    setSearchParams(searchParams)
  }
  if (isLoading) return <Spinner />
  const { name, totalProduct, totalComment, follower, following, createdAt, tenantImage, userId } = tenantInfor;
  const pages = Math.ceil(count / PAGE_SIZE_PRODUCT_TENANT);
  function hanleOnClickChat() {
    setIsChatOpen(true)
    handleSelectedUser(userId, name);
  }
  return (
    <div>
      <Header>
        <div>
          <div className='flex'>
            <Avatar src={tenantImage} alt={name} />
            <div className='ml-5'>
              <SellerName>{name}</SellerName>
              <p>Online 4 phút trước</p>
            </div>
          </div>
          <div className='mt-5 flex justify-between'>
            <ChatButton onClick={() => hanleOnClickChat()}>Chat</ChatButton>
            <ChatButton>+ Theo dõi</ChatButton>
          </div>
        </div>
        <Stats>
          <StatItem>Sản Phẩm: {totalProduct}</StatItem>
          <StatItem>Đang Theo: {following}</StatItem>
          <StatItem>Người theo dõi:{follower}</StatItem>
          <StatItem>Đánh Giá: 4.9 ({totalComment} Đánh Giá)</StatItem>
          <StatItem>Tham Gia: {calculateDayDifference(createdAt)} Trước</StatItem>
        </Stats>
        <div className='flex justify-between'>
        </div>
      </Header>
      <Nav>
        <NavItem active={activeCategory === null}>Dạo</NavItem>
        <NavItem active={activeCategory && activeCategory.id === 0} onClick={() => handleOnClickCategory(0)}>TẤT CẢ SẢN PHẨM</NavItem>
        {categories.slice(0, 5).map(({ parent }) =>
          <NavItem active={activeCategory && activeCategory.id === parent.id} onClick={() => handleOnClickCategory(parent.id, parent.left, parent.right)} key={parent.id}>{parent.name.toUpperCase()}</NavItem>
        )}
        <NavItem>BEST SELLER</NavItem>
      </Nav>
      <LayoutWithSideBar>
        <TenantMenuCategory activeCategory={activeCategory} handleOnClickCategory={handleOnClickCategory} categories={categories} />
        {
          (gettingProducts) ? <Spinner /> : <ProductSearch columns={5} products={products} totalPages={pages} />
        }
      </LayoutWithSideBar>
    </div>
  );
};

export default Tenant;