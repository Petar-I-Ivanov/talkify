import useSWR from "swr";
import useSWRInfinite from "swr/infinite";
import { EntityModel, PagedModel } from "hateoas-hal-types";
import laggy from "../utils/laggy";
import fetcher, { stringifyUrl } from "./fetcher";
import MatchMutate from "../../models/common/MatchMutate";
import UniqueValueRequest from "../../models/common/UniqueValueRequest";
import Channel from "../../models/channel/Channel";
import ChannelCreateUpdateRequest from "../../models/channel/ChannelCreateUpdateRequest";
import ChannelSearchCriteria from "../../models/channel/ChannelSearchCriteria";
import ChannelMember from "../../models/channel/ChannelMember";
import AddChannelGuestRequest from "../../models/channel/AddChannelGuestRequest";

const baseUrl = "/api/v1/channels";

const reloadChannels = async (mutate: MatchMutate) =>
  await mutate(new RegExp(baseUrl));

export const createChannel = async (
  request: ChannelCreateUpdateRequest,
  mutate: MatchMutate
) =>
  await fetcher
    .post(baseUrl, {
      body: JSON.stringify(request),
    })
    .then(() => reloadChannels(mutate));

export const getChannelsExistsByName = async (
  request: UniqueValueRequest
): Promise<boolean> =>
  await fetcher.get(stringifyUrl(`${baseUrl}/exists/name`, request));

export const useChannelMembers = (id: number) => {
  const { data, error, isLoading } = useSWR(`${baseUrl}/${id}/members`);
  return {
    data: (data?._embedded?.channelMembers ?? []) as ChannelMember[],
    error,
    isLoading,
  };
};

export const getChannelById = async (id: number): Promise<Channel> =>
  await fetcher.get(`${baseUrl}/${id}`);

export const getChannelsByCriteria = async (
  criteria: ChannelSearchCriteria
): Promise<PagedModel<Channel>> =>
  await fetcher.get(stringifyUrl(baseUrl, criteria));

export const useChannelsForInfiniteScrolling = (
  criteria: ChannelSearchCriteria
) =>
  useSWRInfinite<PagedModel<Channel>>(
    (page, previousPageData) =>
      previousPageData && !previousPageData._embedded
        ? null
        : stringifyUrl(baseUrl, { ...criteria, page }),
    fetcher.get,
    { use: [laggy] }
  );

export const useChannelsByCriteria = (criteria: ChannelSearchCriteria) => {
  const { data, error, isLoading } = useSWR(stringifyUrl(baseUrl, criteria));
  return {
    data: (data?._embedded?.channels ?? []) as EntityModel<Channel>[],
    error,
    isLoading,
  };
};

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

export const removeMember = async (
  member: ChannelMember,
  mutate: MatchMutate
) =>
  member?._links?.removeMember?.href &&
  (await fetcher
    .delete(member._links.removeMember.href)
    .then(() => reloadChannels(mutate)));

export const makeAdmin = async (member: ChannelMember, mutate: MatchMutate) =>
  member?._links?.makeAdmin?.href &&
  (await fetcher
    .patch(member._links.makeAdmin.href)
    .then(() => reloadChannels(mutate)));

export const updateChannel = async (
  channel: Channel,
  request: ChannelCreateUpdateRequest,
  mutate: MatchMutate
) =>
  channel?._links?.update?.href &&
  (await fetcher
    .put(channel._links.update.href, {
      body: JSON.stringify(request),
    })
    .then(() => reloadChannels(mutate)));

export const deleteChannel = async (channel: Channel, mutate: MatchMutate) =>
  channel?._links?.delete?.href &&
  (await fetcher
    .delete(channel._links.delete.href)
    .then(() => reloadChannels(mutate)));
