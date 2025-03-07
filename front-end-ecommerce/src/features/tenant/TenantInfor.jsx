import styled from 'styled-components';

import Avatar from '../../ui/Avatar';
import Seperator from '../../ui/Seperator';

import Button from '../../ui/Button';
import ButtonGroup from '../../ui/ButtonGroup';
import useTenant from './useTenant';
import { calculateDayDifference } from '../../utils/helper';
import { useNavigate } from 'react-router-dom';

const ShopInfoContainer = styled.div`
  display: grid;
  align-items: center;
  padding: 1rem;
  background-color:var(--color-grey-0);
  grid-template-columns:0.45fr 0.05fr 1fr;
`;

const ShopLogo = styled.div`
  margin-right: 1rem;
  display:flex;
  /* Add styles for logo here */
`;

const ShopDetails = styled.div`
  display: grid;
  grid-template-columns:0.25fr 0.25fr 0.25fr 0.25fr;
  margin-right: 1rem;
`;
const ShopName = styled.div`
  display:flex;
  flex:1;
  flex-direction:column;
  justify-content:space-between;
`
const ShopLabel = styled.label`
    
    flex:0.6;
     font-weight: 500;
`;
const ShopContent = styled.div`
    color:var(--primary-color);
    display:flex;
    flex: 0.4;
`
const ShopDetail = styled.div`
  display:flex;
  margin:10px;
`
function TenantInfor({ tenantId }) {
  const { tenant, isLoading } = useTenant(tenantId);
  const navigate = useNavigate()
  console.log(tenantId)
  if (isLoading) return;
  const { name, tenantImage, totalComment, totalProduct, createdAt, following } = tenant;
  return (
    <ShopInfoContainer>
      <ShopLogo>
        <Avatar height={80} url={tenantImage} />
        <ShopName>
          <span>{name.toUpperCase()}</span>
          <span>Online 28 phút trước</span>
          <ButtonGroup>
            <Button>Chat Ngay</Button>
            <Button onClick={() => navigate(`/shop/${tenantId}`)} variation="second">Xem Shop</Button>
          </ButtonGroup>
        </ShopName>
      </ShopLogo>
      <Seperator />
      <ShopDetails>
        <ShopDetail>
          <ShopLabel>Đánh giá</ShopLabel>
          <ShopContent>{totalComment}</ShopContent>
        </ShopDetail>
        <ShopDetail>
          <ShopLabel>Tham gia</ShopLabel>
          <ShopContent>{calculateDayDifference(createdAt)}</ShopContent>
        </ShopDetail>
        <ShopDetail>
          <ShopLabel>Sản phẩm</ShopLabel>
          <ShopContent>{totalProduct}</ShopContent>
        </ShopDetail>
        <ShopDetail>
          <ShopLabel>Người theo dõi</ShopLabel>
          <ShopContent>{following}</ShopContent>
        </ShopDetail>
      </ShopDetails>

    </ShopInfoContainer>
  );
}

export default TenantInfor
