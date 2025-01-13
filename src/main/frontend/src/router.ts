import { createBrowserRouter } from "react-router";
import App from "./App";
import SignInPage from "./pages/login/SignInPage";
import SignUpPage from "./pages/register/SignUpPage";
import HomePage from "./pages/home/HomePage";

const router = createBrowserRouter([
  {
    path: "/",
    Component: App,
    children: [
      {
        path: "/",
        Component: HomePage,
      },
      {
        path: "/sign-in",
        Component: SignInPage,
      },
      {
        path: "/sign-up",
        Component: SignUpPage,
      },
    ],
  },
]);

export default router;
