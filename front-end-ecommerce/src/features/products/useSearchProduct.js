import { useQuery, useQueryClient } from "@tanstack/react-query";
import { useSearchParams } from "react-router-dom";
import { searchProduct } from "../../services/apiProduct";
import { PAGE_SIZE_PRODUCT } from "../../utils/constant";

export function useSearchProduct() {
    const queryClient = useQueryClient();
    const [searchParams] = useSearchParams()
    const keyword = searchParams.get("keyword");
    const page = searchParams.get("page") ? searchParams.get("page") - 1 : 0
    const category = searchParams.get("category")
    const sortBy = searchParams.get("sortBy")
    const order = searchParams.get("order")
    const minPrice = searchParams.get("minPrice")
    const maxPrice = searchParams.get("maxPrice")
    const location = searchParams.get("location")
    const { isLoading,
        data: { data: products, count, page: currentPage, filter } = {},
        error } = useQuery({
            queryKey: ["search", keyword, location, minPrice, maxPrice, category, sortBy, order, page],
            queryFn: () => searchProduct({ keyword, location, minPrice, maxPrice, category, sortBy, order, size: PAGE_SIZE_PRODUCT, page })
        })

    const pageCount = Math.ceil(count / PAGE_SIZE_PRODUCT);
    if (page < pageCount) {
        queryClient.prefetchQuery({
            queryKey: ["search", keyword, location, minPrice, maxPrice, category, sortBy, order, page + 1],
            queryFn: () => searchProduct({ keyword, location, minPrice, maxPrice, category, sortBy, order, size: PAGE_SIZE_PRODUCT, page: page + 1 }),
        });
    }
    if (page > 1) {
        queryClient.prefetchQuery({
            queryKey: ["search", keyword, location, minPrice, maxPrice, category, sortBy, order, page - 1],
            queryFn: () => searchProduct({ keyword, minPrice, maxPrice, category, sortBy, order, size: PAGE_SIZE_PRODUCT, page: page - 1 }),
        });
    }

    return { isLoading, products, currentPage, totalPages: pageCount, filter, error }
}