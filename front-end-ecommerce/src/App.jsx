import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import GlobalStyle from "./style/GlobalStyle";
import AppLayout from "./ui/AppLayout";
import HomePage from "./pages/HomePage";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import Login from "./pages/Login";
import Product from "./features/products/Product";
import Cart from "./features/cart/Cart";
import { CartContextProvider } from "./context/CartContext";
import { Toaster } from "react-hot-toast";
import Profile from "./features/authentication/Profile";
import ProfileLayout from "./ui/ProfileLayout";
import PasswordChange from "./features/authentication/PasswordChange";
import Address from "./features/authentication/Address";
import OrderHistory from "./features/orders/OrderHistory";
import PaymentPage from "./pages/PaymentPage";
import SearchPage from "./pages/SearchPage"
import PageNotFound from "./ui/PageNotFound";
import Tenant from "./features/tenant/Tenant";
import ProtectedRouter from "./ui/ProtectedRouter";
import SignUp from "./pages/SignUp";
import { ChatContextProvider } from "./context/ChatContext";

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 60 * 1000,
      // staleTime: 0
    },
  },
});

function App() {
  return (
    <ChatContextProvider>
      <CartContextProvider>
        <QueryClientProvider client={queryClient}>
          <ReactQueryDevtools initialIsOpen={false} />
          <GlobalStyle />
          <BrowserRouter>
            <Routes>
              <Route element={<AppLayout />}>
                <Route path="/" element={<HomePage />} />
                <Route path="/shop/:shopId" element={<Tenant />} />
                <Route path="search" element={<SearchPage />} />
                <Route path="product/:productId" element={<Product />} />

                <Route path="/user/account/" element={
                  <ProfileLayout />
                }>
                  <Route path="purchase" element={<OrderHistory />} />
                  <Route path="profile" element={<Profile />} />
                  <Route path="password" element={<PasswordChange />} />
                  <Route path="address" element={<Address />} />
                </Route>

                <Route path="*" element={<PageNotFound />} />
              </Route>
              <Route path="cart" element={<ProtectedRouter><Cart /></ProtectedRouter>} />
              <Route path="checkout" element={<ProtectedRouter><PaymentPage /></ProtectedRouter>} />

              <Route path="login" element={<Login />} />
              <Route path="sign-up" element={<SignUp />} />
            </Routes>
          </BrowserRouter>
          <Toaster
            position="top-center"
            gutter={12}
            containerStyle={{ margin: "8px" }}
            toastOptions={{
              success: {
                duration: 3000,
              },
              error: {
                duration: 5000,
              },
              style: {
                fontSize: "16px",
                maxWidth: "500px",
                padding: "16px 24px",
                backgroundColor: "var(--color-grey-0)",
                color: "var(--color-grey-700)",
              },
            }}
          />
        </QueryClientProvider>
      </CartContextProvider>
    </ChatContextProvider>

  );
}

export default App;
