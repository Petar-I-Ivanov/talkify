import useSWR from "swr";
import fetcher from "./fetcher";
import { channelBaseUrl, reloadChannels } from "./channelApi";
import MatchMutate from "~/models/common/MatchMutate";
import Channel from "~/models/channel/Channel";
import ChannelMember from "~/models/channel/member/ChannelMember";
import AddChannelGuestRequest from "~/models/channel/member/AddChannelGuestRequest";

export const addChannelMember = async (
  channel: Channel,
  request: AddChannelGuestRequest,
  mutate: MatchMutate
) =>
  channel?._links?.addMember?.href &&
  request.userId &&
  (await fetcher
    .post(channel._links.addMember.href, { body: JSON.stringify(request) })
    .then(() => reloadChannels(mutate)));

export const useChannelMembers = (id: string) => {
  const { data, error, isLoading } = useSWR(`${channelBaseUrl}/${id}/members`);
  return {
    data: (data?._embedded?.channelMembers ?? []) as ChannelMember[],
    error,
    isLoading,
  };
};

export const makeAdmin = async (member: ChannelMember, mutate: MatchMutate) =>
  member?._links?.makeAdmin?.href &&
  (await fetcher
    .patch(member._links.makeAdmin.href)
    .then(() => reloadChannels(mutate)));

export const removeMember = async (
  member: ChannelMember,
  mutate: MatchMutate
) =>
  member?._links?.removeMember?.href &&
  (await fetcher
    .delete(member._links.removeMember.href)
    .then(() => reloadChannels(mutate)));
