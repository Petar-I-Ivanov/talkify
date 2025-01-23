import useSWR from "swr";
import useSWRInfinite from "swr/infinite";
import { EntityModel, PagedModel } from "hateoas-hal-types";
import fetcher, { stringifyUrl } from "./fetcher";
import MatchMutate from "~/models/common/MatchMutate";
import UniqueValueRequest from "~/models/common/UniqueValueRequest";
import User from "~/models/user/User";
import UserLogin from "~/models/user/UserLogin";
import UserCreateRequest from "~/models/user/UserCreateRequest";
import UserSearchCriteria from "~/models/user/UserSearchCriteria";
import UserUpdateRequest from "~/models/user/UserUpdateRequest";

const baseUrl = "/api/v1/users";

export const reloadUsers = async (mutate: MatchMutate) =>
  await mutate(new RegExp(baseUrl));

export const login = async (request: UserLogin) =>
  await fetcher.post("/login", {
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
    },
    body: new URLSearchParams({ ...request }),
  });

export const logout = async () =>
  await fetcher
    .get("/logout")
    .then((response) => window.location.replace(response.url));

export const createUser = async (
  request: UserCreateRequest,
  mutate: MatchMutate
) =>
  await fetcher
    .post(baseUrl, {
      body: JSON.stringify(request),
    })
    .then(() => reloadUsers(mutate));

export const registerUser = async (
  request: UserCreateRequest,
  mutate: MatchMutate
) =>
  await fetcher
    .post(`${baseUrl}/register`, {
      body: JSON.stringify(request),
    })
    .then(() => reloadUsers(mutate));

export const getUserExistsByUsername = async (
  request: UniqueValueRequest
): Promise<boolean> =>
  await fetcher.get(stringifyUrl(`${baseUrl}/exists/username`, request));

export const getUserExistsByEmail = async (
  request: UniqueValueRequest
): Promise<boolean> =>
  await fetcher.get(stringifyUrl(`${baseUrl}/exists/email`, request));

export const getUserById = async (id: number): Promise<User> =>
  await fetcher.get(`${baseUrl}/${id}`);

export const useCurrentUser = () => useSWR<User>(`${baseUrl}/current`);

export const getUsersByCriteria = async (
  criteria: UserSearchCriteria
): Promise<PagedModel<User>> =>
  await fetcher.get(stringifyUrl(baseUrl, criteria));

export const useUsersForInfiniteScrolling = (criteria: UserSearchCriteria) =>
  useSWRInfinite<PagedModel<User>>(
    (page, previousPageData) =>
      previousPageData && !previousPageData._embedded
        ? null
        : stringifyUrl(baseUrl, { ...criteria, page }),
    fetcher.get,
    { keepPreviousData: true }
  );

export const useUsersByCriteria = (criteria: UserSearchCriteria) => {
  const { data, error, isLoading } = useSWR(stringifyUrl(baseUrl, criteria));
  return {
    data: (data?._embedded?.users ?? []) as EntityModel<User>[],
    error,
    isLoading,
  };
};

export const updateUser = async (
  user: User,
  request: UserUpdateRequest,
  mutate: MatchMutate
) =>
  user?._links?.update?.href &&
  (await fetcher
    .put(user._links.update.href, {
      body: JSON.stringify(request),
    })
    .then(() => reloadUsers(mutate)));

export const deleteUser = async (user: User, mutate: MatchMutate) =>
  user?._links?.delete?.href &&
  (await fetcher
    .delete(user._links.delete.href)
    .then(() => reloadUsers(mutate)));
