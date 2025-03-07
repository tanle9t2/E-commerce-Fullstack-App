import { useMutation } from "@tanstack/react-query";
import { verifyOtp as verifyOtpAPI } from "../../services/apiMail";

export function useVerifyOtp() {
    const { isLoading, mutate: verifyOtp } = useMutation({
        mutationFn: (otp) => verifyOtpAPI(otp)
    })
    return { isLoading, verifyOtp }
}