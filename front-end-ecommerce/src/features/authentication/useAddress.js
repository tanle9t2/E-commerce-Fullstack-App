import { useQuery } from "@tanstack/react-query";
import { useAuthContext } from "../../context/AuthContext";
import { getAddress } from "../../services/apiAuthentication";

export function useAddress() {
    const { token } = useAuthContext()
    const { isLoading, data: addresses, error } = useQuery({
        queryKey: ["address"],
        queryFn: () => getAddress(token)
    })
    return { isLoading, addresses, error }
}