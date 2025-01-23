import { useEffect, useMemo, useRef, useState } from "react";
import { Container } from "react-bootstrap";
import { Button, Input, MessageList, MessageType } from "react-chat-elements";
import { Client } from "@stomp/stompjs";
import useMatchMutate from "~/services/utils/useMatchMutate";
import { useSelectedChannelId } from "~/services/utils/useSelectedChannelId";
import { buildHeaderFromCookie } from "~/services/apis/fetcher";
import {
  createMessage,
  useMessagesForInfiniteScrolling,
} from "~/services/apis/messageApi";
import { useCurrentUser } from "~/services/apis/userApi";
import Message from "~/models/messages/Message";

import "./ChatPreview.css";

const ChatPreview: React.FC<{ channelId: number }> = ({ channelId }) => {
  const { data: currentUser } = useCurrentUser();
  const { data, error, setSize } = useMessagesForInfiniteScrolling(
    channelId
      ? { channelId, page: 0, size: 30, sort: "sentAt,desc" }
      : undefined
  );

  const [latestTexts, setLatestTexts] = useState<Message[]>([]);

  const messageToDatasource = (message: Message): MessageType => ({
    type: "text",
    id: message.id,
    position: currentUser?.username === message.sender ? "right" : "left",
    text: message.text ?? "",
    title: message.sender,
    focus: false,
    date: message.sentAt,
    titleColor: "black",
    forwarded: false,
    replyButton: false,
    removeButton: false,
    status: "read",
    notch: false,
    retracted: false,
  });

  const filterRepeatingMessages = (messages: MessageType[]) => {
    const uniqueMessages = messages.reduce((map, message) => {
      if (message.id !== undefined) {
        map.set(message.id, message);
      }
      return map;
    }, new Map<string | number, MessageType>());

    return Array.from(uniqueMessages.values());
  };

  const messagesToDatasources = (messages?: Message[]): MessageType[] =>
    messages?.map(messageToDatasource) ?? [];

  const concatMessages = (
    firstMessages?: Message[],
    lastMessages?: Message[]
  ): MessageType[] =>
    filterRepeatingMessages([
      ...messagesToDatasources(firstMessages),
      ...messagesToDatasources(lastMessages),
    ]);

  const dataSource = useMemo<MessageType[]>(
    () =>
      concatMessages(
        latestTexts,
        data?.flatMap((d) => d._embedded.messages)
      ).reverse(),
    [data, latestTexts]
  );

  useEffect(() => {
    setLatestTexts([]);
    const client = new Client({
      connectHeaders: buildHeaderFromCookie("X-CSRF-TOKEN"),
      brokerURL: "ws://localhost:8080/ws/websocket",
      reconnectDelay: 5000,
      debug: (str) => console.log(str),
      onConnect: () => {
        console.log("Connected to STOMP WebSocket");

        client.subscribe(`/topic/chat/${channelId}`, async (message) => {
          const body = await JSON.parse(message.body);
          setLatestTexts((prev) => [body, ...prev]);
        });
      },
      onDisconnect: () => console.log("Disconnected from STOMP WebSocket"),
    });

    client.activate();

    return () => {
      client.deactivate();
    };
  }, [channelId]);

  return (
    <Container className="shadow w-100 h-100 pb-3">
      {channelId ? (
        <>
          {error && error?.error?.status === 403 ? (
            <ErrorMessage message="You are not permitted to access the following channel!" />
          ) : (
            <MessagesComponent
              messages={dataSource}
              fetchMore={() => setSize((size) => size + 1)}
            />
          )}
        </>
      ) : (
        <ErrorMessage message="Select channel!" />
      )}
    </Container>
  );
};

const ErrorMessage: React.FC<{ message: string }> = ({ message }) => (
  <div className="w-100 h-100 d-flex">
    <span className="m-auto fw-bolder">{message}</span>
  </div>
);

const MessagesComponent: React.FC<{
  messages: MessageType[];
  fetchMore: () => void;
}> = ({ messages, fetchMore }) => {
  const { channelId } = useSelectedChannelId();
  const mutate = useMatchMutate();
  const listRef = useRef();
  const inputRef = useRef<HTMLInputElement>();
  const [text, setText] = useState<string>();

  const submit = async () => {
    const val = inputRef?.current?.value;
    if (val && val.trim() !== "") {
      await createMessage({ text: val, channelId }, mutate).then(
        () => (inputRef.current!.value = "")
      );
    }
  };

  return (
    <MessageList
      className="message-list flex-column-reverse h-100"
      referance={listRef}
      lockable
      isShowChild
      dataSource={messages}
      onScroll={(e: any) => {
        if (e.target?.scrollTop === 0 && messages.length > 25) {
          fetchMore();
        }
      }}
    >
      <Input
        placeholder="Type here..."
        referance={inputRef}
        multiline
        value={text}
        maxHeight={100}
        onChange={(event: any) => setText(event?.target?.value)}
        onKeyDown={(e) => {
          if (e.key === "Enter" && !e.shiftKey) {
            e.preventDefault();
            submit();
          }
        }}
        rightButtons={<Button text="Send" onClick={submit} />}
      />
    </MessageList>
  );
};

export default ChatPreview;
