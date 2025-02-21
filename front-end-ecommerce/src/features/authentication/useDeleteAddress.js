import { useMutation, useQueryClient } from "@tanstack/react-query"
import { useAuthContext } from "../../context/AuthContext"
import { deleteAddress as deleteAddressAPI } from "../../services/apiAuthentication"
import toast from "react-hot-toast"
export function useDeleteAddress() {
    const queryClient = useQueryClient()
    const { token } = useAuthContext()
    const { isLoading, mutate: deleteAddress } = useMutation({
        mutationFn: ({ addressId }) =>
            deleteAddressAPI(addressId, token),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: ["address"]
            })
            toast.success("Xóa địa chỉ thành công")
        },
        onError: (error) => toast.error(error.message)
    })
    return { isLoading, deleteAddress }
}