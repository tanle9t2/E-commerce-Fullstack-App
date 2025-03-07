import { Button, MenuItem, Select } from "@mui/material";
import Input from "../../ui/Input";
import React, { useEffect, useState, useMemo } from "react";
import styled from "styled-components";
import { useUser } from "./useUser";
import Spinner from "../../ui/Spinner";
import { useUpdateUser } from "./useUpdateUser";
import { splitName, validateEmail } from "../../utils/helper";
import toast from "react-hot-toast";
import { set } from "react-hook-form";
import { useAuthContext } from "../../context/AuthContext";

const ProfileContent = styled.div`
  flex: 1;
  background: white;
  padding: 20px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: space-around;
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
  margin: 20px 0;
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
  padding: 8px 14px;
  background: var(--white-color);
  border: 1px solid var(--line-color);
  border-radius: 5px;
  cursor: pointer;
`;

const ProfileInfor = styled.div`
  display: flex;
  flex-direction: column;
  flex: 0.7;
`;

const ButtonChange = styled.span`
  color: #05a;
  margin-left: 10px;
  text-decoration: underline;
  flex:1;
  cursor: pointer;
`;

const FileInput = styled.input`
  display: none;
`;

const Profile = () => {

  const [name, setName] = useState("");
  const [gender, setGender] = useState(false);
  const [dob, setDob] = useState({ day: "", month: "", year: "" });
  const [changeDob, setChangeDob] = useState(false);
  const [changeEmail, setChangeEmail] = useState(false)
  const [changePhone, setChangePhone] = useState(false);
  const [image, setImage] = useState(null);
  const [file, setFile] = useState(null);
  const [phone, setPhone] = useState("");
  const [email, setEmail] = useState("");

  const { isLoading, user } = useUser();
  const { isLoading: updateLoading, updateUser } = useUpdateUser();
  const days = useMemo(() => [...Array(31)].map((_, i) => i + 1), []);
  const months = useMemo(() => [...Array(12)].map((_, i) => i + 1), []);
  const years = useMemo(() => [...Array(50)].map((_, i) => 2025 - i), []);


  useEffect(() => {
    if (!isLoading && user) {
      const { firstName, lastName, dateOfBirth, email, phoneNumber, sex } = user;
      const dobArrays = dateOfBirth?.split("-") || ["", "", ""];
      setDob({ day: dobArrays[2], month: dobArrays[1], year: dobArrays[0] });
      setName(`${firstName} ${lastName || ""}`);
      setPhone(phoneNumber);
      setGender(sex);
      setEmail(email);
    }
  }, [isLoading, user]);
  if (isLoading || updateLoading) return <Spinner />;

  const handleImageUpload = (event) => {
    const file = event.target.files[0];
    if (file) {
      setFile(file);
      setImage(URL.createObjectURL(file))
    };
  };

  const handleOnClickUpdate = () => {
    if (!validateEmail(email)) {
      toast.error("Email không hợp lệ")
      return;
    }
    const userData = {
      ...splitName(name),
      phoneNumber: phone,
      dob: dob.day ? `${dob.day.toString().padStart(2, "0")}/${dob.month.toString().padStart(2, "0")}/${dob.year}` : null,
      sex: gender,
    };
    updateUser({ userData, image: file }, {
      onSettled: () => {
        setChangeDob(false);
      }
    });
  };

  const renderDropdown = (label, values, value, setter) => (
    <Select
      onChange={(e) => setter((prev) => ({ ...prev, [label]: e.target.value }))}
      displayEmpty
      inputProps={{ "aria-label": "Without label" }}
      MenuProps={{
        PaperProps: {
          style: { maxHeight: 200, overflowY: "auto" },
        },
      }}
      sx={{ fontSize: "16px", width: "25%", height: "40px", marginRight: "20px" }}
    >
      {values.map((val) => (
        <MenuItem key={val} sx={{ fontSize: "16px" }} value={val}>
          {val}
        </MenuItem>
      ))}
    </Select>
  );
  function handleOnChangePhone(e) {
    if (/^\d*$/.test(e.target.value)) {
      setPhone(e.target.value)
    }
  }
  return (
    <ProfileContent>
      <ProfileInfor>
        <Title>Hồ Sơ Của Tôi</Title>
        <SubTitle>Quản lý thông tin hồ sơ để bảo mật tài khoản</SubTitle>
        <Label>Tên đăng nhập</Label>
        <Input value={user.username} disabled />
        <Label>Tên</Label>
        <Input value={name} onChange={(e) => setName(e.target.value)} placeholder="Nhập tên của bạn" />
        <Label>Email</Label>
        {email && !changeEmail ? <div className="flex">
          <span>{email}</span>
          <ButtonChange onClick={() => setChangeEmail(!changeEmail)}>Thay đổi</ButtonChange>
        </div>
          : <>
            <Input value={email} onChange={(e) => { setEmail(e.target.value) }} />
          </>
        }


        <Label>Số điện thoại</Label>
        {phone && !changePhone ? <div className="flex">
          <span>{phone}</span>
          <ButtonChange onClick={() => setChangePhone(!changePhone)}>Thay đổi</ButtonChange>
        </div>
          : <>
            <Input value={phone} onChange={(e) => handleOnChangePhone(e)} />
          </>
        }


        <Label>Giới tính</Label>
        <RadioGroup>
          <label>
            <input type="radio" name="gender" checked={gender === true} onChange={() => setGender(true)} />
            Nam
          </label>
          <label>
            <input type="radio" name="gender" checked={gender === false} onChange={() => setGender(false)} />
            Nữ
          </label>
        </RadioGroup>

        <div>
          <Label>Ngày sinh</Label>
          {dob.day && !changeDob ? (
            <>
              <span>{`${dob.day}/${dob.month}/${dob.year.substring(0, 4)}`}</span>
              <ButtonChange onClick={() => setChangeDob(!changeDob)}>Thay đổi</ButtonChange>
            </>
          ) : (
            <>
              {renderDropdown("day", days, dob.day, setDob)}
              {renderDropdown("month", months, dob.month, setDob)}
              {renderDropdown("year", years, dob.year, setDob)}
            </>
          )}
        </div>

        <SaveButton onClick={handleOnClickUpdate}>Lưu</SaveButton>
      </ProfileInfor>

      <ProfileImageContainer>
        <ProfileImage src={image || user.avtUrl} alt="Profile Avatar" />
        <UploadButton>
          Chọn Ảnh
          <FileInput type="file" accept="image/*" onChange={handleImageUpload} />
        </UploadButton>
      </ProfileImageContainer>
    </ProfileContent>
  );
};

export default Profile;
