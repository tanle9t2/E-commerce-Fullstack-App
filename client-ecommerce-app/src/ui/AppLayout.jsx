import styled from "styled-components";
import Header from "./Header";
import { Outlet } from "react-router-dom";
import Chat from "../features/chat/Chat";
import { useAuthContext } from "../context/AuthContext";

const StyledAppLayout = styled.div`
  height: 100vh;
  position:relative;
`;
const Main = styled.main`
  background-color: var(--color-grey-50);
  padding: var(--padding-container);
`;
function AppLayout() {
  const { auth } = useAuthContext()
  return (
    <StyledAppLayout>
      <Header />
      <Main>
        <Outlet />
        {auth && <Chat />}
      </Main>

    </StyledAppLayout>
  );
}

export default AppLayout;
