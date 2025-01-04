import ApiError from "./ApiError";

class ApiException {
  error: ApiError;

  constructor(error: ApiError) {
    this.error = error;
  }
}

export default ApiException;
