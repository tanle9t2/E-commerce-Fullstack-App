export const formatCurrencyVND = (amount) => {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND',
    }).format(amount);
};
export const calculateDayDifference =(date) => {
    // Chuyển đổi ngày sang đối tượng Date
        // Chuyển đổi ngày sang đối tượng Date
        const d1 = new Date();
        const d2 = new Date(date);
        // Tính số mili-giây giữa hai ngày
        const differenceInTime = Math.abs(d1-d2);
        // Chuyển đổi mili-giây thành số ngày
        const differenceInDays = Math.ceil(differenceInTime / (1000 * 60 * 60 * 24));
        // Nếu khoảng cách lớn hơn 24 tháng (730 ngày), trả về số năm
        if (differenceInDays > 730) {
            const differenceInYears = Math.floor(differenceInDays / 365); // 365 ngày = 1 năm
            return `${differenceInYears} năm`;
        }
        
        // Nếu khoảng cách lớn hơn 30 ngày, trả về số tháng
        if (differenceInDays > 30) {
            const differenceInMonths = Math.floor(differenceInDays / 30); // 30 ngày = 1 tháng
            return `${differenceInMonths} tháng`;
        }
        
        // Nếu nhỏ hơn hoặc bằng 30 ngày, trả về số ngày
        return `${differenceInDays} ngày`;
}