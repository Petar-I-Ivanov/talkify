import { useForm } from "react-hook-form";
import ChannelSearchCriteria from "../../models/channel/ChannelSearchCriteria";
import { useChannelsForInfiniteScrolling } from "../../services/apis/channelApi";
import { Form, ListGroup } from "react-bootstrap";
import InfiniteScroll from "../../components/InfiniteScroll";
import { useSelectedChannelId } from "../../services/utils/useSelectedChannelId";

const ChannelsListPanel = () => {
  const { channelId, setChannelId } = useSelectedChannelId();
  const { watch, register } = useForm<ChannelSearchCriteria>({
    defaultValues: {
      active: true,
      page: 0,
      size: 7,
    },
  });
  const channels = useChannelsForInfiniteScrolling(watch());

  return (
    <>
      <h4>Channels list</h4>
      <Form.Control
        className="mb-2"
        {...register("name")}
        placeholder="search"
      />

      <div style={{ height: "70%", overflow: "auto", scrollbarWidth: "thin" }}>
        <InfiniteScroll
          swr={channels}
          emptyIndicator={<p>No channels to show</p>}
          isAll={(swr, page) =>
            (swr?.data?.[swr?.data?.length - 1]?.page.totalPages ?? 0) <= page
          }
          isEmpty={(swr) =>
            !!swr.data?.[0] && swr.data[0].page.totalElements === 0
          }
        >
          {(response, index) => (
            <ListGroup key={index}>
              {response._embedded?.channels.map((channel) => (
                <ListGroup.Item
                  key={channel.id}
                  style={{
                    cursor: "pointer",
                    ...(channel.id === channelId
                      ? { backgroundColor: "lightgray" }
                      : {}),
                  }}
                  onClick={() =>
                    channel.id === channelId
                      ? setChannelId(undefined)
                      : setChannelId(channel.id)
                  }
                >
                  {channel.name}
                </ListGroup.Item>
              ))}
            </ListGroup>
          )}
        </InfiniteScroll>
      </div>
    </>
  );
};

export default ChannelsListPanel;
