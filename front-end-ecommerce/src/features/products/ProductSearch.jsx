
import ProductItem from "./ProductItem"
import { useProducts } from "./useProducts"
import Spinner from "../../ui/Spinner"
import Section from "../../ui/Section"
import React, { useState } from "react";
import styled from "styled-components";
import { PAGE_SIZE_PRODUCT } from "../../utils/constant";
import { useSearchProduct } from "./useSearchProduct";

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
  background: ${(props) => (props.active ? "#e74c3c" : "#fff")};
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

const PaginationButton = styled.button`
  background: ${(props) => (props.disabled ? "#fff" : "#f0f0f0")};
  border: 1px solid #ddd;
  padding: 6px 12px;
  margin-right: 5px;
  border-radius: 5px;
  cursor: ${(props) => (props.disabled ? "not-allowed" : "pointer")};
  color: ${(props) => (props.disabled ? "#ccc" : "#333")};

  &:hover {
    background: ${(props) => (props.disabled ? "#fff" : "#ddd")};
  }
`;
function ProductSearch({ columns }) {
    const { response, isLoading } = useSearchProduct();
    const [activeSort, setActiveSort] = useState("relevant");
    const [priceSort, setPriceSort] = useState("lowToHigh");
    if (isLoading) return <Spinner />
    const {data:products,page:currentPage,totalElement} = response;
    const totalPages = Math.ceil(totalElement/PAGE_SIZE_PRODUCT)
    console.log(products)
    return (
        <div>
            <SortingContainer>
                <div>
                    <Label>Sắp xếp theo</Label>
                    <Button active={activeSort === "relevant"} onClick={() => setActiveSort("relevant")}>Liên Quan</Button>
                    <Button active={activeSort === "newest"} onClick={() => setActiveSort("newest")}>Mới Nhất</Button>
                    <Button active={activeSort === "bestseller"} onClick={() => setActiveSort("bestseller")}>Bán Chạy</Button>
                    <Dropdown onChange={(e) => setPriceSort(e.target.value)} value={priceSort}>
                        <option value="lowToHigh">Giá: Thấp đến Cao</option>
                        <option value="highToLow">Giá: Cao đến Thấp</option>
                    </Dropdown>
                </div>
                <PaginationContainer>
                    <PageNumber>
                        {currentPage}/{totalPages}
                    </PageNumber>
                    <Button
                        disabled={currentPage === 1}
                        // onClick={() => onPageChange(currentPage - 1)}
                    >
                        {"<"}
                    </Button>
                    <Button
                        disabled={currentPage === totalPages}
                        // onClick={() => onPageChange(currentPage + 1)}
                    >
                        {">"}
                    </Button>
                </PaginationContainer>
            </SortingContainer>

            <Section columns={columns}>
                {products.map(product => <ProductItem key={product.id} product={product} />)}
            </Section>
        </div>
    )
}

export default ProductSearch
