import { ReactNode } from "react";
import { Button, Form } from "react-bootstrap";
import { useForm } from "react-hook-form";
import {
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
import UserCreateRequest from "../../models/user/UserCreateRequest";
import "./SignUpPage.css";

const RegisterUserForm: React.FC<{
  header?: ReactNode;
  footer?: ReactNode;
  onSubmit: (request: UserCreateRequest) => void;
}> = ({ header, onSubmit, footer }) => {
  const {
    watch,
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<UserCreateRequest>();

  return (
    <Form className="RegisterForm" onSubmit={handleSubmit(onSubmit)}>
      {header}

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

      {footer ? (
        <>{footer}</>
      ) : (
        <Button className="w-100" type="submit">
          Register
        </Button>
      )}
    </Form>
  );
};

export default RegisterUserForm;
