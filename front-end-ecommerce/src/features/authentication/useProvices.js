import { useQuery } from "@tanstack/react-query";
import { getProvinces } from "../../services/apiOpenAddress"
export function useProvices() {
    const { isLoading, data: provinces, error } = useQuery({
        queryKey: ["provinces"],
        queryFn: getProvinces
    })

    return { isLoading, provinces, error }
}