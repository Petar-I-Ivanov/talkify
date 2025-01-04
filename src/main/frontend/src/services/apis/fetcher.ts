import moment from "moment";
import queryString from "query-string";
import { DATE_ALIKE_PATTERN, DATE_TIME_FORMAT } from "../utils/constants";
import ApiError from "../../models/exceptions/ApiError";
import ApiException from "../../models/exceptions/ApiException";

const fetcherInternal = async (url: string, options: RequestInit = {}) => {
  let { headers, ...rest } = options;
  headers = { ...headers };
  const init = {
    ...rest,
    headers,
  };

  const response = await fetch(url, init);
  if (response.ok) {
    const contentType = response.headers.get("content-type");
    if (!contentType) {
      return;
    }
    if (contentType.includes("text")) {
      return response.text();
    } else {
      return await JSON.parse(await response.text(), (_, value) => {
        // If the value is a string and if it roughly looks like it could be a
        // JSON-style date string go ahead and try to parse it as a Date object.
        if ("string" === typeof value && DATE_ALIKE_PATTERN.test(value)) {
          let date = new Date(value);
          // If the date is valid then go ahead and return the date object.
          if (!isNaN(+date)) {
            return date;
          }
        }
        // If a date was not returned, return the value that was passed in.
        return value;
      });
    }
  }

  if (response.status === 400) {
    const error: ApiError = await response.json();
    throw new ApiException(error);
  }

  throw new Error(await response.text());
};

const fetcher = {
  get: fetcherInternal,
  post: (url: string, options: RequestInit = {}) =>
    fetcherInternal(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json; charset=utf-8",
        ...(options?.headers || {}),
      },
      ...options,
    }),
  patch: (url: string, options: RequestInit = {}) =>
    fetcherInternal(url, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json; charset=utf-8",
        ...(options?.headers || {}),
      },
      ...options,
    }),
  delete: (url: string, options: RequestInit = {}) =>
    fetcherInternal(url, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json;",
        ...(options?.headers || {}),
      },
      ...options,
    }),
  put: (url: string, options: RequestInit = {}) =>
    fetcherInternal(url, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json;",
        ...(options?.headers || {}),
      },
      ...options,
    }),
};

export function replacer(this: any, key: string, value: any) {
  return this[key] instanceof Date
    ? moment(this[key]).format(DATE_TIME_FORMAT)
    : value;
}

export const stringifyUrl = (url: string, query: any) =>
  queryString.stringifyUrl({
    url,
    query: Object.fromEntries(
      Object.entries(query).map((entry) => [
        entry[0],
        entry[1] instanceof Date
          ? moment(entry[1]).format(DATE_TIME_FORMAT)
          : entry[1],
      ])
    ) as any,
  });

export default fetcher;
