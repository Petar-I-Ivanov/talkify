import { Form, ListGroup } from "react-bootstrap";
import { useForm } from "react-hook-form";
import InfiniteScroll from "../../components/InfiniteScroll";
import { useUsersForInfiniteScrolling } from "../../services/apis/userApi";
import UserSearchCriteria from "../../models/user/UserSearchCriteria";
import { useSelectedChannelId } from "../../services/utils/useSelectedChannelId";

const FriendsListPanel = () => {
  const { channelId, setChannelId } = useSelectedChannelId();
  const { watch, register } = useForm<UserSearchCriteria>({
    defaultValues: {
      active: true,
      onlyFriends: true,
      page: 0,
      size: 7,
    },
  });
  const users = useUsersForInfiniteScrolling(watch());

  return (
    <>
      <h4>Friends list</h4>
      <Form.Control
        className="mb-2"
        {...register("search")}
        placeholder="search"
      />

      <div style={{ height: "70%", overflow: "auto", scrollbarWidth: "thin" }}>
        <InfiniteScroll
          swr={users}
          emptyIndicator={<p>No friends to show</p>}
          isAll={(swr, page) =>
            (swr?.data?.[swr?.data?.length - 1]?.page.totalPages ?? 0) <= page
          }
          isEmpty={(swr) =>
            !!swr.data?.[0] && swr.data[0].page.totalElements === 0
          }
        >
          {(response, index) => (
            <ListGroup key={index}>
              {response._embedded?.users.map((user) => (
                <ListGroup.Item
                  key={user.id}
                  style={{
                    cursor: "pointer",
                    ...(user.id === channelId
                      ? { backgroundColor: "lightgray" }
                      : {}),
                  }}
                  onClick={() =>
                    user.id === channelId
                      ? setChannelId(undefined)
                      : setChannelId(user.id)
                  }
                >
                  {user.username}
                </ListGroup.Item>
              ))}
            </ListGroup>
          )}
        </InfiniteScroll>
      </div>
    </>
  );
};

export default FriendsListPanel;
