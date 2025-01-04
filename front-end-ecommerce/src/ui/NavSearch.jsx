import styled from "styled-components";
import Input from "./Input"
import Logo from "./Logo"
import { HiOutlineSearch, HiOutlineShoppingCart } from "react-icons/hi";

const SearchBar =styled.div`
    width:100%;
    color:#000;
    position:relative;
`
const StyledFindIcon = styled.span`
    position:absolute;
    padding:8px 15px;
    top: 5px;
    right: 5px;
    border-radius:5px;
    color:var(--color-white);
    background-color:var(--primary-color);
`

function NavSearch() {
    return (
       <div className="flex items-center">
       <Logo/>
        <SearchBar>
            <Input/>
            <StyledFindIcon>
                <HiOutlineSearch />
            </StyledFindIcon>
        </SearchBar>
        <div className="text-2xl ml-11">
            <HiOutlineShoppingCart fontSize={24} />
        </div>
       </div>
    )
}

export default NavSearch
