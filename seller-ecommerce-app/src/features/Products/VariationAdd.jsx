import styled from "styled-components";
import Input from "../../ui/Input";
import { red } from "@mui/material/colors";



const Section = styled.div`
  background-color: #fff;
  padding: 20px;
  margin-bottom: 20px;
  border: 1px solid #ddd;
  border-radius: 4px;
`;

const SectionTitle = styled.h3`
  margin-top: 0;
  color: #333;
  font-size: 16px;
  margin-bottom: 10px;
`;

const InputGroup = styled.div`
  margin-bottom: 15px;
`;

const Label = styled.label`
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
`;



const VariationTable = styled.table`
  width: 100%;
  border-collapse: collapse;
  border: 1px solid #ddd;
  border-radius: 4px;
`;

const TableHeader = styled.th`
  background-color: #f5f5f5;

  border: 1px solid #ddd; /* Border around each header cell */
  text-align: left;
  color: #333;
  width: 20%; /* Each column takes 25% of the table width */
  text-align:center;
`;

const TableRow = styled.tr`
  &:nth-child(even) {
    background-color: #f9f9f9;
  }
  &[data-new-group="true"] {
    border-top: 2px solid #ddd;
  }
`;
const RowSpan = styled.div`
    padding:5px;
     &:not(:last-child) {
    border-bottom: 1px solid var(--color-grey-100);
  } 
`
const TableCell = styled.td`
  padding: 10px 0;
  border: 1px solid #ddd; /* Border around each cell */
  text-align: left;
  width: 20%; /* Each column takes 25% of the table width */
  text-align:center;    
`;

const AddButton = styled.button`
  padding: 8px 16px;
  margin:10px 0;
  background-color: #ee4d2d;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
`;

const VariationGroup = styled.div`

  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: #f9f9f9;
  position: relative;
`;

const InputWrapper = styled.div`
  flex: 1;
  position: relative;
  display:grid;
  grid-template-columns: 0.2fr 0.4fr 0.4fr; 
  align-items:center;
`;
const CloseButton = styled.button`
  position: absolute;
  top: 5px;
  right: 5px;
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
  color: #333;
  z-index:10;
`;

const AddMoreButton = styled(AddButton)`
  margin-top: 10px;
`;
const VariationsInput = styled.div`
    margin:10px 0;
    display:grid;
    grid-template-columns:0.2fr 0.4fr 0.4fr; 
`
const Grid = styled.div`
    margin:10px 0;
    gap: 10px;
    display:grid;
    justify-content: space-between;
    grid-template-columns: 0.5fr 0.5fr;
`
// const initOptionsState = {
//     options: [
//         {
//             "index": 1,
//             "name": "size",
//             "value": ["1", "2"],
//         },
//         {
//             "index": 1,
//             "name": "color",
//             "value": ["blue", "red"],
//         }
//     ],
//     vartions: [

//     ]
// };

