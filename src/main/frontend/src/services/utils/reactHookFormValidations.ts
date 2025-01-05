import { EMAIL_PATTERN, PASSWORD_PATTERN } from "./constants";

export const REQUIRED_MSG = "This field is required!";
export const minLength = (min: number) => ({
  message: `Minimum length is ${min}!`,
  value: min,
});
export const maxLength = (max: number) => ({
  message: `Maximum length is ${max}!`,
  value: max,
});
export const isValidEmail = () => ({
  message: "Invalid email address!",
  value: EMAIL_PATTERN,
});
export const isValidPassword = () => ({
  message:
    "Password must contain atleast 1 upper, lower case and number or symbol!",
  value: PASSWORD_PATTERN,
});
