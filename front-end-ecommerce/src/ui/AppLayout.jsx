import styled from "styled-components";
import Header from "./Header";
import { Outlet } from "react-router-dom";

const StyledAppLayout = styled.div`

  height: 100vh;
`;
const Main = styled.main`
  background-color: var(--color-grey-50);
  padding: var(--padding-container);
`;
function AppLayout() {
  return (
    <StyledAppLayout>
      <Header />
      <Main>
        <Outlet />
      </Main>
    </StyledAppLayout>
  );
}

export default AppLayout;
