import { useMutation } from "@tanstack/react-query";
import { searchProduct } from "../../services/apiSearch";
import { useParams, useSearchParams } from "react-router-dom";

export function useGetProductByCategory() {

    const { isLoading, mutate: getProducts } = useMutation({
        mutationFn: ({ tenantId, category, lft, rgt }) => searchProduct({ tenantId, category, lft, rgt })
    })

    return { isLoading, getProducts }
}