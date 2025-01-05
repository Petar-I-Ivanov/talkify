import { SWRConfig } from "swr";
import fetcher from "./services/apis/fetcher";
import { Outlet } from "react-router";

const App = () => (
  <AppConfig>
    <Outlet />
  </AppConfig>
);

const AppConfig: React.FC<{ children: React.ReactNode }> = ({ children }) => (
  <SWRConfig value={{ fetcher: fetcher.get }}>{children}</SWRConfig>
);

export default App;
