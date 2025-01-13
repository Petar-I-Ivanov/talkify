import { useState } from "react";
import { useNavigate } from "react-router";
import { Button, Form, InputGroup } from "react-bootstrap";
import { useForm } from "react-hook-form";
import UserLogin from "../../models/user/UserLogin";
import { login } from "../../services/apis/userApi";

import background from "../../assets/images/login-background.png";
import UserIcon from "../../assets/icons/user-icon.svg?react";
import LockIcon from "../../assets/icons/lock-icon.svg?react";
import EyeShowIcon from "../../assets/icons/eye-show-icon.svg?react";
import EyeOffIcon from "../../assets/icons/eye-off-icon.svg?react";
import "./SignInPage.css";

const SignInPage = () => {
  const nav = useNavigate();
  const { register, handleSubmit } = useForm<UserLogin>();
  const [error, setError] = useState(false);
  const [showPass, setShowPass] = useState(false);

  return (
    <div
      className="LoginPage-Container"
      style={{ backgroundImage: `url(${background})` }}
    >
      <div className="shadow p-3 mb-5 bg-white rounded">
        <Form
          className="LoginForm"
          onSubmit={handleSubmit(
            async (data) =>
              await login(data).then((response) => {
                if (!response?.includes("/sign-in?error=true")) {
                  window.location.replace(response);
                  return;
                }

                setError(true);
              })
          )}
        >
          <div className="LoginForm-Header">
            <h3 className="text-center">Welcome Back</h3>
            <div className="d-flex align-items-center justify-content-center">
              <p className="text-center m-0">Don't have an account yet?</p>
              <Button variant="link" onClick={() => nav("/sign-up")}>
                Sign up
              </Button>
            </div>
          </div>

          <Form.Group>
            <InputGroup>
              <InputGroup.Text>
                <UserIcon width={20} height={20} />
              </InputGroup.Text>
              <Form.Control
                {...register("username")}
                placeholder="username or email"
              />
            </InputGroup>
          </Form.Group>

          <Form.Group>
            <InputGroup>
              <InputGroup.Text
                style={{ paddingLeft: "13.5px", paddingRight: "13.5px" }}
              >
                <LockIcon width={17} height={17} />
              </InputGroup.Text>
              <Form.Control
                {...register("password")}
                type={showPass ? "text" : "password"}
                placeholder="password"
              />
              <InputGroup.Text
                style={{ cursor: "pointer" }}
                onClick={() => setShowPass((prev) => !prev)}
              >
                {showPass ? (
                  <EyeShowIcon width={20} />
                ) : (
                  <EyeOffIcon width={20} />
                )}
              </InputGroup.Text>
            </InputGroup>
          </Form.Group>

          {error && (
            <Form.Text className="error">
              No user found with provided credentials
            </Form.Text>
          )}

          <Button className="w-100" type="submit">
            Login
          </Button>
        </Form>
      </div>
    </div>
  );
};

export default SignInPage;
