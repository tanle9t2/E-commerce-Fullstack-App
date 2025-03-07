import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { logoutUser } from "../../services/apiAuthentication";
import toast from "react-hot-toast";
import { useAuthContext } from "../../context/AuthContext";
import { setAuth } from "../../utils/helper";
export function useLogout() {
  const { handleLogout } = useAuthContext();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const { mutate: logout, isLoading } = useMutation({
    mutationFn: logoutUser,
    onSuccess: () => {
      handleLogout()
      setAuth({})
      queryClient.removeQueries();
      navigate("/", { replace: true });
    },
    onError: (error) => toast.error(error.message),
  });

  return { logout, isLoading };
}