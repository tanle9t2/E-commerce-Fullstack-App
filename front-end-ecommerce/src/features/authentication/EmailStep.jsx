
import FormRowVertical from "../../ui/FormRowVertical";
import { validateEmail } from "../../utils/helper";
import SpinnerMini from "../../ui/SpinnerMini";
import Button from "../../ui/Button";
import Input from "../../ui/Input";
import Form from "../../ui/Form";
import styled from "styled-components";
import ButtonText from "../../ui/ButtonText";
import ButtonIcon from "../../ui/ButtonIcon";
import { MdFacebook } from "react-icons/md";
import ButtonGroup from "../../ui/ButtonGroup";
import { FcGoogle } from "react-icons/fc";
const CustomForm = styled(Form)`
  position:absolute;
`
const CustomButton = styled(Button)`
  width:100%;
`
const StyledLineBreak = styled.div`
    background-color: #dbdbdb;
    flex: 1;
    height: 1px;
    width: 100%;

`;
const StyledLoginSocical = styled.div`
    display:flex;
    align-items:center;
`
const StyledWord = styled.div`  
    color:var(--color-grey-800);
    margin:0 10px;
`;
function EmailStep({ onSubmit, errors, loading, register }) {
    return (
        <CustomForm onSubmit={onSubmit}>
            <FormRowVertical label="Email" error={errors.toEmail?.message}>
                <Input
                    width="340px"
                    type="text"
                    id="toEmail"
                    {...register("toEmail", {
                        required: "Vui lòng nhập email",
                        validate: (value) => validateEmail(value) || "Email không hợp lệ",
                    })}
                    disabled={loading}
                />
            </FormRowVertical>
            <CustomButton disabled={loading}>{loading ? <SpinnerMini /> : "Tiếp theo"}</CustomButton>
            <FormRowVertical className>
                <StyledLoginSocical>
                    <StyledLineBreak />
                    <StyledWord className="ml2 mr-2">Hoặc</StyledWord>
                    <StyledLineBreak />
                </StyledLoginSocical>
                <ButtonGroup>
                    <Button variation="secondary" size="medium">
                        <ButtonIcon>
                            <MdFacebook />
                        </ButtonIcon>
                        <ButtonText>
                            Facebook
                        </ButtonText>
                    </Button>
                    <Button variation="secondary" size="medium">
                        <ButtonIcon>
                            <FcGoogle />
                        </ButtonIcon>
                        <ButtonText>
                            Google
                        </ButtonText>
                    </Button>
                </ButtonGroup>
            </FormRowVertical>
            <FormRowVertical  >
                <span>Bạn đã có tài khoản? <a className="text-center" href="#">Đăng nhập</a> </span>
            </FormRowVertical>

        </CustomForm >
    );
}

export default EmailStep
