import React, { useState } from 'react';
import styled from 'styled-components';
import { useProuducts } from './useProducts';
import Spinner from "../../ui/Spinner";
import TruncateText from "../../ui/TruncateText"
import Table from '../../ui/Table';
import ProductManagemnetRow from './ProductManagemnetRow';
import Pagination from "../../ui/Pagination"
import { PAGE_SIZE_PRODUCT } from '../../utils/constant';
import { useNavigate } from 'react-router-dom';
// Styled Components

const MainContent = styled.div`
  flex: 1;
  padding: 20px;
  background-color: #fff;
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
`;

const Title = styled.h2`
  font-size: 18px;
  color: #333;
`;

const Button = styled.button`
  padding: 8px 16px;
  background-color: ${(props) => (props.primary ? '#ee4d2d' : '#fff')};
  color: ${(props) => (props.primary ? '#fff' : '#ee4d2d')};
  border: 1px solid #ee4d2d;
  border-radius: 4px;
  cursor: pointer;
  margin-left: 10px;
`;

const FormSection = styled.div`
  margin-bottom: 20px;
`;

const FormRow = styled.div`
  display: flex;
  gap: 20px;
  margin-bottom: 15px;
`;

const InputGroup = styled.div`
  flex: 1;
`;

const Label = styled.label`
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
`;

const Input = styled.input`
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
`;

const Select = styled.select`
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
`;

const Stats = styled.div`
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  padding: 10px;
  background-color: #f9f9f9;
  border-radius: 4px;
`;

const StatItem = styled.div`
  flex: 1;
  text-align: center;
`;


const TableCell = styled.div`
  flex: ${(props) => props.flex || 1};
  padding: 5px;

`;

const AddButton = styled.button`
display:flex;
    margin-left:auto;
    margin-top: 10px;
    margin-bottom: 10px;
  padding: 8px 16px;
  background-color: #ee4d2d;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;

`;

// React Component
const ProductManagement = () => {
    const [productName, setProductName] = useState('');
    const [category, setCategory] = useState('');
    const [brand, setBrand] = useState('');
    const [price, setPrice] = useState('');
    const [inventory, setInventory] = useState('');
    const { isLoading, products, totalPages } = useProuducts()
    const categories = ['Vui lòng nhập đầy đủ ký tự', 'Đồ gia dụng', 'Thời trang'];
    const brands = ['Thiều', 'Tối đa']
    const [selectedProducts, setSelectedProducts] = useState([]);
    const navigate = useNavigate()
    console.log(isLoading)
    if (isLoading) return <Spinner />
    console.log(products)
    const handleCheckboxChange = (id) => {
        setSelectedProducts((prev) =>
            prev.includes(id) ? prev.filter((item) => item !== id) : [...prev, id]
        );
    };

    const handleSelectAll = (e) => {
        if (e.target.checked) {
            setSelectedProducts(products.map((product) => product.id));
        } else {
            setSelectedProducts([]);
        }
    };


    return (
        <MainContent>
            <Header>
                <Title>Quản lý sản phẩm</Title>

            </Header>

            {/* Form Section */}
            <FormSection>
                <FormRow>
                    <InputGroup>
                        <Label>Tên sản phẩm</Label>
                        <Input
                            value={productName}
                            onChange={(e) => setProductName(e.target.value)}
                            placeholder="Vui lòng nhập đầy đủ ký tự"
                        />
                    </InputGroup>
                </FormRow>

                <FormRow>
                    <InputGroup>
                        <Label>Ngành</Label>
                        <Select
                            value={category}
                            onChange={(e) => setCategory(e.target.value)}
                        >
                            {categories.map((cat, index) => (
                                <option key={index} value={cat}>
                                    {cat}
                                </option>
                            ))}
                        </Select>
                    </InputGroup>
                    <InputGroup>
                        <Label>Thương hiệu</Label>
                        <Select value={brand} onChange={(e) => setBrand(e.target.value)}>
                            {brands.map((br, index) => (
                                <option key={index} value={br}>
                                    {br}
                                </option>
                            ))}
                        </Select>
                    </InputGroup>
                </FormRow>

                <FormRow>
                    <InputGroup>
                        <Label>Doanh số</Label>
                        <Input
                            type="number"
                            value={price}
                            onChange={(e) => setPrice(e.target.value)}
                            placeholder="Tối đa"
                        />
                    </InputGroup>
                    <InputGroup>
                        <Label>Tồn kho</Label>
                        <Input
                            type="number"
                            value={inventory}
                            onChange={(e) => setInventory(e.target.value)}
                            placeholder="Tối đa"
                        />
                    </InputGroup>
                </FormRow>
            </FormSection>

            {/* Stats Section */}
            <Stats>
                <StatItem>Tất cả: 0</StatItem>
                <StatItem>Đang hoạt động: 0</StatItem>
                <StatItem>Hết hàng: 0</StatItem>
                <StatItem>Vi phạm: 0</StatItem>
                <StatItem>Đã ẩn: 0</StatItem>
            </Stats>
            <AddButton onClick={() => navigate('/portal/new')}>Thêm 1 sản phẩm mới</AddButton>
            {/* Table Header */}
            <Table columns="0.05fr 0.2fr 0.1fr 0.2fr 0.15fr 0.1fr 0.15fr 0.1fr">
                <Table.Header>
                    <TableCell>
                        <input
                            type="checkbox"
                            checked={selectedProducts.length === products.length}
                            onChange={handleSelectAll}
                        />

                    </TableCell>
                    <TableCell>Tên sản phẩm</TableCell>
                    <TableCell>SKU phân loại</TableCell>
                    <TableCell>Phân loại hàng</TableCell>
                    <TableCell>Giá</TableCell>
                    <TableCell>Kho hàng</TableCell>
                    <TableCell>Doanh số</TableCell>
                    <TableCell>Thao tác</TableCell>
                </Table.Header>

                <Table.Body
                    data={products}
                    render={(product) => (
                        <ProductManagemnetRow selectedProducts={selectedProducts}
                            handleCheckboxChange={handleCheckboxChange}
                            key={product.id} product={product} />
                    )}
                />
            </Table>

            <Pagination pages={totalPages} />

        </MainContent >
    );
};

export default ProductManagement;