import React, { useState } from "react";
import styled from "styled-components";

const SidebarContainer = styled.div`
  width: 250px;
  padding: 25px 0;

`;
const Header = styled.p`
    font-size:2.5rem;
    font-weight:700;
`


const FilterTitle = styled.h3`
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
`;

const Label = styled.label`
  display: flex;
  align-items: center;
  cursor: pointer;
`;

const Checkbox = styled.input`
  margin-right: 8px;
  cursor: pointer;
`;
const categories = [
    { id: 1, name: "Áo thun", count: 15 },
    { id: 2, name: "Áo Thun", count: 12 },
    { id: 3, name: "Áo Hoodie", count: 11 },
    { id: 4, name: "Hoodie và Áo nỉ", count: 3 },
];
const location = [
    { id: 1, name: "Áo thun", count: 15 },
    { id: 2, name: "Áo Thun", count: 12 },
    { id: 3, name: "Áo Hoodie", count: 11 },
    { id: 4, name: "Hoodie và Áo nỉ", count: 3 },
]
const FilterContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding-right:30px;
`;
const FilterWrapper = styled.div`
    margin: 10px 0;
    padding: 15px 0;
    border-bottom: 1px solid var(--line-color);
`

const InputContainer = styled.div`
  display: flex;
  gap: 10px;
  align-items: center;
  justify-content:space-between;
`;

const Input = styled.input`
  width: 80px;
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 5px;
  text-align: center;
`;

const ApplyButton = styled.button`
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 8px;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
  text-transform: uppercase;

  &:hover {
    background-color: #c0392b;
  }
`;
function Sidebar() {
    const [selectedFilters, setSelectedFilters] = useState([])
    const [minPrice, setMinPrice] = useState("");
    const [maxPrice, setMaxPrice] = useState("");


    const handleCheckboxChange = (id) => {
        setSelectedFilters((prev) =>
            prev.includes(id) ? prev.filter((item) => item !== id) : [...prev, id]
        );
    };

    return (
        <SidebarContainer>
            <Header>Bộ lọc tìm kiếm</Header>
            <FilterWrapper>
                <FilterTitle>Theo Danh Mục</FilterTitle>
                <FilterContainer>
                    {categories.map((category) => (
                        <Label key={category.id}>
                            <Checkbox
                                type="checkbox"
                                checked={selectedFilters.includes(category.id)}
                                onChange={() => handleCheckboxChange(category.id)}
                            />
                            {category.name} ({category.count})
                        </Label>
                    ))}
                </FilterContainer>
            </FilterWrapper>
            <FilterWrapper>
                <FilterTitle>Nơi bán</FilterTitle>
                <FilterContainer>
                    {location.map((category) => (
                        <Label key={category.id}>
                            <Checkbox
                                type="checkbox"
                                checked={selectedFilters.includes(category.id)}
                                onChange={() => handleCheckboxChange(category.id)}
                            />
                            {category.name} ({category.count})
                        </Label>
                    ))}
                </FilterContainer>
            </FilterWrapper>
           <FilterWrapper>
           <FilterContainer>
                <FilterTitle>Khoảng Giá</FilterTitle>
                <InputContainer>
                    <Input
                        type="number"
                        placeholder="₫ TỪ"
                        value={minPrice}
                        onChange={(e) => setMinPrice(e.target.value)}
                    />
                    —
                    <Input
                        type="number"
                        placeholder="₫ ĐẾN"
                        value={maxPrice}
                        onChange={(e) => setMaxPrice(e.target.value)}
                    />
                </InputContainer>
                <ApplyButton >Áp Dụng</ApplyButton>
            </FilterContainer>
           </FilterWrapper>
        </SidebarContainer>
    );
};

export default Sidebar;
