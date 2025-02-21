import { useMutation } from "@tanstack/react-query";
import { getWards } from "../../services/apiOpenAddress";

export function useWard() {
    const { isLoading, mutate: getWard } = useMutation({
        mutationFn: (code) => getWards(code)
    })
    return { isLoading, getWard }
}