import styled from "styled-components"
import ProductDisplay from "../features/products/ProductDisplay"
import SiderBar from "../features/products/SiderBar"
import ProductSearch from "../features/products/ProductSearch"
import { useSearchProduct } from "../features/products/useSearchProduct"
import Spinner from "../ui/Spinner"
import Empty from "../ui/Empty"
import { useEffect, useMemo, useRef, useState } from "react"
const StyledSearchPage = styled.div`
    display:grid;
    grid-template-columns:0.2fr 0.8fr;
`
function SearchPage() {
    const { products, totalPages, filter, isLoading } = useSearchProduct();
   

    if (isLoading) return <Spinner />
    if (!products.length) return <Empty resource={"Sản phẩm"} />;
   
    return (
        <StyledSearchPage>
            <SiderBar filters={filter} />
            <ProductSearch products={products} totalPages={totalPages} columns={5} />
        </StyledSearchPage>
    )
}

export default SearchPage
