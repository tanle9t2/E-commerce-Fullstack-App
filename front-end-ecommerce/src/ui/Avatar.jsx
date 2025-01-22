import styled from "styled-components";

const Avatar = styled.img`
  height:  ${(props) => props.type ==="user" ? `2.5rem` :`10rem`};
  width: auto;
  border-radius:50%;
  ${(props) => props.type ==="tenant" && `border: 1px solid var(--line-color);`}
  margin-right:10px ;
`;
Avatar.defaultProps = {
    type:"user"
  };
export default Avatar;