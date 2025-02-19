import styled from "styled-components";
import Button from "../../ui/Button";

const StyledAddressCard = styled.div`
  border-bottom: 1px solid #ddd;
  padding: 15px 0;
  display:flex;
  justify-content:space-between;
`;

const Name = styled.span`
  font-weight: bold;
  margin-bottom: 5px;
`;

const Phone = styled.span`
  color: gray;
  font-size: 14px;
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
    color:blue;
    margin:0 5px;
`
const AddressDetail = styled.div`
  margin-top: 5px;
  font-size: 14px;
`;
function AddressCard({address}) {

    return (
        <StyledAddressCard >
        <div>
            <Name>{ address.firstName} { address.lastName}</Name>
            <Phone>{ address.phoneNumber}</Phone>
            <AddressDetail>{ address.streetNumber}</AddressDetail>
            <AddressDetail>{ address.district},{address.city}</AddressDetail>
            <Tag default>Mặc định</Tag>
        </div>

        <div>
            <DeleteButton>Cập nhật</DeleteButton>
            <DeleteButton>Xóa</DeleteButton>
        
        </div>
    </StyledAddressCard>
    )
}

export default AddressCard
