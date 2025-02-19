import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { logoutUser } from "../../services/apiAuthentication";
import toast from "react-hot-toast";
export function useLogout() {
    const navigate = useNavigate();
    const queryClient = useQueryClient();
    const { mutate: logout, isLoading } = useMutation({
      mutationFn: logoutUser,
      onSuccess: () => {
        queryClient.removeQueries();
        navigate("/", { replace: true });
      },
      onError: (error) => toast.error(error.message),
    });
  
    return { logout, isLoading };
}