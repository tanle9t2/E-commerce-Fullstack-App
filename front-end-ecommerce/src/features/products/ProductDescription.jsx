import styled from "styled-components"
import Section from "../../ui/Section"
import TextWithLabel from "../../ui/TextWithLabel"
const Title = styled.div`
    font-weight:bold;
    font-size:2rem;
    background-color:var(--color-grey-50);
    padding:20px;
`
const Category= styled.a`
    color: var(--color-blue-700);
`
function ProductDescription({description,stock,category}) {
    const descriptionArray =description.split('\\n')
    console.log(descriptionArray)
    return (
        <Section padding columns ={1} bgcolor={'#fff'}>
            <Title>Chi tiết sản phẩm</Title>
            <TextWithLabel label="Danh mục">
            {category.map(p => <Category href="#">{p} {" > "} </Category>)}    
            </TextWithLabel>
            <TextWithLabel label="Kho">
                    {stock}
            </TextWithLabel>
            <Title>Mô tả sản phẩm</Title>
            {descriptionArray.map(d => <p>{d}</p>)}
        </Section>

    )
}

export default ProductDescription
