import styled from "styled-components";
import List from "./List";
import ListItem from "./ListItem";
import User from "../features/authentication/User";
import NavSearch from "./NavSearch";
import HeaderMenu from "./HeaderMenu";

const StyledHeader = styled.header`
  background-color: var(--primary-color);
  color: var(--color-white);
  padding:var(--padding-container);
`;

function Header() {
  return (
    <StyledHeader>
      <HeaderMenu/>
      <NavSearch/>
    </StyledHeader>
  );
}

export default Header;
