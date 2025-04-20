import { useQuery } from "@tanstack/react-query";
import { useAuthContext } from "../../context/AuthContext";
import { getOrders } from "../../services/apiOrder";

export function useOrders() {
    const { token } = useAuthContext()
    const { isLoading, data: orders, error } = useQuery({
        queryKey: ["order"],
        queryFn: () => getOrders(token)
    })

    return { isLoading, orders, error }
}