import { useQuery } from "@tanstack/react-query";
import useDebounce from "../../hook/useDebounce";
import { searchHint } from "../../services/apiSearch";

export function useSearchHint(searchTerm) {
    const debouncedSearch = useDebounce(searchTerm, 500);

    const { isLoading, data: hint, error } = useQuery({
        queryKey: ["searchNav", debouncedSearch],
        queryFn: () => searchHint(debouncedSearch),
        enabled: !!debouncedSearch,
    });

    return { isLoading, hint, error }
}