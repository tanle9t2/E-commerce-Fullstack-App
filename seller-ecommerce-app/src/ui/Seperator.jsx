import React from 'react';
import styled from 'styled-components';

// Define the separator style using styled-components
const SeparatorStyled = styled.span`
  margin: 0 8px;
  color: #888;
`;

const Separator = () => {
  return <SeparatorStyled>|</SeparatorStyled>;
};

export default Separator;