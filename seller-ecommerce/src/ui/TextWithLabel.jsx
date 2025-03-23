import styled from "styled-components";

const Label = styled.label`
    flex:0.2;
     font-weight: 500;
`;
const Content = styled.div`
    display:flex;
    flex: 0.8;
`
const StledTextWithLabel = styled.div`
    display:flex;
    align-items:center;
    margin:20px 0;
`
function TextWithLabel({label,children, handleOnClick}) {
    return (
        <StledTextWithLabel>
            {label && <Label>{label}</Label>}
            <Content>
                {children}
            </Content>
        </StledTextWithLabel>
    )
}

export default TextWithLabel
