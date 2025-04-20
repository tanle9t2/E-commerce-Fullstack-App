import styled from "styled-components";

const TruncateText = styled.p`
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: ${(props => props.width ? props.width : "200px")}; /* Adjust based on layout */
`;
export default TruncateText