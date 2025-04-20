import React, { useRef, useState } from "react";
import styled from "styled-components";
import Input from "./Input";
import { useProvices } from "../features/authentication/useProvices";
import { useDistrct } from "../features/authentication/useDistrict";
import { useWard } from "../features/authentication/useWard";
import Spinner from "./Spinner";

const DropdownContainer = styled.div`
  position: relative;
  border-radius: 5px;
  font-family: Arial, sans-serif;
`;

const Tabs = styled.div`
  display: flex;
  border-bottom: 1px solid #ccc;
`;

const Tab = styled.div`
  flex: 1;
  text-align: center;
  padding: 10px;
  font-size: 14px;
  cursor: ${({ canClick }) => (canClick ? "pointer" : "not-allowed")};
  color: ${({ active }) => (active ? "red" : "#666")};
  border-bottom: ${({ active }) => (active ? "2px solid red" : "none")};
  font-weight: ${({ active }) => (active ? "bold" : "normal")};
`;

const DropdownList = styled.ul`
  max-height: 200px;
  overflow-y: auto;
  list-style: none;
  padding: 0;
  margin: 0;
  border-top: 1px solid #ccc;
`;

const DropdownItem = styled.li`
  padding: 10px;
  cursor: pointer;
  &:hover {
    background: #f0f0f0;
  }
`;

const DropDown = ({ isOpen, setIsOpen, state, handleOnChange }) => {
  const [activeTab, setActiveTab] = useState(new Set()); // Prevent duplicate entries
  const [searchTerm, setSearchTerm] = useState("");

  const { isLoading, provinces } = useProvices();
  const [currentTab, setCurrentTab] = useState("city");
  const { isLoading: loadingDistricts, getDistrict } = useDistrct();
  const { isLoading: loadingWard, getWard } = useWard();
  const [districtValue, setDistrictValue] = useState([]);
  const [wardValue, setWardValue] = useState([]);
  if (isLoading || loadingDistricts || loadingWard) return <Spinner />;

  function getDataActiveTab() {
    if (currentTab === "city") {
      return provinces?.filter((item) =>
        item.name.toLowerCase().includes(searchTerm.toLowerCase())
      );
    } else if (currentTab === "district") {
      return districtValue?.filter((item) =>
        item.name.toLowerCase().includes(searchTerm.toLowerCase())
      );
    } else if (currentTab === "ward") {
      return wardValue?.filter((item) =>
        item.name.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }
    return [];
  }

  function handleOnChangeCurrent(code, name) {
    if (currentTab === "city") {
      getDistrict(code, {
        onSuccess: (data) => {
          setDistrictValue(data)
          handleOnChange({ "city": name })
          setActiveTab((prev) => {
            const newTabs = new Set(prev);
            newTabs.add("city");
            return newTabs;
          });
        },
      });
      setCurrentTab("district");
    } else if (currentTab === "district") {
      getWard(code, {
        onSuccess: (data) => {
          setWardValue(data)
          handleOnChange({ "district": name })
          setActiveTab((prev) => {
            const newTabs = new Set(prev);
            newTabs.add("district");
            return newTabs;
          });
        },
      });
      setCurrentTab("ward");
    } else if (currentTab === "ward") {
      handleOnChange({ "ward": name })
      setActiveTab((prev) => {
        const newTabs = new Set(prev);
        newTabs.add("ward");
        return newTabs;
      });
      setIsOpen(false)
    }
  }

  function handleOnClickProvince() {
    setActiveTab((prev) => {
      const newTabs = new Set(prev);
      newTabs.add("city");
      return newTabs;
    });
    setCurrentTab("city");
  }

  function handleOnClickDistrict() {
    if (activeTab.has("city") && currentTab ==="district"){
      setActiveTab((prev) => {

        const newTabs = new Set(prev);
        newTabs.add("district");
        return newTabs;
      }
      );
    setCurrentTab("district");}
  }

  function handleOnClickWard() {
    if (activeTab.has("district")&&currentTab ==="ward") {
      setActiveTab((prev) => {

        const newTabs = new Set(prev);
        newTabs.add("ward");
        return newTabs;

      });
      setCurrentTab("ward");
    }
  }


  return (
    <DropdownContainer>

      {isOpen && (
        <>
          <Tabs>
            <Tab
              canClick
              active={currentTab === "city"}
              onClick={handleOnClickProvince}
            >
              Tỉnh/Thành phố
            </Tab>
            <Tab
              canClick={activeTab.has("city")}
              active={currentTab === "district"}
              onClick={handleOnClickDistrict}
            >
              Quận/Huyện
            </Tab>
            <Tab
              canClick={activeTab.has("district")}
              active={currentTab === "ward"}
              onClick={handleOnClickWard}
            >
              Phường/Xã
            </Tab>
          </Tabs>

          <DropdownList>
            {getDataActiveTab().map((item) => (
              <DropdownItem
                key={item.code}
                onClick={() => handleOnChangeCurrent(item.code, item.name)}
              >
                {item.name}
              </DropdownItem>
            ))}
          </DropdownList>
        </>
      )}
    </DropdownContainer>
  );
};

export default DropDown;
