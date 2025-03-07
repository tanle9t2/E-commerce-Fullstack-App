import styled from "styled-components";
import Button from "../../ui/Button";
import ButtonGroup from "../../ui/ButtonGroup";
import FormRowVertical from "../../ui/FormRowVertical";
import Input from "../../ui/Input";
import SpinnerMini from "../../ui/SpinnerMini";
import Form from "../../ui/Form";
const CustomForm = styled(Form)`
  position:absolute;
`

function OtpStep({ onSubmit, errors, loading, register, resendOtp }) {
    return (
        <CustomForm onSubmit={onSubmit}>
            <FormRowVertical label="Nhập mã OTP" error={errors.otp?.message}>
                <Input width="340px" type="text" id="otp" {...register("otp", { required: "Vui lòng nhập mã OTP" })} disabled={loading} />
            </FormRowVertical>
            <ButtonGroup>
                <Button disabled={loading}>{loading ? <SpinnerMini /> : "Xác nhận"}</Button>
                <Button variation="secondary" type="button" onClick={resendOtp}>
                    Gửi lại OTP
                </Button>
            </ButtonGroup>
        </CustomForm>
    );
}

export default OtpStep
