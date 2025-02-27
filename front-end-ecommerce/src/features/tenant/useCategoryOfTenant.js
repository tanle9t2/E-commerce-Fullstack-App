import { useQuery } from "@tanstack/react-query";
import { getCategoryByTenant } from "../../services/apiCategory";
import { useParams } from "react-router-dom";

export function useCategoryOfTenant() {
    const { shopId } = useParams()
    const { isLoading, data: category, error } = useQuery({
        queryKey: ["category"],
        queryFn: () => getCategoryByTenant(shopId)
    })

    return { isLoading, category, error };
}