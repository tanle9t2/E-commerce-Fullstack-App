import { InputLabel, MenuItem, Select } from "@mui/material";
import Input from "../../ui/Input";
import React, { useEffect, useState } from "react";
import styled from "styled-components";
import {useUser} from"./useUser";
import Spinner from "../../ui/Spinner"


const ProfileContent = styled.div`
  flex: 1;
  background: white;
  padding: 20px;
  border-radius: 8px;
  display:flex;
  align-items:center;
  justify-content:space-around;
`;

const Title = styled.h2`
  margin-bottom: 5px;
`;

const SubTitle = styled.p`
  font-size: 14px;
  color: gray;
  margin-bottom: 20px;
`;


const Label = styled.label`
  font-weight: bold;
  display: block;
  margin: 10px 0 5px;
`;

const RadioGroup = styled.div`
  display: flex;
  gap: 15px;
  margin-bottom: 10px;
`;

const RadioInput = styled.input`
  margin-right: 5px;
`;


const SaveButton = styled.button`
  background-color: #ff4d4f;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;

  &:hover {
    background-color: #e04345;
  }
  margin:20px 0;
`;

const ProfileImageContainer = styled.div`
  text-align: center;
`;

const ProfileImage = styled.img`
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin-bottom: 10px;
`;

const UploadButton = styled.label`
  display: block;
  margin: auto;
  padding: 8px 14px;
  background: var(--white-color);
  border: 1px solid var(--line-color);
  border-radius: 5px;
  cursor: pointer;
`;
const ProfileInfor = styled.div`
    display:flex;
    flex-direction:column;
    flex:0.7;
`
const FileInput = styled.input`
  display: none;
`;

const Profile = () => {
  const [name, setName] = useState("");
  const [gender, setGender] = useState("");
  const [dob, setDob] = useState({ day: "", month: "", year: "" });
  const [image, setImage] = useState("");
  const {isLoading,user} = useUser();
  useEffect(() => {
    if(!isLoading) {
      const {firstName,username,lastName,email,avtUrl,phoneNumber} = user;
      setName(`${firstName} ${lastName}`)
      setImage(avtUrl)
    }
  },[isLoading,setImage,setName,user])
  if(isLoading) return <Spinner/>
  const {firstName,username,lastName,email,avtUrl,phoneNumber} = user;
  const handleImageUpload = (event) => {
    const file = event.target.files[0];
    if (file) {
      const imageUrl = URL.createObjectURL(file);
      setImage(imageUrl);
    }
  };
  return (
      <ProfileContent>
        <ProfileInfor>  
        <Title>Hồ Sơ Của Tôi</Title>
        <SubTitle>Quản lý thông tin hồ sơ để bảo mật tài khoản</SubTitle>

        <Label>Tên đăng nhập</Label>
        <Input value={username} disabled />

        <Label>Tên</Label>
        <Input
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Nhập tên của bạn"
        />

        <Label>Email</Label>
        <Input value={email}  />
        

        <Label>Số điện thoại</Label>
        <Input value={phoneNumber} />
        <Label>Giới tính</Label>
        <RadioGroup>
          <label>
            <RadioInput
              type="radio"
              name="gender"
              value="Nam"
              checked={gender === "Nam"}
              onChange={(e) => setGender(e.target.value)}
            />
            Nam
          </label>
          <label>
            <RadioInput
              type="radio"
              name="gender"
              value="Nữ"
              checked={gender === "Nữ"}
              onChange={(e) => setGender(e.target.value)}
            />
            Nữ
          </label>
          <label>
            <RadioInput
              type="radio"
              name="gender"
              value="Khác"
              checked={gender === "Khác"}
              onChange={(e) => setGender(e.target.value)}
            />
            Khác
          </label>
        </RadioGroup>

        <div>
        <Label>Ngày sinh</Label>
        <Select
          displayEmpty
          inputProps={{ 'aria-label': 'Without label' }}
          MenuProps={{
            PaperProps: {
              style: {
                maxHeight: 200,  // Adjust the height as needed
                overflowY: "auto",  // Add a scrollbar if the content exceeds the maxHeight
              },
            },
          }}
          sx={{ fontSize: "16px",width:"25%",height:"40px",marginRight:"20px" }}
        >
          {[...Array(31)].map((_, i) => (
            <MenuItem  sx={{ fontSize: "16px" }} value={i}> {i + 1}</MenuItem>
          ))}
        </Select>
        <Select
          displayEmpty
          inputProps={{ 'aria-label': 'Without label' }}
          MenuProps={{
            PaperProps: {
              style: {
                maxHeight: 200,  // Adjust the height as needed
                overflowY: "auto",  // Add a scrollbar if the content exceeds the maxHeight
              },
            },
          }}
          sx={{ fontSize: "16px",width:"25%",height:"40px",marginRight:"20px" }}
        >
          {[...Array(12)].map((_, i) => (
            <MenuItem  sx={{ fontSize: "16px" }} value={i}> Tháng {i + 1}</MenuItem>
          ))}
        </Select>
        <Select
          displayEmpty
          inputProps={{ 'aria-label': 'Without label' }}
          MenuProps={{
            PaperProps: {
              style: {
                maxHeight: 200,  // Adjust the height as needed
                overflowY: "auto",  // Add a scrollbar if the content exceeds the maxHeight
              },
            },
          }}
          sx={{ fontSize: "16px",width:"25%",height:"40px",marginRight:"20px" }}
        >
          {[...Array(50)].map((_, i) => (
            <MenuItem  sx={{ fontSize: "16px" }} value= {2025 - i}>  {2025 - i}</MenuItem>
          ))}
        </Select>

        </div>
        <SaveButton>Lưu</SaveButton>
        </ProfileInfor>
        <ProfileImageContainer>
            <ProfileImage
            src={image}
            alt="Profile Avatar"
            />
            <UploadButton type="file">Chọn Ảnh
                <FileInput type="file" accept="image/*" onChange={(e) => handleImageUpload(e)}/>
            </UploadButton>
        </ProfileImageContainer>
      </ProfileContent>

  );
};

export default Profile;
