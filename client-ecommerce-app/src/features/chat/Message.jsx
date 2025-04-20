import styled from "styled-components";

const StyledMessage = styled.div`
display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 16px;
  max-width: 400px;
  margin: auto;
`;

const MessageBubble = styled.p`
display:flex;
   max-width: 70%;
  padding: 10px;
  border-radius: 12px;
  font-size: 14px;
  line-height: 1.5;
  position: relative;
  
  background-color: ${(props) => props.isSender ? "#dcf8c6" : "#fff"};
  margin-left: ${(props) => (props.isSender ? "auto" : "0")};

  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
`;
const TimeStamp = styled.p`
  font-size: 10px;
  color: gray;
  margin-top: 10px;
  margin-left:5px;
`;
function Message({ isSender, content, timestamp }) {
  return (
    <StyledMessage >
      <MessageBubble isSender={isSender}>{content}
        <TimeStamp>{new Date(timestamp).toDateString()}</TimeStamp>
      </MessageBubble>

    </StyledMessage>


  )
}

export default Message
