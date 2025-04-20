import SignupForm from "../features/authentication/SignupForm"
import AuthLayout from "../ui/AuthLayout"

function SignUp() {
    return (
        <AuthLayout type={"Đăng ký"}>
            <SignupForm />
        </AuthLayout>
    )
}

export default SignUp
