import styled from "styled-components"
import LoginForm from "../features/authentication/LoginForm"
import Footer from "../ui/Footer"
import AuthLayout from "../ui/AuthLayout"


function Login() {
    return (<AuthLayout type={"Đăng nhập"}>
        <LoginForm />
    </AuthLayout>
    )
}

export default Login
