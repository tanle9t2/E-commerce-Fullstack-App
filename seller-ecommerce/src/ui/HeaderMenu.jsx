import ListItem from "./ListItem"
import List from "./List"
import User from "../features/authentication/User"
import { HiBell } from "react-icons/hi"
import { useAuthContext } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
function HeaderMenu() {
    const { auth } = useAuthContext();
    const navigate = useNavigate();
    return (
        <div className="flex justify-between flex-1 mb-5">
            <List >
                <ListItem><a href="/">Kênh người bán</a></ListItem>
                <ListItem><a href="/">Tải ứng dụngbán</a></ListItem>
            </List>
            <List>
                <ListItem className="flex items-center"><HiBell /> Thông báo</ListItem>
                <ListItem>
                    {auth
                        ? <User fullName={auth.fullName} avatar={auth.avatar} />
                        : <div>
                            <span className="mr-4 cursor-pointer" onClick={() => navigate("/sign-up")}>Đăng ký</span>
                            <span className="cursor-pointer" onClick={() => navigate("/login")}>Đăng Nhập</span>
                        </div>}
                </ListItem>
            </List>
        </div>
    )
}

export default HeaderMenu
