import useSWR from "swr";
import { EntityModel, PagedModel } from "hateoas-hal-types";
import fetcher, { stringifyUrl } from "./fetcher";
import MatchMutate from "../../models/common/MatchMutate";
import UniqueValueRequest from "../../models/common/UniqueValueRequest";
import User from "../../models/user/User";
import UserCreateRequest from "../../models/user/UserCreateRequest";
import UserSearchCriteria from "../../models/user/UserSearchCriteria";
import UserUpdateRequest from "../../models/user/UserUpdateRequest";

const baseUrl = "/api/v1/users";

const reloadUsers = async (mutate: MatchMutate) =>
  await mutate(new RegExp(baseUrl));

export const createUser = async (
  request: UserCreateRequest,
  mutate: MatchMutate
) =>
  await fetcher
    .post(baseUrl, {
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

export const useCurrentUser = async () => useSWR<User>(`${baseUrl}/current`);

export const getUsersByCriteria = async (
  criteria: UserSearchCriteria
): Promise<PagedModel<User>> =>
  await fetcher.get(stringifyUrl(baseUrl, criteria));

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
