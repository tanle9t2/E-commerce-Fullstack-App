import { useMutation, useQueryClient } from "@tanstack/react-query";
import { updateCartItem as updateCartItemAPI } from "../../services/apiCart";
import toast from "react-hot-toast";
import { useAuthContext } from "../../context/AuthContext";
export function useUpdateCart() {
    const queryClient = useQueryClient();
    const { token } = useAuthContext()
    const { isLoading, mutate: updateCartItem } = useMutation({
        mutationFn: ({ skuId, quantity }) => updateCartItemAPI({ skuId, quantity }, token),
        onSuccess: () => {
            toast.success("Cart item successfull update")
            queryClient.invalidateQueries({
                queryKey: ["cart", 1]
            })

        },
        onError: (error) => toast.error(error.message),
    })
    return { isLoading, updateCartItem }
}