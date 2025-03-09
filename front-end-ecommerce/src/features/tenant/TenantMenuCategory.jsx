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
  color: ${props => (props.active ? '#ff4500' : '#333')};
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
    color: ${props => (props.active ? '#ff4500' : '#333')};
  }

  color: ${props => (props.active ? '#ff4500' : '#333')};
`;

// Main Component
function TenantMenuCategory({ categories, activeCategory, handleOnClickCategory }) {

  return (
    <Sidebar>
      <CategoryList>
        <CategoryItem>Sản Phẩm</CategoryItem>
        <CategoryItem>NEW ARRIVAL</CategoryItem>
        <CategoryItem className="best-seller">BEST SELLER</CategoryItem>
        {categories.slice(0, 5).map(({ parent, subCategory }) => (
          <>
            <CategoryItem active={activeCategory && activeCategory.id === parent.id}
              onClick={() => handleOnClickCategory(parent.id, parent.left, parent.right)} key={parent.id}>{parent.name.toUpperCase()}</CategoryItem>
            <SubcategoryList>
              {
                subCategory.map(item => <SubcategoryItem
                  active={activeCategory && activeCategory.id === item.id}
                  onClick={() => handleOnClickCategory(item.id, item.left, item.right)}
                  key={item.id}>{item.name.toUpperCase()}</SubcategoryItem>)
              }
            </SubcategoryList>
          </>
        )
        )}

      </CategoryList>
    </Sidebar>
  );
};

export default TenantMenuCategory;