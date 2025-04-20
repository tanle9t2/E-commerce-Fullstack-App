
import ProductItem from "./ProductItem"
import Section from "../../ui/Section"
import React, { useState } from "react";
import styled from "styled-components";

import { useSearchParams } from "react-router-dom";
import Pagination from "../../ui/Pagination";
import Empty from "../../ui/Empty"

const SortingContainer = styled.div`
  display: flex;
  align-items: center;
  background: #f5f5f5;
  padding: 10px;
  border-radius: 5px;
  width:100%;
  justify-content:space-between;
  margin-bottom:-35px;
  margin-top:25px;
`;

const Label = styled.span`
  font-size: 14px;
  margin-right: 10px;
`;

const Button = styled.button`
  background: ${(props) => (props.active ? "var(--primary-color)" : "var(--color-white)")};
  color: ${(props) => (props.active ? "white" : "black")};
  border: 1px solid #ddd;
  padding: 8px 12px;
  margin-right: 5px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  font-weight: bold;

  &:hover {
    background: ${(props) => (props.active ? "#c0392b" : "#f0f0f0")};
  }
`;

const Dropdown = styled.select`
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 14px;
  color: ${(props) => (props.isActive ? "var(--primary-color)" : "var(--black-color)")};
  cursor: pointer;
`;
const PaginationContainer = styled.div`
  display: flex;
  align-items: center;
  background: #f5f5f5;
  padding: 8px;
  border-radius: 5px;
`;

const PageNumber = styled.span`
  font-size: 14px;
  font-weight: bold;
  margin-right: 10px;
  color: #e74c3c;
`;
function ProductSearch({ columns, totalPages, products }) {
  const [searchParams, setSearchParams] = useSearchParams();
  const [activeSort, setActiveSort] = useState(searchParams.get("sortBy") || "relevant-desc");
  const currentPage = searchParams.get("page") ? parseInt(searchParams.get("page")) : 1;

  if (!products.length) return < Empty />

  function handleNextPage() {
    const next = currentPage === totalPages ? currentPage : currentPage + 1;
    searchParams.set("page", next);
    setSearchParams(searchParams);
  }
  function handlePrevPage() {
    const prev = currentPage <= 1 ? currentPage : currentPage - 1;
    searchParams.set("page", prev);
    setSearchParams(searchParams);
  }
  function onClickSort(value) {
    if (!value) return;
    const arr = value.split("-")
    setActiveSort(value)
    searchParams.set("sortBy", arr[0]);
    searchParams.set("order", arr[1])
    setSearchParams(searchParams);
  }
  return (
    <div>
      <SortingContainer>
        <div>
          <Label>Sắp xếp theo</Label>
          <Button active={activeSort === "relevant-desc" || activeSort === "relevant"} onClick={() => onClickSort("relevant-desc")}>Liên Quan</Button>
          <Button active={activeSort === "newest-desc" || activeSort === "newest"} onClick={() => onClickSort("newest-desc")}>Mới Nhất</Button>
          <Button active={activeSort === "bestseller-desc" || activeSort === "bestseller"} onClick={() => onClickSort("bestseller-desc")}>Bán Chạy</Button>
          <Dropdown value={activeSort} isActive={activeSort.split("-")[0] === "price"} onChange={(e) => {
            onClickSort(e.target.value)
          }}>
            {activeSort.split("-")[0] !== "price" && <option value="">Giá</option>}
            <option value="price-asc">Giá: Thấp đến Cao</option>
            <option value="price-desc">Giá: Cao đến Thấp</option>
          </Dropdown>
        </div>
        {totalPages !== 1 &&
          <PaginationContainer>
            <PageNumber>
              {currentPage}/{totalPages}
            </PageNumber>
            <Button
              disabled={currentPage === 1}
              onClick={() => handlePrevPage()}
            >
              {"<"}
            </Button>
            <Button
              disabled={currentPage === totalPages}
              onClick={() => handleNextPage()}
            >
              {">"}
            </Button>
          </PaginationContainer>
        }
      </SortingContainer>

      <Section columns={columns}>
        {products.map(product => <ProductItem key={product.id} product={product} />)}
      </Section>
      <Pagination pages={totalPages} />
    </div>
  )
}

export default ProductSearch
