import styled from "styled-components";
import Form from "../../ui/Form";
import Button from "../../ui/Button";
import FormRowVertical from "../../ui/FormRowVertical";
import ButtonGroup from "../../ui/ButtonGroup";
import SpinnerMini from "../../ui/SpinnerMini";
import Input from "../../ui/Input";
import { validatePhone } from "../../utils/helper";

const CustomForm = styled(Form)`
  position:absolute;
`
const CustomButton = styled(Button)`
  width:100%;
`


function SignUpDetailStep({ onSubmit, errors, register, getValues, reset, isLoading }) {
    return (
        <CustomForm onSubmit={onSubmit}>
            <div className="flex justify-between">
                <FormRowVertical error={errors?.firstName?.message}>
                    <Input
                        width="160px"
                        type="text"
                        id="firstName"
                        placeholder="Họ"
                        {...register("firstName", { required: "this field is required" })}
                        disabled={isLoading}
                    />
                </FormRowVertical>
                <FormRowVertical error={errors?.lastName?.message}>
                    <Input
                        width="160px"
                        type="text"
                        id="lastName"
                        placeholder="Tên"
                        {...register("lastName", { required: "this field is required" })}
                        disabled={isLoading}
                    />
                </FormRowVertical>
            </div>
            <FormRowVertical error={errors?.phoneNumber?.message}>
                <Input
                    width="340px"
                    type="text"
                    id="phoneNumber"
                    placeholder="Số điện thoại"
                    {...register("phoneNumber", {
                        required: "this field is required"
                        , validate: (value) => validatePhone(value) || "Số điện thoại không hợp lệ",
                    })}
                    disabled={isLoading}
                />
            </FormRowVertical>
            <FormRowVertical error={errors?.username?.message}>
                <Input
                    width="340px"
                    type="text"
                    id="username"
                    placeholder="Username"
                    {...register("username", { required: "this field is required" })}
                    disabled={isLoading}
                />
            </FormRowVertical>
            <FormRowVertical

                error={errors?.password?.message}
            >
                <Input
                    width="340px"
                    type="password"
                    id="password"
                    placeholder="Mật khẩu (ít nhất 8 kí tự, có kí tự thường, kí tự hoa, kí tự đặc biệt, số)"
                    {...register("password", {
                        required: "this field is required",
                        minLength: {
                            value: 8,
                            message: "Password should be at least 8 character",
                        },
                    })}
                    disabled={isLoading}
                />
            </FormRowVertical>
            <FormRowVertical error={errors?.passwordConfirm?.message}>
                <Input
                    width="340px"
                    type="password"
                    id="passwordConfirm"
                    placeholder="Xác nhận mật khẩu"
                    {...register("passwordConfirm", {
                        required: "this field is required",
                        validate: (value) =>
                            value === getValues().password || "Mật khẩu không khớp",
                    })}
                    disabled={isLoading}
                />
            </FormRowVertical>

            <FormRowVertical>
                {/* type is an HTML attribute! */}
                <ButtonGroup >
                    <CustomButton
                        variation="secondary"
                        type="reset"
                        disabled={isLoading}
                    // onClick={() => reset()}
                    >
                        Hủy
                    </CustomButton>
                    <CustomButton>{isLoading ? <SpinnerMini /> : "Tạo tài khoản"}</CustomButton>
                </ButtonGroup>
            </FormRowVertical>
        </CustomForm>
    )
}

export default SignUpDetailStep
