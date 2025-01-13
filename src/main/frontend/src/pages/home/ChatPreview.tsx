import { Container } from "react-bootstrap";
import {
  createMessage,
  useMessagesForInfiniteScrolling,
} from "../../services/apis/messageApi";
import { useSelectedChannelId } from "../../services/utils/useSelectedChannelId";
import { Button, Input, MessageList, MessageType } from "react-chat-elements";
import { useMemo, useRef, useState } from "react";
import "./ChatPreview.css";
import useMatchMutate from "../../services/utils/useMatchMutate";

const ChatPreview = () => {
  const { channelId } = useSelectedChannelId();
  const mutate = useMatchMutate();

  const listRef = useRef();
  const inputRef = useRef<HTMLInputElement>();
  const [text, setText] = useState<string>();

  const { data, error } = useMessagesForInfiniteScrolling(
    channelId
      ? { channelId, page: 0, size: 30, sort: "sentAt,desc" }
      : undefined
  );

  const dataSource = useMemo<MessageType[]>(
    () =>
      data?.flatMap((d) =>
        d._embedded.messages.map((message) => ({
          type: "text",
          id: message.id,
          position: message.currentUserSender ? "right" : "left",
          text: message.text ?? "",
          title: "",
          focus: false,
          date: message.sentAt,
          titleColor: "black",
          forwarded: false,
          replyButton: false,
          removeButton: false,
          status: "read",
          notch: false,
          retracted: false,
        }))
      ) ?? [],
    [data]
  );

  return (
    <Container className="shadow w-100 h-100 pb-3">
      {channelId ? (
        <>
          {error && error?.error?.status === 403 ? (
            <ErrorMessage message="You are not permitted to access the following channel!" />
          ) : (
            <>
              {data && data.length > 0 ? (
                <MessageList
                  className="message-list flex-column-reverse h-100"
                  referance={listRef}
                  lockable
                  isShowChild
                  dataSource={dataSource}
                >
                  <Input
                    placeholder="Type here..."
                    referance={inputRef}
                    multiline
                    value={text}
                    maxHeight={100}
                    onChange={(event: any) => setText(event?.target?.value)}
                    rightButtons={
                      <Button
                        text="Send"
                        onClick={async () =>
                          inputRef?.current?.value &&
                          (await createMessage(
                            { text: inputRef.current.value, channelId },
                            mutate
                          ).then(() => (inputRef.current!.value = "")))
                        }
                      />
                    }
                  />
                </MessageList>
              ) : (
                <ErrorMessage message="No messages yet!" />
              )}
            </>
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

export default ChatPreview;
