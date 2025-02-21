import styled from "styled-components";
import Highlight from "../../ui/Highlight";
import  Button  from "../../ui/Button";
import OrderItem from "./OrderItem";
import { formatCurrencyVND } from "../../utils/helper";
const OrderCard = styled.div`
  border: 1px solid #ddd;
  padding: 15px;
  border-radius: 8px;
  background: #fff;
`;

const StoreName = styled.h3`
  font-size: 16px;
  margin-bottom: 10px;
`;

const OrderContent = styled.div`
  display: flex;
  align-items: center;
`;
const OrderFooter = styled.div`
  text-align:end;
  background-color:rgb(255, 254, 251);
  margin-bottom: 10px;
  border-radius: 8px;
  padding: 15px;
`;

const ProductImage = styled.img`
  width: 80px;
  height: 80px;
  margin-right: 15px;
`;

const ProductDetails = styled.div`
  flex: 1;
    display:flex;
    align-items:center;
    justify-content:space-between;
`;

const ProductTitle = styled.p`
  font-size: 1.6rem;
  font-weight:500;
`;

const ProductPrice = styled.p`
  color: red;
  font-weight: bold;
`;

const Status = styled.span`
  font-weight: bold;
  color: ${(props) => (props.status === "completed" ? "green" : "red")};
`;

const CustomerHighlight = styled(Highlight)`
    font-size:2.5rem;
`
function OderCard({ order }) {
    const {  tenantOrder, status,itemList,totalPrice } = order;
    return (
        <>
            <OrderCard >
                <div className="flex justify-between">
                    <StoreName>{tenantOrder.tenantName}</StoreName>
                    <Status status={status}>{status === "COMPLETE" ? "HOÀN THÀNH" : "ĐÃ HỦY"}</Status>
                </div>
                {
                    itemList.map(item => <OrderItem key={item.skuId} item={item}/>)
                }
            </OrderCard>
            <OrderFooter>
                <span>Thành tiền: <CustomerHighlight>{formatCurrencyVND(totalPrice)}</CustomerHighlight></span>
                <div className="mt-5 flex justify-end items-center">
                    <Button className="mr-3" size="large">Mua lại</Button>
                    <Button size="large" variation="secondary">Liên hệ người bán</Button>
                </div>
            </OrderFooter>
        </>
    )
}

export default OderCard
