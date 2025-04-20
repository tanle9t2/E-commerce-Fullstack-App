import styled from "styled-components";
import Button from "../../ui/Button";
import Menus from "../../ui/Menus";
import Modal from "../../ui/Modal";
import SelectionAddressForm from "./SelectionAddressForm";
import { useDeleteAddress } from "./useDeleteAddress";
import Spinner from "../../ui/Spinner";
const StyledAddressCard = styled.div`
  border-bottom: 1px solid #ddd;
  padding: 15px 0;
  display:flex;
  justify-content:space-between;
  font-size:1.6rem;
`;

const Name = styled.span`
  font-weight: bold;
  margin-bottom: 5px;
`;

const Phone = styled.span`
  color: gray;
 
  margin-left:10px;
`;
const Tag = styled.span`
  display: inline-block;
  background: ${(props) => (props.default ? "red" : "#ddd")};
  color: white;
  padding: 3px 8px;
  font-size: 12px;
  border-radius: 3px;
  margin-top: 5px;
`;
const DeleteButton = styled.span`
    color:var(--blue-color);
    cursor: pointer;
    margin:0 5px;
`
const AddressDetail = styled.div`
  margin-top: 5px;
  
`;
function AddressCard({ address }) {
  const {isLoading,deleteAddress} = useDeleteAddress()
  if(isLoading) return <Spinner/>
  return (
    <StyledAddressCard >
      <div>
        <Name>{address.firstName} {address.lastName}</Name>
        <Phone>{address.phoneNumber}</Phone>
        <AddressDetail>{address.streetNumber}</AddressDetail>
        <AddressDetail>{`${address.ward}, ${address.district}, ${address.city}`}</AddressDetail>
      </div>
    
      <div>
       <Modal>
        <Modal.OpenButton opens={"update-address"}>
            <DeleteButton>Cập nhật</DeleteButton>
          </Modal.OpenButton>
          <Modal.Window name={"update-address"}>
            <SelectionAddressForm heading="Cập nhật địa chỉ"
            addressId={address.id}
             fullName={`${address.firstName} ${address.lastName}`} 
             phone={address.phoneNumber} 
             city={address.city} 
             district={address.district}
             ward={address.ward}
             detail={address.streetNumber}/>
          </Modal.Window>
       </Modal>
        <DeleteButton onClick={() => deleteAddress({addressId:address.id})}>Xóa</DeleteButton>

      </div>
    </StyledAddressCard>
  )
}

export default AddressCard
