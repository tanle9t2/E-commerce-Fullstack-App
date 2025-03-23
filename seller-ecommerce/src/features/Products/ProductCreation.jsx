import React, { useEffect, useReducer, useRef, useState } from 'react';
import styled from 'styled-components';
import Form from "../../ui/Form"
import FormRow from "../../ui/FormRow"
import Input from "../../ui/Input"
import VariationAdd from './VariationAdd';
import Spinner from "../../ui/Spinner"
import { useForm } from 'react-hook-form';
import { useCreateProduct } from './useCreateProduct';
// Styled Components



const InputGroup = styled.div`
  margin-bottom: 15px;
`;

const Label = styled.label`
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
`;



const TextArea = styled.textarea`
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
  height: 100px;
`;



const SubmitButton = styled.button`
  padding: 10px 20px;
  background-color: #ee4d2d;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  float: right;
`;
const ImagePreview = styled.div`
 display: flex;
  gap: 10px;
  margin-top: 10px;
  overflow-x: auto; /* Enable horizontal scrolling */
  white-space: nowrap; /* Prevent wrapping */
  padding-bottom: 10px; /* Space for scrollbar */
  &::-webkit-scrollbar {
    height: 8px;
  }
  &::-webkit-scrollbar-thumb {
    background-color: #ee4d2d; /* Red scrollbar thumb */
    border-radius: 4px;
  }
  &::-webkit-scrollbar-track {
    background-color: #f5f5f5;
  }
`;

const ImageItem = styled.div`
    position: relative;
    border-radius: 4px;
    overflow: hidden;
    flex: 0 0 100px;
    height: 119px;
    border: 2px dashed #ddd;
    padding: 6px;
    text-align: center;
    margin-bottom: 10px;
`;
const CoverImageLabel = styled.div`
  background-color: #d3d3d3;
  padding: 2px 8px;
  font-size: 12px;
  position: absolute;
  top: 0;
  z-index:10;
  left: 0;
`;

const Image = styled.img`
  width: 100px;
  height: 100px;
  object-fit: cover;
`;

const AddImageButton = styled.button`
  padding: 8px 16px;
  margin-top:10px;
  background-color: #ee4d2d;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
`;
const ImageSection = styled.div`
    justify-content:flex-start;
   
`

