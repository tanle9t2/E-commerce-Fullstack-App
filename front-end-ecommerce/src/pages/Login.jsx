import styled from "styled-components"
import LoginForm from "../features/authentication/LoginForm"
import Footer from "../ui/Footer"

const StyledLogin = styled.div`
    background-color:rgb(239, 83, 42);
    width:100%;
    height:100%;
`
const ContainerForm = styled.div`
    background-image: url("https://down-vn.img.susercontent.com/file/sg-11134004-7rff9-m47h5vo9qfr552");
    background-repeat:no-repeat;
    background-size: contain;
    height: 600px;
    margin: 0 auto;
    min-height: 600px;
    display:flex;
    align-items:center;
    width: 1040px;
    position:relative;
`
const Img = styled.img`
  height: 4.6rem;
  width: auto;
`;
const HeaederLogin = styled.div`
    display:flex;
    align-items:center;
    line-height:2rem;
    padding: var(--padding-container);
    
`
function Login() {
    return (<>
        <HeaederLogin>
            <Img src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/fe/Shopee.svg/1200px-Shopee.svg.png"/>
            <h1 className="ml-3 mt-6 text-4xl font-semibold">Đăng nhập</h1>
        </HeaederLogin>
        <StyledLogin>
            <ContainerForm>
                 <LoginForm/>
            </ContainerForm>
        </StyledLogin>
        <Footer/>
        </>
    )
}

export default Login
