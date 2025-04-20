import { useMutation } from "@tanstack/react-query";
import { createProduct as createProductAPI } from "../../services/apiProducts";
import toast from "react-hot-toast";

export function useCreateProduct() {
    const { isLoading, mutate: createProduct } = useMutation({
        mutationFn: (data) => createProductAPI(data),
        onSuccess: () => {
            toast.success("Thêm sản phẩm thành công")
        },
        onError: (error) => toast.error(error.message)
    })
    return { isLoading, createProduct }
}