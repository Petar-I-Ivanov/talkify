import { useNavigate } from "react-router";
import { Button } from "react-bootstrap";
import useMatchMutate from "~/services/utils/useMatchMutate";
import { registerUser } from "~/services/apis/userApi";
import RegisterUserForm from "./RegisterUserForm";

import background from "~/assets/images/login-background.png";
import "./SignUpPage.css";

const SignUpPage = () => {
  const nav = useNavigate();
  const mutate = useMatchMutate();

  return (
    <div
      className="RegisterPage-Container"
      style={{ backgroundImage: `url(${background})` }}
    >
      <div className="shadow p-3 mb-5 bg-white rounded">
        <RegisterUserForm
          header={
            <div className="RegisterForm-Header">
              <h3 className="text-center">Welcome</h3>
              <div className="d-flex align-items-center justify-content-center">
                <p className="text-center m-0">Already have an account?</p>
                <Button variant="link" onClick={() => nav("/sign-in")}>
                  Sign in
                </Button>
              </div>
            </div>
          }
          onSubmit={(data) =>
            registerUser(data, mutate).finally(() => nav("/sign-in"))
          }
        />
      </div>
    </div>
  );
};

export default SignUpPage;
