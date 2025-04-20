import { useMutation, useQueryClient } from "@tanstack/react-query";
import { deleteCartItem as deleteCartItemAPI } from "../../services/apiCart";
import toast from "react-hot-toast";
import { useAuthContext } from "../../context/AuthContext";
export default function useDeleteCartItem() {
    const queryClient = useQueryClient();
    const { token } = useAuthContext()
    const { isLoading, mutate: deleteCartItem } = useMutation({
        mutationFn: ({ cartItems }) => deleteCartItemAPI({ cartItems, token }),
        onSuccess: () => {
            toast.success("Cart item successfull delete")
            queryClient.invalidateQueries({
                queryKey: ["cart", 1]
            })
        },
        onError: (error) => toast.error(error)
    })
    return { isLoading, deleteCartItem }
}