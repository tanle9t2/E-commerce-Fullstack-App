import { useQuery } from "@tanstack/react-query";
import { useAuthContext } from "../../context/AuthContext";
import { getMethods } from "../../services/apiPayment";

export function usePaymentMethods() {
    const { token } = useAuthContext()
    const { isLoading, data: methods, error } = useQuery({
        queryKey: ["methods"],
        queryFn: () => getMethods(token)
    })

    return { isLoading, methods, error };
}