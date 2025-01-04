import useSWR from "swr";
import { EntityModel, PagedModel } from "hateoas-hal-types";
import fetcher, { stringifyUrl } from "./fetcher";
import MatchMutate from "../../models/common/MatchMutate";
import UniqueValueRequest from "../../models/common/UniqueValueRequest";
import Channel from "../../models/channel/Channel";
import ChannelCreateUpdateRequest from "../../models/channel/ChannelCreateUpdateRequest";
import ChannelSearchCriteria from "../../models/channel/ChannelSearchCriteria";

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

export const getChannelById = async (id: number): Promise<Channel> =>
  await fetcher.get(`${baseUrl}/${id}`);

export const getChannelsByCriteria = async (
  criteria: ChannelSearchCriteria
): Promise<PagedModel<Channel>> =>
  await fetcher.get(stringifyUrl(baseUrl, criteria));

export const useChannelsByCriteria = (criteria: ChannelSearchCriteria) => {
  const { data, error, isLoading } = useSWR(stringifyUrl(baseUrl, criteria));
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
