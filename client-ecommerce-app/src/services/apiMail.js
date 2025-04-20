import api from "./api";




export async function verifyEmail(toEmail) {
    try {
        const res = await api.post("/email/sendOtp",
            toEmail
        );
        return res.data;
    } catch (error) {
        throw new Error(error.response.data.detail);
    }
}
export async function verifyOtp(otp) {
    try {
        const res = await api.post("/email/verify-otp", otp
        );
        return res.data;
    } catch (error) {
        throw new Error(error.response.data.detail);
    }
}