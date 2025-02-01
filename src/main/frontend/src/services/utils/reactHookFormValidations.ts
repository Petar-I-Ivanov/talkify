import { IntlShape } from "react-intl";
import { EMAIL_PATTERN, PASSWORD_PATTERN } from "./constants";
import { getUserExistsByEmail, getUserExistsByUsername } from "../apis/userApi";
import UniqueValueRequest from "~/models/common/UniqueValueRequest";

export const requiredMsg = (intl: IntlShape) =>
  intl.formatMessage({
    id: "general.validation.required",
    defaultMessage: "This field is required!",
  });

export const minLength = (min: number, intl: IntlShape) => ({
  message: intl.formatMessage(
    {
      id: "general.validation.minLength",
      defaultMessage: "Minimum length of this field is {min}!",
    },
    { min }
  ),
  value: min,
});

export const maxLength = (max: number, intl: IntlShape) => ({
  message: intl.formatMessage(
    {
      id: "general.validation.maxLength",
      defaultMessage: "Maximum length of this field is {max}!",
    },
    { max }
  ),
  value: max,
});

export const isValidEmail = (intl: IntlShape) => ({
  message: intl.formatMessage({
    id: "general.validation.email",
    defaultMessage: "This field should be email!",
  }),
  value: EMAIL_PATTERN,
});

export const isValidPassword = (intl: IntlShape) => ({
  message: intl.formatMessage({
    id: "general.validation.password",
    defaultMessage:
      "Password must contain atleast 1 upper, lower case and number or symbol!",
  }),
  value: PASSWORD_PATTERN,
});

export const isUsernameTaken = async (
  intl: IntlShape,
  request?: UniqueValueRequest
) =>
  (request?.value &&
    !(await getUserExistsByUsername({ value: request?.value }))) ||
  intl.formatMessage({
    id: "general.validation.usernameTaken",
    defaultMessage: "Username is taken!",
  });

export const isEmailTaken = async (
  intl: IntlShape,
  request?: UniqueValueRequest
) =>
  (request?.value &&
    !(await getUserExistsByEmail({ value: request?.value }))) ||
  intl.formatMessage({
    id: "general.validation.emailTaken",
    defaultMessage: "Email is taken!",
  });

export const isConfirmPassMatchPass = (
  intl: IntlShape,
  confirmPass?: string,
  pass?: string
) =>
  confirmPass === pass ||
  intl.formatMessage({
    id: "general.validation.confirmPass",
    defaultMessage: "Passwords do not match!",
  });