function VariationAdd({ register, errors, state, dispatch }) {
    const { options, variations } = state
    const handleChangeNameVariations = (id, value) => {
        dispatch({
            type: "changeName",
            payload: {
                optionIndex: id, value
            }
        })
    };
    const handleChangeVariationValue = (id, index, value) => {
        dispatch({
            type: "changeValue",
            payload: {
                id, index, newValue: value
            }
        })
        if (index === options[id].value.length - 1 && index !== 0) {
            dispatch({
                type: "createVarationValue",
                payload: {
                    id, index
                }
            })
        }
    }
    const handleChangeVariationskuPrice = (index, skuPrice) => {
        console.log(index)
        dispatch({
            type: "changeskuPrice",
            payload: {
                index, newskuPrice: skuPrice
            }
        })
    }
    const handleChangeVariationName = (index, skuNo) => {
        dispatch({
            type: "changeSkuNo",
            payload: {
                index, newSkuNo: skuNo
            }
        })
    }
    const handleChangeVariationskuStock = (index, skuStock) => {
        dispatch({
            type: "changeskuStock",
            payload: {
                index, newskuStock: skuStock
            }
        })
    }

    const handleAddGroup = (e) => {
        e.preventDefault()
        dispatch({
            type: "createVariation"
        })
    };

    const handleRemoveGroup = (e, id) => {
        e.preventDefault();
        dispatch({
            type: "removeOptions",
            payload: {
                id
            }
        })
    }

    return !options.length ?
        (<div>
            <AddButton onClick={(e) => handleAddGroup(e)}>Thêm nhóm phân loại</AddButton>
            <InputGroup>
                <Label required>Giá</Label>
                <Input
                    // onChange={(e) => setskuPrice(e.target.value)}
                    placeholder="Nhập giá"
                    {...register("skuPrice", { required: "Không được để trống ô" })}

                />
                <span style={{ color: "red" }}>{errors?.skuStock?.message || ""}</span>
            </InputGroup>
            <InputGroup>
                <Label required>Kho hàng</Label>
                <Input
                    // value={inventory}
                    // onChange={(e) => setInventory(e.target.value)}
                    placeholder="Nhập số lượng"
                    {...register("skuStock", { required: "Không được để trống ô" })}

                />
                <span style={{ color: "red" }}>{errors?.skuStock?.message || ""}</span>
            </InputGroup>
        </div>)
        :
        (<div>
            <Section>
                <SectionTitle>Phân loại hàng</SectionTitle>
                {options.map(({ name, value }, index) => (
                    <VariationGroup key={index}>
                        <CloseButton onClick={(e) => handleRemoveGroup(e, index)}>X</CloseButton>
                        <InputWrapper>
                            <span>Nhóm phân loại {index}</span>
                            <div>
                                <Input
                                    id={index}
                                    type="text"
                                    placeholder="ví dụ: màu sắc v.v"
                                    value={name && undefined}
                                    onChange={(e) => handleChangeNameVariations(index, e.target.value)}
                                    maxLength={20}
                                />
                                <span style={{ color: "red" }}>{errors?.[`option-${index}`]?.message || ""}</span>
                            </div>
                        </InputWrapper>
                        <VariationsInput>
                            <span>Phân loại hàng</span>
                            <Grid>
                                {
                                    value.map((v, idx) =>
                                        <div>
                                            <Input
                                                id={`${index}-${idx}`}
                                                type="text"
                                                value={v && undefined}
                                                onChange={(e) => handleChangeVariationValue(index, idx, e.target.value)}
                                                maxLength={25}
                                                placeholder="ví dụ: Trắng, Đỏ v.v"
                                            />
                                            <span style={{ color: "red" }}>{errors?.[`${index}-${idx}`]?.message || ""}</span>
                                        </div>
                                    )
                                }
                            </Grid>

                        </VariationsInput>

                    </VariationGroup>
                ))}
                {options.length < 2 && <AddMoreButton onClick={(e) => handleAddGroup(e)}>Thêm nhóm phân loại 2</AddMoreButton>}
            </Section>
            <VariationTable>
                <thead >
                    <tr>
                        {options.map(({ name }, index) => <TableHeader key={index}>{name === "" ? `Nhóm phân loại ${index}` : name}</TableHeader>)}
                        <TableHeader>Giá</TableHeader>
                        <TableHeader> Kho hàng</TableHeader>
                        <TableHeader>SKU phân loại</TableHeader>
                    </tr>
                </thead>
                <tbody>
                    {
                        options[0].value.slice(0, options[0].value.length - 1).map((v0, idx0) => {
                            {/*get start variation for every render new row*/ }
                            let startIdx =
                                options[1] ? idx0 * (Math.min(options[0].value.length - 1, options[1] ? options[1].value.length - 1 : 99)) : idx0

                            return (
                                <TableRow>
                                    <TableCell>{v0}</TableCell>
                                    {options[1] &&
                                        <TableCell> {options[1].value.slice(0, options[1].value.length - 1).map(v1 =>
                                            <RowSpan>{v1}</RowSpan>
                                        )}</TableCell>
                                    }
                                    {/*skuPrice column */}
                                    <TableCell>
                                        {
                                            options[1] ? options[1].value.slice(0, options[1].value.length - 1).map((v1, idx1) => {

                                                return (
                                                    <RowSpan key={`skuPrice-${idx1}-${idx1}`}>
                                                        <Input
                                                            type="text"
                                                            value={variations[startIdx + idx1].skuPrice || ""}
                                                            onChange={(e) => handleChangeVariationskuPrice(startIdx + idx1, e.target.value)}
                                                            placeholder="Nhập vào"
                                                        />
                                                        <span style={{ color: "red" }}>{errors?.[`skuPrice-${startIdx + idx1}`]?.message || ""}</span>
                                                    </RowSpan>
                                                )

                                            }) :
                                                <RowSpan>
                                                    <Input
                                                        type="text"
                                                        value={variations[startIdx].skuPrice || ""}
                                                        onChange={(e) => handleChangeVariationskuPrice(startIdx, e.target.value)}
                                                        placeholder="Nhập vào"
                                                    />
                                                    <span style={{ color: "red" }}>{errors?.[`skuPrice-${startIdx}`]?.message || ""}</span>
                                                </RowSpan>

                                        }
                                    </TableCell>
                                    {/*skuStock column */}
                                    <TableCell>
                                        {
                                            options[1] ? options[1].value.slice(0, options[1].value.length - 1).map((v1, idx1) => {

                                                return (<RowSpan key={`skuStock-${idx1}-${idx1}`}>
                                                    <Input
                                                        type="text"
                                                        value={variations[startIdx + idx1].skuStock || ""}
                                                        onChange={(e) => handleChangeVariationskuStock(startIdx + idx1, e.target.value)}
                                                        placeholder="Nhập vào"
                                                    />
                                                    <span style={{ color: "red" }}>{errors?.[`skuStock-${startIdx + idx1}`]?.message || ""}</span>
                                                </RowSpan>)

                                            }
                                            ) :

                                                <RowSpan>
                                                    <Input
                                                        type="text"
                                                        value={variations[startIdx].skuStock || ""}
                                                        onChange={(e) => handleChangeVariationskuStock(startIdx, e.target.value)}

                                                        placeholder="Nhập vào"
                                                    />
                                                    <span style={{ color: "red" }}>{errors?.[`skuStock-${startIdx}`]?.message || ""}</span>
                                                </RowSpan>

                                        }
                                    </TableCell>
                                    {/*skuNo column */}
                                    <TableCell>
                                        {
                                            options[1] ? options[1].value.slice(0, options[1].value.length - 1).map((v1, idx1) => {

                                                return (

                                                    <RowSpan key={`skuNo-${idx1}-${idx1}`}>
                                                        <Input
                                                            type="text"
                                                            value={variations[startIdx + idx1].skuNo || ""}
                                                            onChange={(e) => handleChangeVariationName(startIdx + idx1, e.target.value)}
                                                            placeholder="Nhập vào"
                                                        />
                                                        <span style={{ color: "red" }}>{errors?.[`skuNo-${startIdx + idx1}`]?.message || ""}</span>
                                                    </RowSpan>

                                                )
                                            }) :
                                                <RowSpan>
                                                    <Input
                                                        type="text"
                                                        value={variations[startIdx].skuNo || ""}
                                                        onChange={(e) => handleChangeVariationName(startIdx, e.target.value)}
                                                        placeholder="Nhập vào"
                                                    />
                                                    <span style={{ color: "red" }}>{errors?.[`skuNo-${startIdx}`]?.message || ""}</span>
                                                </RowSpan>

                                        }
                                    </TableCell >

                                </TableRow >
                            )
                        })
                    }
                </tbody>
            </VariationTable>
        </div >
        )


}

export default VariationAdd
