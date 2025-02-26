import styled from "styled-components";

const Section = styled.div`
    background-color:${(props) => (props.bgcolor ? props.bgcolor : 'transparent')};
    display: grid;
    grid-template-columns: repeat(${(props) => (props.columns ? props.columns : 6)}, 1fr);
    margin:50px 0;
    padding:15px;
    border-radius:5px;
    gap: 16px;
`
export default Section;