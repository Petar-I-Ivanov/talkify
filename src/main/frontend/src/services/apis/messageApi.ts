import useSWR from "swr";
import useSWRInfinite from "swr/infinite";
import { EntityModel, PagedModel } from "hateoas-hal-types";
import fetcher, { stringifyUrl } from "./fetcher";
import MatchMutate from "~/models/common/MatchMutate";
import Message from "~/models/messages/Message";
import MessageCreateRequest from "~/models/messages/MessageCreateRequest";
import MessageUpdateRequest from "~/models/messages/MessageUpdateRequest";
import MessageSearchCriteria from "~/models/messages/MessageSearchCriteria";

const baseUrl = "/api/v1/messages";

const reloadMessages = async (mutate: MatchMutate) =>
  await mutate(new RegExp(baseUrl));

export const createMessage = async (
  request: MessageCreateRequest,
  mutate: MatchMutate
) =>
  await fetcher
    .post(baseUrl, {
      body: JSON.stringify(request),
    })
    .then(() => reloadMessages(mutate));

export const getMessageById = async (id: number): Promise<Message> =>
  await fetcher.get(`${baseUrl}/${id}`);

export const getMessagesByCriteria = async (
  criteria: MessageSearchCriteria
): Promise<PagedModel<Message>> =>
  await fetcher.get(stringifyUrl(baseUrl, criteria));

export const useMessagesForInfiniteScrolling = (
  criteria?: MessageSearchCriteria
) =>
  useSWRInfinite<PagedModel<Message>>(
    (page, previousPageData) =>
      (previousPageData && !previousPageData._embedded) || !criteria?.channelId
        ? null
        : stringifyUrl(baseUrl, { ...criteria, page }),
    fetcher.get,
    { keepPreviousData: true }
  );

export const useMessagesByCriteria = (criteria?: MessageSearchCriteria) => {
  const { data, error, isLoading } = useSWR(
    criteria && stringifyUrl(baseUrl, criteria)
  );
  return {
    data: (data?._embedded?.messages ?? []) as EntityModel<Message>[],
    error,
    isLoading,
  };
};

export const updatMessage = async (
  message: Message,
  request: MessageUpdateRequest,
  mutate: MatchMutate
) =>
  message?._links?.update?.href &&
  (await fetcher
    .put(message._links.update.href, {
      body: JSON.stringify(request),
    })
    .then(() => reloadMessages(mutate)));

export const deleteMessage = async (message: Message, mutate: MatchMutate) =>
  message?._links?.delete?.href &&
  (await fetcher
    .delete(message._links.delete.href)
    .then(() => reloadMessages(mutate)));
