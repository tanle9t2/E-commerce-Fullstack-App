import styled from "styled-components"
import Form from "../../ui/Form"
import FormRowVertical from "../../ui/FormRowVertical"
import Input from "../../ui/Input"
import Button from "../../ui/Button"

import { useEffect, useRef, useState } from "react"
import DropDown from "../../ui/DropDown"
import { useForm } from "react-hook-form"
import { splitName, validatePhone } from "../../utils/helper"
import { useUpdateAddress } from "./useUpdateAddress"
import { useCreateAddress } from "./useCreateAddress"
import Spinner from "../../ui/Spinner"
const CustomForm = styled(Form)`
    border:none;
    padding:3.2rem 1rem;
`
const BackButton = styled.span`
    font-size:1.6rem;
    margin-right:40px;
    cursor: pointer;
`
const RowWithMargin = styled.div`
    margin-right:10px !important;
`
const Heading = styled.p`
    font-size:2rem;
    font-weight:700;
`
function SelectionAddressForm({addressId = 0,heading,fullName = undefined,phone = undefined,detail = undefined,city = undefined,district= undefined,ward= undefined,onCloseModal}) {
    const [state, setState] = useState({city,district,ward})
    const [isOpen, setIsOpen] = useState(false);
    const { register, handleSubmit, formState, setError } = useForm();
    const { errors } = formState;
    const isEditSession = Boolean(addressId);
    const {isLoading: loadingUpdate, updateAddress} = useUpdateAddress()
    const {isLoading: loadingCreate, createAddress} = useCreateAddress()
    const rs = useRef(null)
    useEffect(() => {
        console.log(rs.current)
        if (rs.current) {
          console.log(rs.current.value)
        }
      }, [state]);
    function handleOnChangeProvince(value) {
        if (value.city) {
            setState(({ ...value }))
        } else if (value.district) {
            setState((prevState) => {
                const newState = { ...prevState, ...value }; // Merge new values
                delete newState.ward; // Remove the key you want
                return newState;
            });
        } else if (value.ward) {
            setState(state => ({ ...state, ...value }))
        }
    }
    function onSubmit({ name, phone, detail }) {
        console.log(state)
        if(isEditSession) {
            updateAddress({...splitName(name)
                ,phoneNumber:phone
                ,streetNumber:detail
                ,city:state.city
                ,district:state.district
                ,ward:state.ward
                ,id: addressId})
        } else {
            createAddress({...splitName(name)
                ,phoneNumber:phone
                ,streetNumber:detail
                ,city:state.city
                ,district:state.district
                ,ward:state.ward})
        }
        onCloseModal()
    }
    return (
        <CustomForm onSubmit={handleSubmit(onSubmit)}>
            <Heading>{heading}</Heading>
            <div className="flex justify-between">
               <RowWithMargin>
               <FormRowVertical error={errors?.name?.message}>
                    <Input
                        type="text"
                        id="name"
                        disabled ={loadingUpdate || loadingCreate}
                        autoComplete="current-name"
                        placeholder="Họ và tên"
                        defaultValue={fullName}
                        {...register("name", {
                            required: "Vui lòng nhập họ và tên",
                        })}
                    />
                </FormRowVertical>
               </RowWithMargin>
                <FormRowVertical error={errors?.phone?.message}>
                    <Input
                        type="text"
                        id="phone"
                        autoComplete="current-phone"
                        placeholder="Số điện thoại"
                        disabled ={loadingUpdate || loadingCreate}
                        defaultValue={phone}
                        {...register("phone", {
                            required: "Vui lòng nhập số điện thoại",
                            minLength: {
                                value: 10,
                                message: "Số điện thoại không hợp lệ",
                              },
                            maxLength: {
                                value:11,
                                message: "Số điện thoại không hợp lệ"
                            },
                            validate: (value) =>
                                validatePhone(value) || "Số điện thoại không hợp lệ",
                        })}
                    />
                </FormRowVertical>
            </div>
            <FormRowVertical error={errors?.address?.message}>
                <Input
                    ref={rs}
                    disabled ={loadingUpdate || loadingCreate}
                    onChange={() => console.log("Current value:", rs.current?.value)}
                    onClick={() => setIsOpen((prev) => !prev)}
                    type="text"
                    id="address"
                    autoComplete="current-address"
                    value={state.city || state.district || state.ward
                        ? `${state.city ? state.city + ", " : ""} ${state.district ? state.district + ", ": ""} ${state.ward || ""}`
                        : undefined}
                    placeholder="Tỉnh/Thành phố, Quận/Huyện, Phường/Xã"
                    {...register("address", {
                        validate: () => state.city && state.district && state.ward
                        ? true
                        : "Vui lòng nhập Tỉnh/Thành phố, Quận/Huyện, Phường/Xã"
                    })}
                />
                <DropDown isOpen={isOpen} setIsOpen={setIsOpen} state={state} handleOnChange={handleOnChangeProvince}>
                </DropDown>
            </FormRowVertical>
            <FormRowVertical error={errors?.detail?.message}>
                <Input
                    disabled ={loadingUpdate || loadingCreate}
                    type="text"
                    id="detail"
                    defaultValue={detail}
                    autoComplete="current-password"
                    placeholder="Địa chỉ cụ thể"
                    {...register("detail", {
                        required: "Vui lòng nhập địa chỉ cụ thể",
                        
                    })}
                />
            </FormRowVertical>
            <FormRowVertical >
                <div className="flex justify-end items-center">
                    <BackButton onClick={onCloseModal}>Trở lại</BackButton>
                    <Button disabled ={loadingUpdate || loadingCreate} size="large">{loadingUpdate || loadingCreate ? <Spinner/>: 'Hoàn thành'}</Button>
                </div>
            </FormRowVertical>

        </CustomForm>
    )
}

export default SelectionAddressForm
