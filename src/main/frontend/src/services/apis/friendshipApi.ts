import fetcher from "./fetcher";
import { reloadUsers } from "./userApi";
import MatchMutate from "../../models/common/MatchMutate";
import User from "../../models/user/User";

export const addFriend = async (friend: User, mutate: MatchMutate) =>
  friend._links?.addFriend?.href &&
  (await fetcher
    .post(friend._links.addFriend.href)
    .then(() => reloadUsers(mutate)));

export const removeFriend = async (friend: User, mutate: MatchMutate) =>
  friend._links?.removeFriend?.href &&
  (await fetcher
    .delete(friend._links.removeFriend.href)
    .then(() => reloadUsers(mutate)));
