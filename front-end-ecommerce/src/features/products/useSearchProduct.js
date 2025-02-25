import { useQuery } from "@tanstack/react-query";
import { useSearchParams } from "react-router-dom";
import { searchProduct } from "../../services/apiProduct";

export function useSearchProduct() {
    const [searchParams] = useSearchParams()
    const keyword = searchParams.get("keyword");

    const { isLoading, data: response, error } = useQuery({
        queryKey: ["search", keyword],
        queryFn: () => searchProduct({ keyword })
    })
    return { isLoading, response, error }
}