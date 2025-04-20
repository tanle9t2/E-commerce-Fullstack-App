import { useMutation, useQueryClient } from "@tanstack/react-query";
import toast from "react-hot-toast";
import { getVariationInfor } from "../../services/apiProduct";


export function useSKU() {
  const queryClient = useQueryClient();
  const { isLoading, mutate: getVariation } = useMutation({
    mutationKey: ["sku"],
    mutationFn: getVariationInfor,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["sku"],
      });
    },
    onError: (error) => toast.error(error.message),
  });

  return { isLoading, getVariation };
}