import ListItem from "./ListItem"
import List from "./List"
import User from "./User"
import { HiBell } from "react-icons/hi"
function HeaderMenu() {
    return (
        <div className="flex justify-between flex-1 mb-5">
            <List >
                <ListItem><a href="/">Kênh người bán</a></ListItem>
                <ListItem><a href="/">Tải ứng dụngbán</a></ListItem> 
            </List>
            <List>
                <ListItem className="flex items-center"><HiBell/> Thông báo</ListItem>
                <ListItem><User/></ListItem> 
            </List>
        </div>
    )
}

export default HeaderMenu
