import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createAddress as createAddressAPI } from "../../services/apiAuthentication";
import { useAuthContext } from "../../context/AuthContext";
import toast from "react-hot-toast";
export function useCreateAddress() {
    const queryClient = useQueryClient()
    const { token } = useAuthContext()
    const { isLoading, mutate: createAddress } = useMutation({
        mutationFn: ({ city, district, ward, streetNumber, firstName, lastName, phoneNumber }) =>
            createAddressAPI({ city, district, ward, streetNumber, firstName, lastName, phoneNumber }, token),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: ["address"]
            })
            toast.success("Thêm địa chỉ mới thành công")
        },
        onError: (error) => toast.error(error.message)
    })
    return { isLoading, createAddress }
}