import { useQuery, useQueryClient } from "@tanstack/react-query";
import { useSearchParams } from "react-router-dom";
import { getFilterSearch } from "../../services/apiSearch";

export function useGetFilterSearch() {
    const [searchParams] = useSearchParams()
    const keyword = searchParams.get("keyword");

    const { isLoading, data: filters, error } = useQuery({
        queryKey: ["filterSearch", keyword],
        queryFn: () => getFilterSearch(keyword)
    })

    return { isLoading, filters, error }
}