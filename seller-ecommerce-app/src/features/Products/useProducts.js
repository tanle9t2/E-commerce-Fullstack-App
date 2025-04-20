import { useQuery, useQueryClient } from "@tanstack/react-query";
import { getProductsOfTenant } from "../../services/apiProducts";
import { useSearchParams } from "react-router-dom";
import { PAGE_SIZE_PRODUCT } from "../../utils/constant";
import { useEffect } from "react";

export function useProuducts() {
    const [searchParams] = useSearchParams()
    const queryClient = useQueryClient();
    const page = searchParams.get("page") ? searchParams.get("page") - 1 : 0
    const { isLoading,
        data: { data: products, count, page: currentPage } = {}
        , error } = useQuery({
            queryKey: ["products", 1, page],
            queryFn: () => getProductsOfTenant({ tenantId: 1, page })
        })
    const pageCount = Math.ceil(count / PAGE_SIZE_PRODUCT);

    if (page < pageCount - 1) {
        queryClient.prefetchQuery({
            queryKey: ["products", 1, page + 1],
            queryFn: () => getProductsOfTenant({ tenantId: 1, page: page + 1 }),
        });
    }
    if (page > 0) {
        queryClient.prefetchQuery({
            queryKey: ["products", 1, page - 1],
            queryFn: () => getProductsOfTenant({ tenantId: 1, page: page - 1 }),
        });
    }


    return { isLoading, products, currentPage, count, totalPages: pageCount, error }
}