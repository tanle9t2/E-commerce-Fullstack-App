import { useQuery } from "@tanstack/react-query";
import { useParams } from "react-router-dom";
import { getTenants } from "../../services/apiTenant";

export function useTenants() {
    const { shopId } = useParams()
    const { isLoading, data: { tenantInfor, products, categories } = {}, error } = useQuery({
        queryKey: ["tenants",],
        queryFn: () => getTenants(shopId)
    })
    return { isLoading, tenantInfor, products, categories, error }
}