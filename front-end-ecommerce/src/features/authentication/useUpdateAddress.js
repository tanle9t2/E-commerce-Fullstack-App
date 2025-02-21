import { useMutation, useQueryClient } from "@tanstack/react-query";
import { updateAddress as updateAddressAPI } from "../../services/apiAuthentication";
import { useAuthContext } from "../../context/AuthContext";
import toast from "react-hot-toast";
export function useUpdateAddress() {
    const { token } = useAuthContext()
    const queryClient = useQueryClient()
    const { isLoading, mutate: updateAddress } = useMutation({
        mutationFn: ({ id, city, district, ward, streetNumber, firstName, lastName, phoneNumber }) =>
            updateAddressAPI({ id, city, district, ward, streetNumber, firstName, lastName, phoneNumber }, token),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: ["address"]
            })
            toast.success("Cập nhật địa chỉ thành công")
        },
        onError: (error) => toast.error(error.message)
    })

    return { isLoading, updateAddress }
}