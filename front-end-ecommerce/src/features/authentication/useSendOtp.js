import { useMutation } from "@tanstack/react-query";
import { verifyEmail } from "../../services/apiMail";

export function useSendOtp() {
    const { isLoading, mutate: sendOtp } = useMutation({
        mutationFn: (toEmail) => verifyEmail(toEmail)
    })
    return { isLoading, sendOtp };
}