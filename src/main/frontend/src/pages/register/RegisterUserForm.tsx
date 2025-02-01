import { ReactNode } from "react";
import { FormattedMessage, useIntl } from "react-intl";
import { Button, Form } from "react-bootstrap";
import { useForm } from "react-hook-form";
import {
  isConfirmPassMatchPass,
  isEmailTaken,
  isUsernameTaken,
  isValidEmail,
  isValidPassword,
  maxLength,
  minLength,
  requiredMsg,
} from "~/services/utils/reactHookFormValidations";
import UserCreateRequest from "~/models/user/UserCreateRequest";
import "./SignUpPage.css";

const RegisterUserForm: React.FC<{
  header?: ReactNode;
  footer?: ReactNode;
  onSubmit: (request: UserCreateRequest) => void;
}> = ({ header, onSubmit, footer }) => {
  const intl = useIntl();
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
        <Form.Label className="required">
          <FormattedMessage
            id="form.register.username"
            defaultMessage="Username"
          />
        </Form.Label>
        <Form.Control
          {...register("username", {
            required: requiredMsg(intl),
            minLength: minLength(3, intl),
            maxLength: maxLength(64, intl),
            validate: async (value) =>
              value && (await isUsernameTaken(intl, { value })),
          })}
          placeholder={intl.formatMessage({
            id: "form.register.username",
            defaultMessage: "Username",
          })}
        />
        <Form.Text className="error">{errors?.username?.message}</Form.Text>
      </Form.Group>

      <Form.Group>
        <Form.Label className="required">
          <FormattedMessage id="form.register.email" defaultMessage="Email" />
        </Form.Label>
        <Form.Control
          {...register("email", {
            required: requiredMsg(intl),
            pattern: isValidEmail(intl),
            validate: async (value) =>
              value && (await isEmailTaken(intl, { value })),
          })}
          placeholder={intl.formatMessage({
            id: "form.register.email",
            defaultMessage: "Email",
          })}
        />
        <Form.Text className="error">{errors?.email?.message}</Form.Text>
      </Form.Group>

      <Form.Group>
        <Form.Label className="required">
          <FormattedMessage
            id="form.register.password"
            defaultMessage="Password"
          />
        </Form.Label>
        <Form.Control
          {...register("password", {
            required: requiredMsg(intl),
            minLength: minLength(8, intl),
            pattern: isValidPassword(intl),
          })}
          type="password"
          placeholder={intl.formatMessage({
            id: "form.register.password",
            defaultMessage: "Password",
          })}
        />
        <Form.Text className="error">{errors?.password?.message}</Form.Text>
      </Form.Group>

      <Form.Group>
        <Form.Label className="required">
          <FormattedMessage
            id="form.register.confirmPassword"
            defaultMessage="Confirm password"
          />
        </Form.Label>
        <Form.Control
          {...register("confirmPassword", {
            required: requiredMsg(intl),
            validate: (value) =>
              isConfirmPassMatchPass(intl, value, watch("password")),
          })}
          type="password"
          placeholder={intl.formatMessage({
            id: "form.register.confirmPassword",
            defaultMessage: "Confirm password",
          })}
        />
        <Form.Text className="error">
          {errors?.confirmPassword?.message}
        </Form.Text>
      </Form.Group>

      {footer ? (
        <>{footer}</>
      ) : (
        <Button className="w-100" type="submit">
          <FormattedMessage id="form.register.btn" defaultMessage="Register" />
        </Button>
      )}
    </Form>
  );
};

export default RegisterUserForm;
