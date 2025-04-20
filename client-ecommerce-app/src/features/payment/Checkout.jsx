import { useEffect, useState } from "react";
import styled from "styled-components";
import CheckoutCard from "./CheckoutCard";
import { usePaymentMethods } from "./usePaymentMethods";
import { useAddress } from "../authentication/useAddress";
import Spinner from "../../ui/Spinner";
import Highlight from "../../ui/Highlight";
import Button from "../../ui/Button"
import Modal from "../../ui/Modal"
import { useCartContext } from "../../context/CartContext";
import { formatCurrencyVND } from "../../utils/helper";
import ButtonGroup from "../../ui/ButtonGroup";
import { useCreateOrder } from "../orders/useCreateOrder";
import { data } from "autoprefixer";
import useDeleteCartItem from "../cart/useDeleteCartItem";

const StyledCheckout = styled.div`
  font-family: Arial, sans-serif;
  padding: var(--padding-container);
  background-color: var(--color-grey-50);
`;

const AddressSection = styled.div`
  background: var(--color-white);
  display: flex;
  justify-content:space-between;
  align-items:center;
  padding: 15px;
  border-radius: 5px;
  margin-bottom: 20px;
  font-size:1.8rem;
  box-shadow: 0 1px 1px 0 rgba(0, 0, 0, .05);
`;


const PaymentSection = styled.div`
  background: #ffffff;
  padding: 15px;
  border-radius: 5px;
`;

const PaymentMethod = styled.div`
  display: flex;
  gap: 15px;
  margin-top: 10px;
`;

const PaymentButton = styled.button`
  padding: 10px;
  border: 1px solid ${(props) => (props.selected ? "#ee4d2d" : "#ddd")};
  background: ${(props) => (props.selected ? "#fee2e2" : "#fff")};
  cursor: pointer;
  &:hover {
    border-color: #ee4d2d;
  }
`;

const OrderSummary = styled.div`
  background: #ffffff;
  padding: 15px;
  border-radius: 5px;
  margin-top: 20px;
`;

const OrderTotal = styled.h3`
  display: flex;
  justify-content: end;
  font-size:1.8rem;
  flex-direction:column;
`;
const Label = styled.span`
  flex:1;
`
const Price = styled.span`
  flex:0.5;
  text-align:end;
`
const ButtonChange = styled.p`
  color: var(--blue-color);
  cursor: pointer;
`
const CustomHighlight = styled(Highlight)`
  font-size:3rem;
`
const AddressItem = styled.div`
  padding: 20px 10px;
  border: 1px solid ${props => (props.selected ? "#ff4d4f" : "#ddd")};
  border-radius: 5px;
  margin: 20px 0px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  cursor: pointer;
  background: ${props => (props.selected ? "#fff5f5" : "#fff")};
`;

const RadioButton = styled.input`
  margin-right: 10px;
`;

const AddressDetails = styled.div`
  flex: 1;
`;

const AddresModal = ({ addresses, onCloseModal, selectedAddress, setSelectedAddress }) => {
  const [currentSelected, setCurrentSelected] = useState(selectedAddress);
  function handleOnClick() {
    setSelectedAddress(currentSelected)
    onCloseModal();
  }
  return (<>
    <Highlight>Địa Chỉ Của Tôi</Highlight>
    {addresses.map(({ id, firstName, lastName, phoneNumber, streetNumber, ward, city }, index) => (
      <AddressItem key={id} selected={currentSelected === index} onClick={() => setCurrentSelected(index)}>
        <RadioButton type="radio" checked={currentSelected === index} readOnly />
        <AddressDetails>
          <strong>{firstName} {lastName}</strong> ({phoneNumber})
          <br />
          {streetNumber}, {ward}, {city}
        </AddressDetails>
      </AddressItem>
    ))}
    <ButtonGroup>
      <Button onClick={onCloseModal} variation="second" className="large">Hủy</Button>
      <Button onClick={() => handleOnClick()} className="large">Xác nhận</Button>
    </ButtonGroup>
  </>)
}

