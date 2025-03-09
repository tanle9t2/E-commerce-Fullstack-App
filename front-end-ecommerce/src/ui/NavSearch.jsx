import styled from "styled-components";
import Input from "./Input"
import Logo from "./Logo"
import CartNav from '../features/cart/CartNav'
import { HiOutlineSearch } from "react-icons/hi";
import { useNavigate } from "react-router-dom";
import { useAuthContext } from "../context/AuthContext";
import CartNavNoAuth from "../features/cart/CartNavNoAuth";
import { useEffect, useRef, useState } from "react";

import { useSearchHint } from "../features/search/useSearchHint";

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
    const { auth } = useAuthContext();
    const [searchTerm, setSearchTerm] = useState("");
    const [showSuggestions, setShowSuggestions] = useState(false);
    const searchRef = useRef(null);
    const { hint, isLoading, error } = useSearchHint(searchTerm)
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
                    <HiOutlineSearch />
                </StyledFindIcon>
                {!isLoading && showSuggestions && (
                    <SuggestionsList>
                        {Object.entries(hint).map(([key, item]) => (
                            <SuggestionItem onClick={() => hanleOnClickSuggess(key)} key={key}>
                                {item}
                            </SuggestionItem>
                        ))}

                    </SuggestionsList>
                )}
            </SearchBar>
            {auth ? <CartNav /> : <CartNavNoAuth />}
        </StyledNavSearch>
    )
}

export default NavSearch
