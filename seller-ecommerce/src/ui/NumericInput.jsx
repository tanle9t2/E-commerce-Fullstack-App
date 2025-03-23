import React, { useState } from 'react';
import styled from 'styled-components';
// Styled Components
const Container = styled.div`
  display: flex;
  align-items: center;
  gap: 8px;
`;

const Button = styled.button`
  width: 32px;
  height: 32px;
  font-size: 20px;
  text-align: center;
  cursor: pointer;
  border: 1px solid #ccc;
  border-radius: 4px;
  background: #f9f9f9;
  color: #333;
  transition: background 0.2s ease;

  &:hover {
    background: #e0e0e0;
  }

  &:active {
    background: #ccc;
  }
`;

const Input = styled.input`
  width: 50px;
  height: 32px;
  text-align: center;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;

  &:focus {
    outline: none;
    border-color: #007bff;
  }
`;
function NumericInput({value,setValue,children,active, maxValue}) {
  const handleIncrement = () => {
    if(value + 1> maxValue) return;
    setValue((prevValue) => prevValue + 1);
  };

  const handleDecrement = () => {
    if(value-1 <=0 ) return;
    setValue((prevValue) => Math.max(0, prevValue - 1)); // Prevents negative values
  };

  const handleChange = (e) => {
    const newValue = parseInt(e.target.value, 10);
    if (!isNaN(newValue)) {
      setValue(newValue);
    } else {
      setValue(0);
    }
  };

  return (
    <Container>
      <Button disabled ={active} onClick={handleDecrement}>-</Button>
      <Input
        disabled = {active}
        value={value}
        onChange={handleChange}
      />
      <Button disabled ={active} onClick={handleIncrement}>+</Button>
      {children}
    </Container>
  );
}


export default NumericInput;