import styled from 'styled-components';
import Section from '../../ui/Section';
import Logo from '../../ui/Logo';

const ShopInfoContainer = styled.div`
  display: flex;
  align-items: center;
  padding: 1rem;
  border: 1px solid #ccc;
  border-radius: 5px;
`;

const ShopLogo = styled.div`
  margin-right: 1rem;
  /* Add styles for logo here */
`;

const ShopDetails = styled.div`
  display: flex;
  flex-wrap: wrap;
  margin-right: 1rem;
`;

const DetailItem = styled.div`
  margin-right: 1rem;
  margin-bottom: 0.5rem;
`;

const ShopActions = styled.div`
  display: flex;
`;

const ShopActionButton = styled.button`
  margin-left: 0.5rem;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 3px;
  background-color: #007bff;
  color: #fff;
  cursor: pointer;
`;
function TenantInfor() {
    return (
        <Section columns={1} bgcolor={'#fff'}>
          <ShopLogo>
            <Logo/>
            <span>OK</span>
          </ShopLogo>
          <ShopDetails>
            <DetailItem>
              <span>Tham Gia</span>
              <span>feedbackTime</span>
            </DetailItem>
            <DetailItem>
              <span>Sản Phẩm</span>
              <span>product</span>
            </DetailItem>
            <DetailItem>
              <span>Thời Gian Phản Hồi</span>
              <span>feedbackTime</span>
            </DetailItem>
            <DetailItem>
              <span>Người Theo Dõi</span>
              <span>followers</span>
            </DetailItem>
          </ShopDetails>
          <ShopActions>
            <ShopActionButton>Chat Ngay</ShopActionButton>
            <ShopActionButton>Xem Shop</ShopActionButton>
          </ShopActions>
        </Section>
      );
}

export default TenantInfor
