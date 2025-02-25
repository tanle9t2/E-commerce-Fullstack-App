
import ProductItem from "./ProductItem"
import { useProducts } from "./useProducts"
import Spinner from "../../ui/Spinner"
import Section from "../../ui/Section"
import React, { useState } from "react";
import styled from "styled-components";
import { PAGE_SIZE_PRODUCT } from "../../utils/constant";

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
function ProductDisplay({ columns }) {
    const { products, isLoading } = useProducts();
    if (isLoading) return <Spinner />
    const {data} = products;
  
    return (
            <Section columns={columns}>
                {data.map(product => <ProductItem key={product.id} product={product} />)}
            </Section>

    )
}

export default ProductDisplay
