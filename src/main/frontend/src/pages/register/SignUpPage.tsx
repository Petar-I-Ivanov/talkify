import { useNavigate } from "react-router";
import { Button, Form } from "react-bootstrap";
import { useForm } from "react-hook-form";
import useMatchMutate from "../../services/utils/useMatchMutate";
import UserCreateRequest from "../../models/user/UserCreateRequest";
import {
  createUser,
  getUserExistsByEmail,
  getUserExistsByUsername,
} from "../../services/apis/userApi";
import {
  isValidEmail,
  isValidPassword,
  maxLength,
  minLength,
  REQUIRED_MSG,
} from "../../services/utils/reactHookFormValidations";

import background from "../../assets/images/login-background.png";
import "./SignUpPage.css";

const SignUpPage = () => {
  const nav = useNavigate();
  const mutate = useMatchMutate();
  const {
    watch,
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<UserCreateRequest>();

  return (
    <div
      className="RegisterPage-Container"
      style={{ backgroundImage: `url(${background})` }}
    >
      <div className="shadow p-3 mb-5 bg-white rounded">
        <Form
          className="RegisterForm"
          onSubmit={handleSubmit((data) =>
            createUser(data, mutate).then(() => nav("/sign-in"))
          )}
        >
          <div className="RegisterForm-Header">
            <h3 className="text-center">Welcome</h3>
            <div className="d-flex align-items-center justify-content-center">
              <p className="text-center m-0">Already have an account?</p>
              <Button variant="link" onClick={() => nav("/sign-in")}>
                Sign in
              </Button>
            </div>
          </div>

          <Form.Group>
            <Form.Label className="required">Username</Form.Label>
            <Form.Control
              {...register("username", {
                required: REQUIRED_MSG,
                minLength: minLength(3),
                maxLength: maxLength(64),
                validate: async (value) =>
                  (value && !(await getUserExistsByUsername({ value }))) ||
                  "Username is taken!",
              })}
              placeholder="Username"
            />
            <Form.Text className="error">{errors?.username?.message}</Form.Text>
          </Form.Group>

          <Form.Group>
            <Form.Label className="required">Email</Form.Label>
            <Form.Control
              {...register("email", {
                required: REQUIRED_MSG,
                pattern: isValidEmail(),
                validate: async (value) =>
                  (value && !(await getUserExistsByEmail({ value }))) ||
                  "Email is taken!",
              })}
              placeholder="Email"
            />
            <Form.Text className="error">{errors?.email?.message}</Form.Text>
          </Form.Group>

          <Form.Group>
            <Form.Label className="required">Password</Form.Label>
            <Form.Control
              {...register("password", {
                required: REQUIRED_MSG,
                minLength: minLength(8),
                pattern: isValidPassword(),
              })}
              type="password"
              placeholder="Password"
            />
            <Form.Text className="error">{errors?.password?.message}</Form.Text>
          </Form.Group>

          <Form.Group>
            <Form.Label className="required">Confirm password</Form.Label>
            <Form.Control
              {...register("confirmPassword", {
                required: REQUIRED_MSG,
                validate: (value) =>
                  value === watch("password") || "Passwords do not match!",
              })}
              type="password"
              placeholder="Confirm password"
            />
            <Form.Text className="error">
              {errors?.confirmPassword?.message}
            </Form.Text>
          </Form.Group>

          <Button className="w-100" type="submit">
            Register
          </Button>
        </Form>
      </div>
    </div>
  );
};

export default SignUpPage;
