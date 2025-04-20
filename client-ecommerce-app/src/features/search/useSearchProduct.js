import { useQuery, useQueryClient } from "@tanstack/react-query";
import { useParams, useSearchParams } from "react-router-dom";
import { searchProduct } from "../../services/apiSearch";
import { PAGE_SIZE_PRODUCT } from "../../utils/constant";

export function useSearchProduct() {
    const queryClient = useQueryClient();
    const { shopId: tenantId } = useParams();
    const [searchParams] = useSearchParams()
    const keyword = searchParams.get("keyword");
    const page = searchParams.get("page") ? searchParams.get("page") - 1 : 0
    const category = searchParams.get("category")
    const sortBy = searchParams.get("sortBy")
    const order = searchParams.get("order")
    const minPrice = searchParams.get("minPrice")
    const maxPrice = searchParams.get("maxPrice")
    const location = searchParams.get("location")
    const lft = searchParams.get("lft")
    const rgt = searchParams.get("rgt")
    const { isLoading,
        data: { data: products, count, page: currentPage } = {},
        error } = useQuery({
            queryKey: ["search", keyword, tenantId, lft, rgt, location, minPrice, maxPrice, category, sortBy, order, page],
            queryFn: () => searchProduct({ keyword, tenantId, lft, rgt, location, minPrice, maxPrice, category, sortBy, order, size: PAGE_SIZE_PRODUCT, page })
        })

    const pageCount = Math.ceil(count / PAGE_SIZE_PRODUCT);
    if (page < pageCount) {
        queryClient.prefetchQuery({
            queryKey: ["search", keyword, tenantId, lft, rgt, location, minPrice, maxPrice, category, sortBy, order, page + 1],
            queryFn: () => searchProduct({ keyword, tenantId, lft, rgt, location, minPrice, maxPrice, category, sortBy, order, size: PAGE_SIZE_PRODUCT, page: page + 1 }),
        });
    }
    if (page > 1) {
        queryClient.prefetchQuery({
            queryKey: ["search", keyword, tenantId, lft, rgt, location, minPrice, maxPrice, category, sortBy, order, page - 1],
            queryFn: () => searchProduct({ keyword, tenantId, lft, rgt, minPrice, maxPrice, category, sortBy, order, size: PAGE_SIZE_PRODUCT, page: page - 1 }),
        });
    }

    return { isLoading, products, currentPage, count, totalPages: pageCount, error }
}