function Checkout() {
  const { cartItemTick, handleRemoveCartItemTick } = useCartContext();
  const [order, setOrder] = useState(null)
  const [selectedAddress, setSelectedAddress] = useState(0);
  const [selectedPayment, setSelectedPayment] = useState(1);
  const { isLoading, methods } = usePaymentMethods();
  const { isLoading: loadingAddress, addresses } = useAddress();
  const { getTotalPrice } = useCartContext();
  const { deleteCartItem } = useDeleteCartItem();
  const { isLoading: isCreating, createOrder } = useCreateOrder();
  useEffect(() => {
    const grouped = cartItemTick.reduce((acc, item) => {
      if (!acc[item.tenantName]) {
        acc[item.tenantName] = {
          tenantName: item.tenantName,
          items: [{ skuId: item.skuId, quantity: item.quantity }],
          note: null,
        };
      } else {
        acc[item.tenantName].items.push({ skuId: item.skuId, quantity: item.quantity });
      }
      return acc;
    }, {});

    setOrder(Object.values(grouped));
  }, [cartItemTick, setOrder]); // ✅ Runs when `cartItemTick` changes
  if (loadingAddress || isCreating) return <Spinner />
  const address = addresses[selectedAddress];
  const totalPrice = getTotalPrice();
  const groupedByTenant = cartItemTick.reduce((acc, item) => {
    if (!acc[item.tenantName]) {
      // Create a new group with an items array
      acc[item.tenantName] = {
        tenantName: item.tenantName,
        items: [item]  // Initialize with the first item
      };
    } else {
      // Add the item to the existing group
      acc[item.tenantName].items.push(item);
    }
    return acc;
  }, {});
  function handleOnChangeNote(tenantName, value) {

    setOrder(or => or.map(o => o.tenantName === tenantName ? { ...o, "note": value } : o))
  }
  function handleOnClickOrder() {
    const cartItems = order.flatMap(or => or.items.flatMap(item => item.skuId))
    const data = order.map(or => ({
      "items": or.items,
      "addressId": addresses[selectedAddress].id,
      "paymentMethodId": selectedPayment,
      "note": or.note
    }))
    createOrder(data, {
      onSuccess: () => {
        data.map((item) => {
          console.log(item)
          return item.items.skuId
        })
        handleRemoveCartItemTick(cartItems)
        deleteCartItem({ cartId: 1, cartItems })
      }
    })
  }

  return (
    <StyledCheckout>
      <AddressSection>
        <div>
          <Highlight>Địa Chỉ Nhận Hàng</Highlight>
          <p><strong>{address.firstName} {address.lastName}</strong> {address.phoneNumber}</p>
          <p>{address.streetNumber}, {address.ward}, {address.city}</p>
        </div>
        <Modal>
          <Modal.OpenButton opens={"changeAddress"}>
            <ButtonChange>Thay đổi</ButtonChange>
          </Modal.OpenButton>
          <Modal.Window name={"changeAddress"} >
            <AddresModal addresses={addresses} selectedAddress={selectedAddress} setSelectedAddress={setSelectedAddress} />
          </Modal.Window>
        </Modal>
      </AddressSection>

      <CheckoutCard handleOnChange={handleOnChangeNote} data={Object.values(groupedByTenant)} />

      <PaymentSection>
        <h4>Phương thức thanh toán</h4>
        <PaymentMethod>
          {!isLoading && methods.map((method) => (
            <PaymentButton
              key={method.id}
              selected={method.id === selectedPayment}
              onClick={() => setSelectedPayment(method.id)}
            >
              {method.paymentName}
            </PaymentButton>
          ))}
        </PaymentMethod>
      </PaymentSection>

      {/* Order Summary */}
      <OrderSummary>
        <OrderTotal>
          <div className="flex">
            <Label>Tổng tiền hàng:</Label>
            <Price>{formatCurrencyVND(totalPrice)}</Price>
          </div>
          <div className="flex">
            <Label>Tổng tiền phí vận chuyển:</Label>
            <Price>{formatCurrencyVND(30000)}</Price>
          </div>
          <div className="flex">
            <Label>Tổng thanh toán:</Label>
            <Price><CustomHighlight>{formatCurrencyVND(totalPrice + 30000)}</CustomHighlight></Price>
          </div>
        </OrderTotal>
        <div className="flex justify-between mt-5 items-center">
          <p>Nhấn "Đặt hàng" đồng nghĩa với việc bạn đồng ý tuân theo Điều khoản Shopee</p>
          <Button onClick={() => handleOnClickOrder()} size="large">Đặt hàng</Button>
        </div>
      </OrderSummary>
    </StyledCheckout>
  )
}

export default Checkout
