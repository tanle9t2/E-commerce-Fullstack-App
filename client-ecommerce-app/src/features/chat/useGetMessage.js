import { useMutation } from "@tanstack/react-query";
import { getMessages as getMessagesAPI } from "../../services/apiChat";

export function useGetMessage() {
    const { isLoading, mutate: getMessage } = useMutation({
        mutationFn: (recipientId) => getMessagesAPI(recipientId)
    })

    return { isLoading, getMessage }
}