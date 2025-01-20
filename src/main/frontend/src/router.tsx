import { lazy, Suspense } from "react";
import { createBrowserRouter } from "react-router";

const App = lazy(() => import("./App"));
const SignInPage = lazy(() => import("./pages/login/SignInPage"));
const SignUpPage = lazy(() => import("./pages/register/SignUpPage"));
const HomePage = lazy(() => import("./pages/home/HomePage"));

const LazyComponent: React.FC<{ component: React.ComponentType }> = ({
  component: Component,
}) => (
  <Suspense fallback={<></>}>
    <Component />
  </Suspense>
);

const router = createBrowserRouter([
  {
    path: "/",
    element: <LazyComponent component={App} />,
    children: [
      {
        path: "/",
        element: <LazyComponent component={HomePage} />,
      },
      {
        path: "/sign-in",
        element: <LazyComponent component={SignInPage} />,
      },
      {
        path: "/sign-up",
        element: <LazyComponent component={SignUpPage} />,
      },
    ],
  },
]);

export default router;