function reducer(state, action) {
    switch (action.type) {
        case "changeName": {
            const { optionIndex, value } = action.payload;
            return {
                ...state,
                options: state.options.map((option, index) => index === optionIndex ? { ...option, name: value } : option)
            };
        }
        case "changeValue": {
            const { id, index, newValue } = action.payload;

            const newState = {
                ...state,
                options: state.options.map((option, optIndex) => {
                    return optIndex === id
                        ? {
                            ...option,
                            value: option.value.map((val, valIndex) =>
                                valIndex === index ? newValue : val
                            )
                        }
                        : option
                })

            }
            if (newState.options[id].value.length - 1 === index) {
                newState.options[id].value.push(null)
            }
            return newState;
        }
        case "changeskuPrice": {
            const { index, newskuPrice } = action.payload;
            return {
                ...state,
                variations: state.variations.map((v, idx) => {

                    return (index) === idx ? { ...v, skuPrice: newskuPrice } : v
                })
            }

        }
        case "changeskuStock": {
            const { index, newskuStock } = action.payload;

            return {
                ...state,
                variations: state.variations.map((v, idx) => index === idx ? { ...v, skuStock: newskuStock } : v)
            }
        }
        case "changeSkuNo": {
            const { index, newSkuNo } = action.payload;

            return {
                ...state,
                variations: state.variations.map((v, idx) => index === idx ? { ...v, skuNo: newSkuNo } : v)
            }
        }
        case "createVarationValue": {
            const { id, index } = action.payload
            if (state.options.length === 2) {
                const newVarations = []
                for (let i = 0; i < state.options[id].value.length - 1; i++) {
                    newVarations.push({
                        "skuNo": "",
                        "skuStock": null,
                        "skuPrice": null,
                        "optionValueIndex": id === 0 ? [index, i] : [i, index]
                    })

                }

                return {
                    ...state,
                    variations: [...state.variations, ...newVarations]
                };
            }
            return {
                ...state,
                variations: [...state.variations, {
                    "skuNo": "",
                    "skuStock": null,
                    "skuPrice": null,
                    "optionValueIndex": [index]
                }]
            };

        }
        case "createVariation":

            const newOptions = [...state.options, {
                "name": "",
                "value": [""],
            }]
            let newVariations;
            if (newOptions.length === 2) {
                newVariations = []
                for (let i = 0; i < newOptions[0].value.length - 1; i++) {
                    newVariations.push({
                        "skuNo": "",
                        "skuStock": null,
                        "skuPrice": null,
                        "optionValueIndex": [i, 0]
                    })
                }
            } else {
                newVariations = [...state.variations, {
                    "skuNo": "",
                    "skuStock": null,
                    "skuPrice": null,
                    "optionValueIndex": [newOptions[0].value.length - 1]
                }]
            }

            return {
                variations: newVariations, options: newOptions
            }

        case "removeOptions":
            const { id } = action.payload
            const newItems = state.options.filter((option, index) => id !== index)
            console.log(newItems)
            return {
                options: newItems,
                variations: newItems.length ? newItems[0].value.map((item, index) => ({
                    skuNo: "",
                    skuStock: null,
                    skuPrice: null,
                    optionValueIndex: [index]

                })) : []
            }
        case "reset":
            return initOptionsState
        default:
            throw new Error("Unknow action");
    }
}
// const initOptionsState = {
//     options: [
//         {
//        
//             "name": "size",
//             "value": ["1", "2"],
//         },
//         {
//             
//             "name": "color",
//             "value": ["blue", "red"],
//         }
//     ],
//     vartions: [
//           "skuNo": "XSB",
//           "skuStock": 30,
//           "skuPrice": 100,
//           "optionValueIndex": [0, 0]
//     ]
// };
const initOptionsState = {
    options: [

    ],
    variations: [

    ]
};
const ProductForm = () => {
    const [state, dispatch] = useReducer(reducer, initOptionsState)
    const [images, setImages] = useState([]);
    const { register, handleSubmit, formState, clearErrors, reset, setError } = useForm();
    const { errors } = formState;
    const [coverImageIndex, setCoverImageIndex] = useState(null);
    const fileInputRef = useRef(null);
    const { isLoading, createProduct } = useCreateProduct()
    useEffect(() => {
        const timeout = setTimeout(() => {
            if (images.length) {
                clearErrors(`images`);
            }
            state.options.forEach(({ name, value }, index) => {
                if (name !== "") {
                    clearErrors(`option-${index}`);
                }
                value.slice(0, value.length - 1).forEach((v, idx) => {
                    if (v !== "") {
                        clearErrors(`${index}-${idx}`);
                    }
                });
            });
            state.variations.forEach(({ skuPrice, skuStock, skuNo }, index) => {
                if (skuPrice !== null) {
                    clearErrors(`skuPrice-${index}`);
                }
                if (skuStock !== null) {
                    clearErrors(`skuStock-${index}`);
                }
                if (skuNo !== "") {
                    clearErrors(`skuNo-${index}`);
                }
            })
        }, 500)
        return () => clearTimeout(timeout);
    }, [clearErrors, state, images]); // Re-run whenever `state` changes
    if (isLoading) return <Spinner />
    const handleButtonClick = (e) => {
        e.preventDefault();
        fileInputRef.current.click();
    };
    const handleImageUpload = (e) => {
        const files = Array.from(e.target.files);
        const newImages = files.map((file) => ({
            src: URL.createObjectURL(file),
            file,
        }));
        if (images.length + newImages.length <= 9) {
            setImages([...images, ...newImages]);
            if (coverImageIndex === null && images.length === 0) {
                setCoverImageIndex(0);
            }
        } else {
            alert('Maximum 9 images allowed!');
        }

    };

    const handleSetCoverImage = (index) => {
        setCoverImageIndex(index);
    };

    const onSubmit = (data) => {
        console.log(state)
        if (!images.length) {
            setError(`images`, { type: "manual", message: "Vui lòng chọn ít nhất 1 ảnh" });
        }
        state.options.forEach(({ name, value }, index) => {
            if (name === "") {
                setError(`option-${index}`, { type: "manual", message: "Không được để trống ô" });
            }
            value.slice(0, value.length - 1).forEach((v, idx) => {
                if (v === "") {
                    setError(`${index}-${idx}`, { type: "manual", message: "Không được để trống ô" });
                }
            })
        })
        state.variations.forEach(({ skuPrice, skuStock, skuNo }, index) => {
            if (skuPrice === null) {
                setError(`skuPrice-${index}`, { type: "manual", message: "Không được để trống ô" });
            }
            if (skuStock === null) {
                setError(`skuStock-${index}`, { type: "manual", message: "Không được để trống ô" });
            }
            if (skuNo === "") {
                setError(`skuNo-${index}`, { type: "manual", message: "Không được để trống ô" });
            }
        })
        const requestData = {
            product: {
                "name": data.name,
                "reorderLevel": 100,
                "description": data.description,
                "tenant": {
                    "id": 1
                }
            },
            options: state.options.map(o => ({
                name: o.name,
                value: o.value.slice(0, o.value.length - 1)
            })),
            skus: state.variations,
            images
        }
        createProduct(requestData, {
            onSuccess: () => {
                reset()
                setImages([])
                dispatch({
                    "type": "reset"
                })
            }
        })
    };

    return (
        <Form onSubmit={handleSubmit(onSubmit)}>
            <FormRow label="Hình ảnh sản phẩm" error={errors?.images?.message}>
                <ImageSection>
                    <Input ref={fileInputRef} style={{ display: "none" }} type="file" accept='' multiple onChange={handleImageUpload} />
                    <ImagePreview>
                        {images.map((img, index) => (

                            <ImageItem key={index}>
                                {coverImageIndex === index && <CoverImageLabel>Ảnh bìa</CoverImageLabel>}
                                <Image src={img.src} alt={`Product ${index}`} />
                                <button
                                    onClick={() => handleSetCoverImage(index)}
                                    style={{
                                        position: 'absolute',
                                        top: '5px',
                                        right: '5px',
                                        background: 'none',
                                        border: 'none',
                                        cursor: 'pointer',
                                    }}
                                >
                                </button>
                            </ImageItem>
                        ))}
                    </ImagePreview>
                    <AddImageButton onClick={(e) => handleButtonClick(e)} disabled={images.length >= 9}>
                        Thêm hình ảnh ({images.length}/9)
                    </AddImageButton>
                </ImageSection>
            </FormRow>

            <FormRow label="Thông tin cơ bản" error={errors?.name?.message} >
                <Input
                    placeholder="Nhập tên sản phẩm"
                    {...register("name", { required: "Không được để trống ô" })}
                />
            </FormRow>
            <FormRow label="Mô tả sản phẩm" error={errors?.description?.message}>

                <TextArea

                    placeholder="Nhập mô tả sản phẩm"
                    {...register("description", {
                        required: "Không được để trống ô", minLength: {
                            value: 25,
                            message: "Vui lòng nhập tối thiểu 25 ký tự hoặc tải lên tối thiểu 1 hình ảnh.",
                        },
                    })}
                />

            </FormRow>
            <FormRow label="Thông tin sản phẩm">
                <VariationAdd register={register} errors={errors} state={state} dispatch={dispatch} />
            </FormRow>


            <FormRow label="Thông tin khác">
                <InputGroup>
                    <Label>Ghi chú</Label>
                    <Input placeholder="Nhập ghi chú" />
                </InputGroup>
            </FormRow>

            <SubmitButton >Lưu và thêm</SubmitButton>
        </Form >
    );
};

export default ProductForm;