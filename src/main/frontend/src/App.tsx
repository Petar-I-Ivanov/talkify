import { SWRConfig } from "swr";
import fetcher from "./services/apis/fetcher";
import { Outlet } from "react-router";
import useLocale, { LocaleProvider } from "./i18n/LocaleProvider";
import { IntlProvider } from "react-intl";

const App = () => (
  <LocaleProvider>
    <AppConfig>
      <Outlet />
    </AppConfig>
  </LocaleProvider>
);

const AppConfig: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  fetch("/csrf-token");
  const { locale, messages } = useLocale();
  return (
    <IntlProvider locale={locale} messages={messages} defaultLocale="en">
      <SWRConfig value={{ fetcher: fetcher.get }}>{children}</SWRConfig>
    </IntlProvider>
  );
};

export default App;
