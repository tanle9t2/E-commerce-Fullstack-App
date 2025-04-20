import { useMutation } from "@tanstack/react-query";
import { useAuthContext } from "../../context/AuthContext";
import { createOrder as createOrderAPI } from "../../services/apiOrder";
import toast from "react-hot-toast";
import { getPaymentUrl } from "../../services/apiPayment";
import { useNavigate } from "react-router-dom";
import { useCartContext } from "../../context/CartContext";
export function useCreateOrder() {
    const { token } = useAuthContext();
    const navigate = useNavigate()
    const { isLoading, mutate: createOrder } = useMutation({
        mutationFn: (data) => createOrderAPI(data, token),
        onSuccess: async (data) => {
            const { paymentMethodId, orderIds } = data.data;
            console.log(paymentMethodId)
            if (paymentMethodId === 2) {
                const payment = await getPaymentUrl("NCB", orderIds);
                window.location.replace(payment.paymentUrl)
            } else if (paymentMethodId === 1) {
                navigate('/user/account/purchase')
            }
        },
        onError: (error) => toast.error(error.message)
    })

    return { isLoading, createOrder }
}