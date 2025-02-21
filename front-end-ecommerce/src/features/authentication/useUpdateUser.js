import { useMutation, useQueryClient } from "@tanstack/react-query";
import { updateUser as updateUserAPI } from "../../services/apiAuthentication";
import toast from "react-hot-toast";

export function useUpdateUser() {
    const queryClient = useQueryClient();
    const { isLoading, mutate: updateUser } = useMutation({
        mutationFn: ({ userData, image }) => updateUserAPI(userData, image),
        onSuccess: () => {
            toast.success("Cập nhật thành công")
        },
        onError: (error) => toast.error(error.message)
    })

    return { isLoading, updateUser }
}