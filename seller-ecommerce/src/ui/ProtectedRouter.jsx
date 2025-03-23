import { useNavigate } from "react-router";
import { useUser } from "../features/authentication/useUser";
import { useEffect } from "react";

import styled from "styled-components";
import Spinner from "../ui/Spinner";
import { useAuthContext } from "../context/AuthContext";
const FullPage = styled.div`
  height: 100vh;
  display: flex;
  justify-content: center;
  background-color: var(--color-grey-50);
  align-items: center;
`;

function ProtectedRouter({ children }) {
  const { auth } = useAuthContext();
  const navigate = useNavigate();
  useEffect(() => {
    if (!auth) {
      navigate('/login', { replace: true });
    }
  }, [auth, navigate]); // Add dependencies to prevent unnecessary re-renders

  return auth ? children : null;
}

export default ProtectedRouter;
