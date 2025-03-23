import ProductManagement from './features/Products/ProductManagement '
import ProductCreation from "./features/Products/ProductCreation"
import GlobalStyle from "./style/GlobalStyle";
import PageNotFound from "./ui/PageNotFound";
import AppLayout from "./ui/AppLayout";
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { ReactQueryDevtools } from '@tanstack/react-query-devtools'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import { Toaster } from 'react-hot-toast'
import { AuthContextProvider } from './context/AuthContext';
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 60 * 1000,
      // staleTime: 0,
    },
  },
});

function App() {
  return (
    <AuthContextProvider>
      <QueryClientProvider client={queryClient}>
        <ReactQueryDevtools initialIsOpen={false} />
        <GlobalStyle />
        <BrowserRouter>
          <Routes>
            <Route element={<AppLayout />}>
              <Route path="/portal/lists" element={<ProductManagement />} />
              <Route path="/portal/new" element={<ProductCreation />} />

              <Route path="*" element={<PageNotFound />} />
            </Route>


            {/* <Route path="login" element={<Login />} />
          <Route path="sign-up" element={<SignUp />} /> */}
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
    </AuthContextProvider>
  )
}

export default App
