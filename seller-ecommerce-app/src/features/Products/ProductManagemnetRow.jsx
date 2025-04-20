import styled from "styled-components";
import Table from "../../ui/Table"
import TruncateText from "../../ui/TruncateText";
import { useState } from "react";
const TableCell = styled.div`
  padding: 5px;
  text-align: start;
  width:100%;
`;


const ImageCell = styled(TableCell)`
  display: flex;
  align-items: center;
  text-align:start;
`;
const RowSub = styled.p`
    margin:20px 0px;
`
const Center = styled.div`
    text-align:center;
    margin:20px;
    cursor: pointer;
`
const StyledProductManagemnetRow = styled.div`
      &:not(:last-child) {
    border-bottom: 1px solid var(--color-grey-100);
  }
`
function ProductManagemnetRow({ selectedProducts, handleCheckboxChange, product }) {
    const { name, images, skus } = product
    const [collapItem, setCollapItem] = useState(product.skus.length > 4 ? 4 : product.skus.length);
    return (
        <StyledProductManagemnetRow>
            <Table.Row>
                <TableCell>
                    <input
                        type="checkbox" />
                </TableCell>
                <TableCell>
                    <ImageCell>
                        <img src={images[0].imageUrl} alt={name} width="50" />
                        <div>
                            <TruncateText width={"100px"}>{name}</TruncateText>
                            <p>SKU sản phẩm: {product.sku}</p>
                        </div>
                    </ImageCell>
                </TableCell>
                <TableCell>
                    {skus.slice(0, collapItem).map(({ skuNo }) => <RowSub>{skuNo}</RowSub>)}
                </TableCell>
                <TableCell>
                    {skus.slice(0, collapItem).map(({ modelName }) => <RowSub>{modelName}</RowSub>)}
                </TableCell>
                <TableCell>
                    {skus.slice(0, collapItem).map(({ skuPrice }) => <RowSub>{skuPrice}</RowSub>)}
                </TableCell>
                <TableCell>
                    {skus.slice(0, collapItem).map(({ skuStock }) => <RowSub>{skuStock}</RowSub>)}
                </TableCell>
                <TableCell>
                    {skus.slice(0, collapItem).map(({ skuNo }) => <RowSub>{100}</RowSub>)}
                </TableCell>
                <TableCell>
                    <Center>Cập nhật</Center>
                    <Center>Sao chép</Center>
                    <Center>Xem</Center>
                </TableCell>
            </Table.Row>
            {(collapItem === product.skus.length) && <Center onClick={() => setCollapItem(4)}>Đóng</Center>}
            {collapItem < product.skus.length && <Center onClick={() => setCollapItem(product.skus.length)}>{product.skus.length - collapItem}  phân loại hàng khác</Center>}
        </StyledProductManagemnetRow>

    )
}

export default ProductManagemnetRow
