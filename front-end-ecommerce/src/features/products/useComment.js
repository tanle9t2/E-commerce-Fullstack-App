import { useQuery, useQueryClient } from "@tanstack/react-query";
import { getComments } from "../../services/apiProduct";
import { useParams, useSearchParams } from "react-router-dom";

import { PAGE_SIZE_COMMENT } from "../../utils/constant";

export function useComments() {
    const queryClient = useQueryClient();
    const { productId } = useParams()
    const [searchParams] = useSearchParams();
    const page = searchParams.get("page") ?  searchParams.get("page") : 1;
    const {
        isLoading,
        data: { data: comments, count } = {},
        error,
      } = useQuery({
        queryKey: ["comments",productId, page],
        queryFn: () => getComments({ productId,page }),
      });
    const pageCount = Math.ceil(count / PAGE_SIZE_COMMENT);
    if (page < pageCount) {
      queryClient.prefetchQuery({
        queryKey: ["comments",productId,  page + 1],
        queryFn: () => getComments({ productId, page: page + 1 }),
      });
    }
    if (page > 1) {
      queryClient.prefetchQuery({
        queryKey: ["comments",productId, page - 1],
        queryFn: () => getComments({productId, page: page - 1 }),
      });
    }
    
    return {isLoading,comments,count,error}
}