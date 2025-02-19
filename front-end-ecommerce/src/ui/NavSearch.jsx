import styled from "styled-components";
import Input from "./Input"
import Logo from "./Logo"
import CartNav from '../features/cart/CartNav'
import { HiOutlineSearch, HiOutlineShoppingCart } from "react-icons/hi";
import { useNavigate } from "react-router-dom";
import { useAuthContext } from "../context/AuthContext";
import CartNavNoAuth from "../features/cart/CartNavNoAuth";

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
    const {isAuthenticated} = useAuthContext();
    return (
       <div className="flex items-center">
       <Logo/>
        <SearchBar>
            <Input/>
            <StyledFindIcon>
                <HiOutlineSearch />
            </StyledFindIcon>
        </SearchBar>
        {isAuthenticated() ? <CartNav/>:<CartNavNoAuth/>}
       </div>
    )
}

export default NavSearch
