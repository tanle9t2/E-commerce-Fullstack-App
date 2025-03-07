import { useMutation } from "@tanstack/react-query";
import { signUp as signUpAPI } from "../../services/apiAuthentication";
import { useAuthContext } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
export function useSignUp() {
    const { handleLogin } = useAuthContext();
    const navigate = useNavigate();
    const { isLoading, mutate: signUp } = useMutation({
        mutationFn: ({ firstName, lastName, phoneNumber, email, username, password }) => signUpAPI({ firstName, lastName, phoneNumber, email, username, password }),
        onSuccess: (res) => {
            const { accessToken, refreshToken, userInfor } = res.data;
            handleLogin(accessToken, refreshToken, userInfor)
            navigate('/')
        },
    })

    return { isLoading, signUp }
}