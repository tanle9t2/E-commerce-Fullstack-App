import React from "react";
import styled from "styled-components";
import Heading from "../../ui/Heading";
import { useAddress } from "./useAddress";
import Spinner from "../../ui/Spinner";
import AddressCard from "./AddressCard";
import Modal from "../../ui/Modal";
import SelectionAddressForm from "./SelectionAddressForm";

const Container = styled.div`
  padding: 20px;
  width:100%;
  background-color:var(--color-white);
  font-family: Arial, sans-serif;
`;



const Button = styled.button`
  background: var(--primary-color);
  border: none;
  color:var(--color-white);
  padding: 10px 15px;
  cursor: pointer;
  margin-right: 5px;
`;
const AddressHeader = styled.div`
    display:flex;
    align-items:center;
    justify-content:space-between;
`

function Address() {
    const {isLoading, addresses} = useAddress();
    if(isLoading) return <Spinner/>
    return (
        <Container>
            <AddressHeader>
                <Heading as="h1">Địa chỉ của tôi</Heading>
                <Modal>
                    <Modal.OpenButton opens={"create"}>
                    <Button>+ Thêm địa chỉ mới</Button>
                    </Modal.OpenButton>
                    <Modal.Window name={"create"}>
                      <SelectionAddressForm heading={"Thêm địa chỉ mới"}/>
                    </Modal.Window>
                </Modal>
            </AddressHeader>
          {addresses.map((addr) => 
            <AddressCard key={addr.id} address={addr}/>
          )}
        </Container>
      );
}

export default Address

