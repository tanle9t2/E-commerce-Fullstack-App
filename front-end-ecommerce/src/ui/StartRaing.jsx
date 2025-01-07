import { CiStar } from "react-icons/ci";
import { FaStar } from "react-icons/fa";
import styled from "styled-components";
const StyledStarRaing = styled.div`
    display:flex;
    margin-left:10px;
`
function StartRaing({count}) {
    const starsOutline = Array(5-count).fill(<CiStar/>);
    const startsRaing = Array(count).fill(<FaStar fill="yellow"/>    )
    return (
      <StyledStarRaing>
        {startsRaing.map((star, index) => (
          <span key={index}>{star}</span>
        ))}
          {starsOutline.map((star, index) => (
          <span key={index}>{star}</span>
        ))}
      </StyledStarRaing>
    );
}

export default StartRaing
