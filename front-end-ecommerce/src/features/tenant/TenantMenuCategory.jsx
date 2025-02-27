import React from 'react';
import styled from 'styled-components';

// Styled Sidebar Container
const Sidebar = styled.div`
  width: 250px;
  margin-top: 25px;
  border-right: 1px solid #ddd;
  padding: 10px 0;
  font-family: Arial, sans-serif;
`;

// Styled Category List
const CategoryList = styled.ul`
  list-style: none;
  padding: 0;
  margin: 0;
`;

// Styled Category Item
const CategoryItem = styled.li`
  padding: 10px 20px;
  color: #333;
  cursor: pointer;

  &:hover {
    background-color: #f5f5f5;
  }
`;

// Styled Subcategory List
const SubcategoryList = styled.ul`
  list-style: none;
  padding-left: 35px;
  margin: 0;
`;

// Styled Subcategory Item
const SubcategoryItem = styled.li`
  padding: 5px 0;
  color: #666;
  cursor: pointer;

  &:hover {
    color: #000;
  }

  &.best-seller {
    color: #ff0000; // Red color for "Best Seller" as in the image
  }
`;

// Main Component
function TenantMenuCategory() {
    return (
        <Sidebar>
            <CategoryList>
                <CategoryItem>Sản Phẩm</CategoryItem>

                <CategoryItem>NEW ARRIVAL</CategoryItem>
                <CategoryItem className="best-seller">BEST SELLER</CategoryItem>

                <CategoryItem>TOPS</CategoryItem>
                <SubcategoryList>
                    <SubcategoryItem>T-SHIRTS</SubcategoryItem>
                    <SubcategoryItem>POLO</SubcategoryItem>
                    <SubcategoryItem>SHIRTS</SubcategoryItem>
                </SubcategoryList>
                <CategoryItem>BOTTOMS</CategoryItem>
                <SubcategoryList>
                    <SubcategoryItem>SHORTPANTS</SubcategoryItem>
                    <SubcategoryItem>PANTS</SubcategoryItem>
                </SubcategoryList>
                <CategoryItem>OUTERWEAR</CategoryItem>
                <SubcategoryList>
                    <SubcategoryItem>HOODIES</SubcategoryItem>
                    <SubcategoryItem>HOODIES ZIPPER</SubcategoryItem>
                    <SubcategoryItem>JACKETS</SubcategoryItem>
                    <SubcategoryItem>SWEATERS</SubcategoryItem>
                    <SubcategoryItem>CARDIGANS</SubcategoryItem>
                    <SubcategoryItem>VARSITYS</SubcategoryItem>
                    <SubcategoryItem>GILE</SubcategoryItem>
                </SubcategoryList>
                <CategoryItem>ACCESSORIES</CategoryItem>
            </CategoryList>
        </Sidebar>
    );
};

export default TenantMenuCategory;