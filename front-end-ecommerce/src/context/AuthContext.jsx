import { createContext, useContext } from "react";


import { useLocalStorageState } from "../hook/useLocalStorageState";



const AuthContext = createContext()
function AuthContextProvider({ children }) {
    const [auth, setAuth] = useLocalStorageState(null, "user");


    function handleLogin({ user }) {
        setAuth(user)
    }
    function handleLogout() {
        setAuth(null)
    }
    function updateCurrentUser(data) {
        console.log(data)
        setAuth(prev => ({ ...prev, ...data }))
    }
    return (
        <AuthContext.Provider value={{ updateCurrentUser, handleLogin, handleLogout, auth, setAuth }}>
            {children}
        </AuthContext.Provider>
    );
}
function useAuthContext() {
    const context = useContext(AuthContext);
    if (context === undefined)
        throw new Error("AuthContext was used outside of AuthContextProvider");
    return context;
}
export { AuthContextProvider, useAuthContext }