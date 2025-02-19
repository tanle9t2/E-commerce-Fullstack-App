import { useMutation } from "@tanstack/react-query";
import { changePassword as changePasswordAPI } from "../../services/apiAuthentication";
import { useAuthContext } from "../../context/AuthContext";
import toast from "react-hot-toast";
export function useChangePassword() {
    const { token } = useAuthContext()
    const { isLoading, mutate: changePassword } = useMutation({
        mutationFn: ({ oldPassword, newPassword, confirmPassword }) => changePasswordAPI({ oldPassword, newPassword, confirmPassword, token }),
        onSuccess: () => {
            toast.success("Change successfull passowrd")
        },
        onError: (error) => toast.error(error.message)
    })

    return { isLoading, changePassword };
}