import { PiStorefrontLight } from "react-icons/pi"
import styled from "styled-components"
import TruncateText from "../../ui/TruncateText"
import { useCartContext } from "../../context/CartContext"
import { formatCurrencyVND, getDateRange } from "../../utils/helper"

const StyledCheckoutCart = styled.div`
    padding: 20px;
    border-radius: 8px;
    background: #fff;
    box-shadow: 0 1px 1px 0 rgba(0, 0, 0, .05);
`

const Header = styled.div`
    display:flex;
    margin-bottom:20px;
`
const D1 = styled.div`
    flex:4;
    display:flex;
    align-items:center;
    overflow:hidden;
`
const D2 = styled.div`
    flex:2;
  
`
const D3 = styled.div`
    flex:2;
    text-align:end;
`
const D4 = styled.div`
     flex:2;
     text-align:end;
`
const D5 = styled.div`
     flex:2;
     text-align:end;
`
const ListItem = styled.ul`
    width:100%;
    margin: 40px 0;
`
const Item = styled.li`
    margin:20px 0;
    display:flex;
    align-items:center;
`
const Store = styled.p`
    font-size:1.8rem;
    display:flex;
`
const ProductImage = styled.img`
  width: 40px;
  height: 40px;
  margin-right: 15px;
`;
// Styled Components
const SummaryContainer = styled.div`
  background: #fafdff;
  padding: 20px;
  border-radius: 5px;
  margin: 20px 0;
  display:grid;
  grid-template-columns:0.3fr 0.7fr;
  flex-wrap:wrap;
 
`;

const NoteSection = styled.div`
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 15px;
`;

const Input = styled.input`
  flex: 1;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
  color: #333;
  margin-right:20px;

  &:focus {
    outline: none;
    border-color: #007aff;
  }
`;

const ShippingSection = styled.div`
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid #ddd;
`;

const ShippingDetails = styled.div`
  font-size: 14px;
  color: #555;
`;

const ChangeLink = styled.span`
  color: #007aff;
  cursor: pointer;
  font-weight: bold;
  &:hover {
    text-decoration: underline;
  }
`;

const TotalAmount = styled.div`
    margin-top:20px;
  text-align: left;
  font-size: 18px;
  font-weight: bold;
  color: var(--primary-color);
`;



function CheckoutCard({handleOnChange,data}) {
 
    return (
        <StyledCheckoutCart>
            <Header>
                <D1><p>Sản phẩm</p></D1>
                <D2> </D2>
                <D3><p>Đơn giá</p></D3>
                <D4><p>Số lượng</p></D4>
                <D5><p>Thành tiền</p></D5>
            </Header>
            {
                data.map(gr => <>
                    <ListItem key={gr.tenantName}>
                        <Item>
                            <Store className="flex items-center font"> <PiStorefrontLight fontSize={22} className="mr-3" />{gr.tenantName} </Store>
                        </Item>
                        {gr.items.map(item =>
                            <Item key={item.skuId}>
                                <D1>
                                    <ProductImage src={item.image} />
                                    <TruncateText width="300px">{item.name}</TruncateText>
                                </D1>
                                <D2>
                                    <TruncateText width="200px">Loại: {item.modelName}</TruncateText>
                                </D2>
                                <D3>
                                    <p>{formatCurrencyVND(item.sellPrice)}</p>
                                </D3>
                                <D4>
                                    <p>{item.quantity}</p>
                                </D4>
                                <D5>
                                    <p>{formatCurrencyVND(item.sellPrice * item.quantity)}</p>
                                </D5>
                            </Item>
                        )}
                    </ListItem>
                    <SummaryContainer>
                        {/* Note Section */}
                        <NoteSection>
                            <label>Lời nhắn:</label>
                            <Input onChange={(e) => handleOnChange(gr.tenantName,e.target.value)} type="text" placeholder="Lưu ý cho Người bán..." />
                        </NoteSection>

                        {/* Shipping Section */}
                        <ShippingSection>
                            <ShippingDetails>
                                Phương thức vận chuyển: <strong>Nhanh</strong>
                                <br />
                                Đảm bảo nhận hàng từ {getDateRange()}
                                <br />
                            </ShippingDetails>
                            <div>₫63.700</div>
                        </ShippingSection>

                        {/* Total Amount */}
                        <TotalAmount>
                            Tổng số tiền ({gr.items.reduce((acc,{quantity}) => acc +quantity,0)} sản phẩm): 
                            {formatCurrencyVND(gr.items.reduce((acc,{quantity,sellPrice}) => acc +quantity*sellPrice,0) )}
                        </TotalAmount>
                    </SummaryContainer>
                </>
                )
            }

        </StyledCheckoutCart>
    )
}

export default CheckoutCard
