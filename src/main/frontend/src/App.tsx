import { SWRConfig } from "swr";
import { BrowserRouter, Outlet, Route, Routes } from "react-router-dom";
import fetcher from "./services/apis/fetcher";
import SignInPage from "./pages/login-register/SignInPage";
import SignUpPage from "./pages/login-register/SignUpPage";

const App = () => (
  <BrowserRouter>
    <Routes>
      <Route path="/sign-in" element={<SignInPage />} />
      <Route path="/sign-up" element={<SignUpPage />} />
      <Route path="/" element={<Root />}></Route>
    </Routes>
  </BrowserRouter>
);

const Root = () => (
  <AppConfig>
    <Outlet />
  </AppConfig>
);

const AppConfig: React.FC<{ children: React.ReactNode }> = ({ children }) => (
  <SWRConfig value={{ fetcher: fetcher.get }}>{children}</SWRConfig>
);

export default App;
