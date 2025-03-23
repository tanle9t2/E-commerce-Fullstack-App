import styled from "styled-components";
import { Outlet } from "react-router-dom";

import SideBarSeller from "./SiderBarSeller";

const StyledAppLayout = styled.div`
  height: 100vh;
  position:relative;
  display:grid;
  grid-template-columns:0.3fr 6fr;
`;
const Main = styled.main`
  background-color: var(--color-grey-50);
  padding:var(--padding-container);
`;
function AppLayout() {

  return (
    <StyledAppLayout>
      {/* <Header /> */}
      <SideBarSeller />
      <Main>
        <Outlet />
      </Main>

    </StyledAppLayout>
  );
}

export default AppLayout;
