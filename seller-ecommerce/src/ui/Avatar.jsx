import styled from "styled-components";

// const Avatar = styled.img`
//   height:  ${(props) => props.type === "user" ? `2.5rem` : `10rem`};
//   width: auto;
//   border-radius:50%;
//   ${(props) => props.type === "tenant" && `border: 1px solid var(--line-color);`}
//   margin-right:10px ;
// `;
// Avatar.defaultProps = {
//   type: "user"
// };
// export default Avatar;

const StyledAvatar = styled.div`
  border-radius: 50%;
  margin:0 5px;
  /* Center the content */
  align-items: center;
  display: flex;
  justify-content: center;
  overflow: hidden;
  /* Size */
  height: ${(props => props.height ? `${props.height}px` : "25px")};
  width: ${(props => props.width ? `${props.width}px` : "none")};
  aspect-ratio: 1 / 1;
  border: ${(props => props.isBorder ? '1px solid black;' : "none")};
`
const Img = styled.img`
  width: 100%;
  height: 100%;
`
function Avatar({ width, height, url, isBorder = false }) {
  return (
    <StyledAvatar width={width} height={height} isBorder={isBorder}>
      <Img src={url} />
    </StyledAvatar>
  )
}

export default Avatar
