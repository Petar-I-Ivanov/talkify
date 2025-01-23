import useSWR from "swr";
import useSWRInfinite from "swr/infinite";
import { EntityModel, PagedModel } from "hateoas-hal-types";
import fetcher, { stringifyUrl } from "./fetcher";
import MatchMutate from "~/models/common/MatchMutate";
import Channel from "~/models/channel/Channel";
import UniqueValueRequest from "~/models/common/UniqueValueRequest";
import ChannelCreateUpdateRequest from "~/models/channel/ChannelCreateUpdateRequest";
import ChannelSearchCriteria from "~/models/channel/ChannelSearchCriteria";

export const channelBaseUrl = "/api/v1/channels";

export const reloadChannels = async (mutate: MatchMutate) =>
  await mutate(new RegExp(channelBaseUrl));

export const createChannel = async (
  request: ChannelCreateUpdateRequest,
  mutate: MatchMutate
) =>
  await fetcher
    .post(channelBaseUrl, {
      body: JSON.stringify(request),
    })
    .then(() => reloadChannels(mutate));

export const getChannelsExistsByName = async (
  request: UniqueValueRequest
): Promise<boolean> =>
  await fetcher.get(stringifyUrl(`${channelBaseUrl}/exists/name`, request));

export const getChannelById = async (id: number): Promise<Channel> =>
  await fetcher.get(`${channelBaseUrl}/${id}`);

export const getChannelsByCriteria = async (
  criteria: ChannelSearchCriteria
): Promise<PagedModel<Channel>> =>
  await fetcher.get(stringifyUrl(channelBaseUrl, criteria));

export const useChannelsForInfiniteScrolling = (
  criteria: ChannelSearchCriteria
) =>
  useSWRInfinite<PagedModel<Channel>>(
    (page, previousPageData) =>
      previousPageData && !previousPageData._embedded
        ? null
        : stringifyUrl(channelBaseUrl, { ...criteria, page }),
    fetcher.get,
    { keepPreviousData: true }
  );

export const useChannelsByCriteria = (criteria: ChannelSearchCriteria) => {
  const { data, error, isLoading } = useSWR(
    stringifyUrl(channelBaseUrl, criteria)
  );
  return {
    data: (data?._embedded?.channels ?? []) as EntityModel<Channel>[],
    error,
    isLoading,
  };
};

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
