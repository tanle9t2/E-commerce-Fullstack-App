import styled from "styled-components";
import Highlight from "../../ui/Highlight";
import Button from "../../ui/Button";
import OrderItem from "./OrderItem";
import { formatCurrencyVND, translateStatus } from "../../utils/helper";
const OrderCard = styled.div`
  border: 1px solid #ddd;
  padding: 15px;
  border-radius: 8px;
  background: #fff;
`;

const StoreName = styled.a`
  font-size: 16px;
  margin-bottom: 10px;
`;
const OrderFooter = styled.div`
  text-align:end;
  background-color:rgb(255, 254, 251);
  margin-bottom: 10px;
  border-radius: 8px;
  padding: 15px;
`;



const Status = styled.span`
  font-weight: bold;
  color: ${(props) => (props.status === "completed" ? "green" : "red")};
`;

const CustomerHighlight = styled(Highlight)`
    font-size:2.5rem;
`
function OderCard({ order }) {
  const { tenantOrder, status, itemList, totalPrice } = order;

  return (
    <>
      <OrderCard >
        <div className="flex justify-between">
          <StoreName href={`/shop/${tenantOrder.tenantId}`} >{tenantOrder.tenantName}</StoreName>
          <Status status={status}>{translateStatus(status)}</Status>
        </div>
        {
          itemList.map(item => <OrderItem key={item.skuId} item={item} />)
        }
      </OrderCard >
      <OrderFooter>
        <span>Thành tiền: <CustomerHighlight>{formatCurrencyVND(totalPrice)}</CustomerHighlight></span>
        <div className="mt-5 flex justify-end items-center">
          {
            (status === "PROCESSING" || status === "AWAITING_PAYMENT")
              ? <Button className="mr-5" size="large">Hủy đơn hàng</Button>
              : <Button className="mr-5" size="large">Mua lại</Button>
          }
          <Button size="large" variation="secondary">Liên hệ người bán</Button>
        </div>
      </OrderFooter>
    </>
  )
}

export default OderCard
