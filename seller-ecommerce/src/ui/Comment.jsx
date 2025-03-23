import styled from "styled-components"
import Avatar from "./Avatar"
import StartRaing from "./StartRaing"
import Separator from "./Seperator"

const StyledComment = styled.li`
    display:flex;
    padding:10px 0;
    border-bottom: 1px solid var(--line-color);
`
const Content = styled.div`
    
`
function Comment({comment}) {
    const {userInfor,rating,createdAt,skuName,content} = comment;
    return (
        <StyledComment>
            <Avatar src={userInfor.avtUrl}/>
            <div>
                <p>{userInfor.fullName}</p>
                <StartRaing count={rating} color="red"/>
                <p>
                    <span>{createdAt.substring(0, 16)}</span>
                    <Separator/>
                    <span>Phân loại hàng: {skuName}</span>
                </p>
                <p>{content}</p>
           </div>
        </StyledComment>
    )
}

export default Comment
