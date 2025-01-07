import { HiOutlineStar } from "react-icons/hi";
import styled from "styled-components";
import {formatCurrencyVND} from "../utils/helper"

// Styled Card component
const StyledCard = styled.div`
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  
  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 6px 10px rgba(0, 0, 0, 0.15);
  }
`;
const CardImage = styled.img`
  width: 100%;
  height: 200px;
  object-fit: cover;
`;

const CardContent = styled.div`
  padding: 15px 8px;
  
`;

const CardTitle = styled.p`
  margin: 0;
  font-size: 14px;
  font-weight: bold;
  display: -webkit-box;            /* Enables a flexible box for multi-line text */
  -webkit-line-clamp: 2;           /* Limits the text to 2 lines */
  -webkit-box-orient: vertical;    /* Defines the box direction */
  overflow: hidden;                /* Hides overflow text beyond 2 lines */
  line-height: 1.2;                /* Adjusts line height for better spacing */
  height: 2.4em;                   /* Limits height to two lines (adjust as necessary) */
`;

const CardPrice = styled.p`
  font-size: 14px;
  color: var(--primary-color);
  margin-top: 8px;
`;
const CardExtra = styled.div`
    display:flex;
    align-items:center;

`
function Card({imageUrl,title,price,rating,totalSell,handleOnClick}) {
    return (
        <StyledCard onClick = {handleOnClick}>
            <CardImage src={imageUrl} alt={`image of ${title}`}/>
            <CardContent>
            <CardTitle>{title}</CardTitle>
            <CardPrice>
                {formatCurrencyVND(price)}
            </CardPrice>
            <CardExtra>
               <HiOutlineStar fill="yellow" />
                <span>{rating} | Đã bán {totalSell} </span>
               
            </CardExtra>
            </CardContent>
           
        </StyledCard>
    )
}

export default Card

