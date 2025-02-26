import styled from "styled-components";
import Input from "./Input"
import Logo from "./Logo"
import CartNav from '../features/cart/CartNav'
import { HiOutlineSearch, HiOutlineShoppingCart } from "react-icons/hi";
import { useNavigate } from "react-router-dom";
import { useAuthContext } from "../context/AuthContext";
import CartNavNoAuth from "../features/cart/CartNavNoAuth";
import { useEffect, useRef, useState } from "react";
import useDebounce from "../hook/useDebounce";
import { useQuery } from "@tanstack/react-query";
import { searchProduct } from "../services/apiProduct";

const SearchBar = styled.div`
    width:100%;
    color:#000;
    position:relative;
    display:flex;
    align-items:center;
`
const StyledFindIcon = styled.span`
    position:absolute;
    padding:8px 15px;
    top: 8px;
    right: 5px;
    border-radius:5px;
    color:var(--color-white);
    background-color:var(--primary-color);
    cursor: pointer;
`
const StyledNavSearch = styled.div`
    display:grid;
    grid-template-columns:0.15fr 0.7fr 0.15fr;
`
const SearchInput = styled.input`
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 16px;
  outline: none;
`;

const SuggestionsList = styled.ul`
  position: absolute;
  width: 100%;
  background: white;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-top: 5px;
  list-style: none;
  padding: 0;
  max-height: 200px;
  overflow-y: auto;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  top:45px;
  z-index:10;
`;

const SuggestionItem = styled.li`
  padding: 10px;
  cursor: pointer;
  &:hover {
    background: #f0f0f0;
  }
`;

function NavSearch() {
    const { isAuthenticated } = useAuthContext();
    const [searchTerm, setSearchTerm] = useState("");
    const debouncedSearch = useDebounce(searchTerm, 500);
    const [showSuggestions, setShowSuggestions] = useState(false);
    const searchRef = useRef(null);
    const { data, isLoading, error } = useQuery({
        queryKey: ["searchNav", debouncedSearch],
        queryFn: () => searchProduct({keyword:debouncedSearch}),
        enabled: !!debouncedSearch,
    });
    const navigate = useNavigate();
    function hanleOnClickSuggess(id) {
        navigate(`/product/${id}`)
        setSearchTerm("")
        setShowSuggestions(false)
    }
    function handleOnClickFind(keyword) {
        navigate(`/search?keyword=${keyword}`)
        setSearchTerm("")
        setShowSuggestions(false)
    }
    function handleEnter(e) {
        if (e.key === "Enter") {
            navigate(`/search?keyword=${searchTerm}`)
            setSearchTerm("")
            setShowSuggestions(false)
        }
    }
    useEffect(() => {
        function handleClickOutside(event) {
            if (searchRef.current && !searchRef.current.contains(event.target)) {
                setShowSuggestions(false);
            }
        }
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);
    return (
        <StyledNavSearch>
            <Logo />
            <SearchBar ref={searchRef}>
                <Input onFocus={() => setShowSuggestions(true)} 
                value={searchTerm} defaultValue={"Nhập tìm kiếm của bạn"} 
                onChange={(e) => setSearchTerm(e.target.value)}
                onKeyDown={(e) => handleEnter(e)}
                />
                <StyledFindIcon onClick={() => handleOnClickFind(searchTerm)}>
                    <HiOutlineSearch/>
                </StyledFindIcon>
                {data&& showSuggestions && ( 
                    <SuggestionsList>
                        {data.data.map((item) => (
                            <SuggestionItem onClick={() =>hanleOnClickSuggess(item.id)} key={item.id}>{item.name}</SuggestionItem>
                        ))}
                    </SuggestionsList>
                )}
            </SearchBar>
            {isAuthenticated() ? <CartNav /> : <CartNavNoAuth />}
        </StyledNavSearch>
    )
}

export default NavSearch
