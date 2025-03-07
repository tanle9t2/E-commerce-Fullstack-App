import axios from 'axios';


const BASE_URL = `https://provinces.open-api.vn/api`
// const PROVINCE_API = `https://provinces.open-api.vn/api/p/`;
// const DISTRICT_API = `https://provinces.open-api.vn/api/d/`;
// const WARD_API = 'https://provinces.open-api.vn/api/w/';

const openAddressAPI = axios.create({
    baseURL: BASE_URL
})

export const getProvinces = async function () {
    try {
        const res = await openAddressAPI.get("/p/", { withCredentials: false });
        return res.data;

    } catch (error) {
        console.log(error);
    }
};

export const getDistricts = async function (code) {
    try {
        const res = await openAddressAPI.get("/d/", { withCredentials: false });
        return res.data.filter(item => item.province_code === code);
    } catch (error) {
        console.log(error);
    }
};

export const getWards = async function (code) {
    try {
        const res = await openAddressAPI.get("/w/", { withCredentials: false });
        return res.data.filter(item => item.district_code === code);
    } catch (error) {
        console.log(error);
    }
};