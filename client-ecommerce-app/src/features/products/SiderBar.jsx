import React, { useEffect, useState } from "react";
import styled from "styled-components";
import { translateFilter } from "../../utils/helper"
import { useSearchParams } from "react-router-dom";
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

const FilterItem = ({ name, listItems }) => {
    const [searchParams, setSearchParams] = useSearchParams();
    const selectedFilters = new Set(searchParams.get(name)?.split(",") || []);
    const handleCheckboxChange = (id) => {
        const newSelected = new Set(selectedFilters);

        if (newSelected.has(id)) {
            newSelected.delete(id);
        } else {
            newSelected.add(id);
        }

        // Update URL parameters
        const updatedParams = new URLSearchParams(searchParams);
        if (newSelected.size > 0) {
            updatedParams.set(name, Array.from(newSelected).join(","));
        } else {
            updatedParams.delete(name);
        }
        setSearchParams(updatedParams);
    };
    return (
        <FilterWrapper>
            <FilterTitle>{translateFilter(name)}</FilterTitle>
            <FilterContainer>
                {listItems.map((item) => (
                    <Label key={item.id !== null ? item.id : item.name}>
                        <Checkbox
                            type="checkbox"
                            checked={selectedFilters.has(item.id !== null ? `${item.id}` : item.name)}
                            onChange={() => handleCheckboxChange(item.id !== null ? `${item.id}` : item.name)}
                        />
                        {item.name} ({item.value})
                    </Label>
                ))}
            </FilterContainer>
        </FilterWrapper>
    );
};
function Sidebar({ filters }) {
    console.log(filters)
    const [minPrice, setMinPrice] = useState("");
    const [maxPrice, setMaxPrice] = useState("");
    const [searchParams, setSearchParams] = useSearchParams();
    function handleOnClick() {
        searchParams.set("minPrice", minPrice)
        searchParams.set("maxPrice", maxPrice)
        setSearchParams(searchParams);
    }
    return (
        <SidebarContainer>
            <Header>Bộ lọc tìm kiếm</Header>
            {filters.map(filter => <FilterItem name={filter.filterName} listItems={filter.filterItems} />)}
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
                    <ApplyButton onClick={() => handleOnClick()} >Áp Dụng</ApplyButton>
                </FilterContainer>
            </FilterWrapper>
        </SidebarContainer>
    );
};

export default Sidebar;
