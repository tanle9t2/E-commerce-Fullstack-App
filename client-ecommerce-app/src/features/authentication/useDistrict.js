import { useMutation } from "@tanstack/react-query";
import { getDistricts } from "../../services/apiOpenAddress";
import toast from "react-hot-toast";

export function useDistrct() {
    const { isLoading, mutate: getDistrict } = useMutation({
        mutationFn: (code) => getDistricts(code),
        onError: (error) => toast.error(error.message)
    })
    return { isLoading, getDistrict }
}