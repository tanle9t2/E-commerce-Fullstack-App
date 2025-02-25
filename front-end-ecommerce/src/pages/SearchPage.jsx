import styled from "styled-components"
import ProductDisplay from "../features/products/ProductDisplay"
import SiderBar from "../features/products/SiderBar"
import ProductSearch from "../features/products/ProductSearch"
const StyledSearchPage = styled.div`
    display:grid;
    grid-template-columns:0.2fr 0.8fr;
`
function SearchPage() {
    return (
        <StyledSearchPage>
            <SiderBar/>
            <ProductSearch columns={5}/>
        </StyledSearchPage>
    )
}

export default SearchPage
