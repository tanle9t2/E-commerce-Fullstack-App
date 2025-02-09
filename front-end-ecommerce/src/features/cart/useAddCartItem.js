import { useMutation, useQueries, useQueryClient } from "@tanstack/react-query";
import { addCartItem as addCartItemAPI } from "../../services/apiCart";
import toast from "react-hot-toast";
export function useAddCartItem() {
    const queryClient = useQueryClient();
    const {isLoading,mutate:addCartItem} = useMutation({
        mutationFn:addCartItemAPI,
        onSuccess:() => {
            queryClient.invalidateQueries({
                queryKey:["cart",1]
            })
            toast.success("Item successfull add")
        }
    })
    return {isLoading,addCartItem}
}