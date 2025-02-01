import React, { useState } from "react";
import { FormattedMessage, useIntl } from "react-intl";
import { Button, Form, ListGroup, Modal } from "react-bootstrap";
import { useForm } from "react-hook-form";
import IconButton from "~/components/IconButton";
import InfiniteScroll from "~/components/InfiniteScroll";
import RegisterUserForm from "../register/RegisterUserForm";
import { useSelectedChannelId } from "~/services/utils/useSelectedChannelId";
import useMatchMutate from "~/services/utils/useMatchMutate";
import { addFriend, removeFriend } from "~/services/apis/friendshipApi";
import {
  createUser,
  useUsersForInfiniteScrolling,
} from "~/services/apis/userApi";
import UserSearchCriteria from "~/models/user/UserSearchCriteria";

import SearchIcon from "~/assets/icons/search-icon.svg?react";
import BinIcon from "~/assets/icons/bin-icon.svg?react";
import PlusIcon from "~/assets/icons/plus-icon.svg?react";

const FriendsListPanel = () => {
  const intl = useIntl();
  const matchMutate = useMatchMutate();
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
  const [searchUsers, setSearchUsers] = useState(false);
  const [registerFriend, setRegisterFriend] = useState(false);

  return (
    <>
      <div className="d-flex align-items-center justify-content-between">
        <h4>
          <FormattedMessage
            id="page.home.friendsList.title"
            defaultMessage="Friends list"
          />
        </h4>

        <div>
          <IconButton
            icon={SearchIcon}
            tooltipId="UsersSearch"
            placement="bottom"
            tooltip={
              <FormattedMessage
                id="page.home.friendsList.searchUsers"
                defaultMessage="Search all users"
              />
            }
            onClick={() => setSearchUsers(true)}
          />
          <IconButton
            icon={PlusIcon}
            variant="outline-success"
            tooltipId="UserRegister"
            placement="bottom"
            tooltip={
              <FormattedMessage
                id="page.home.friendsList.registerUser"
                defaultMessage="Register friend"
              />
            }
            onClick={() => setRegisterFriend(true)}
          />
        </div>
      </div>
      <Form.Control
        className="mb-2"
        {...register("search")}
        placeholder={intl.formatMessage({
          id: "page.home.friendsList.search.placeholder",
          defaultMessage: "Search",
        })}
      />

      <div style={{ overflow: "auto", scrollbarWidth: "thin" }}>
        <InfiniteScroll
          swr={users}
          emptyIndicator={
            <FormattedMessage
              id="page.home.friendsList.searchUsers.emptyMsg"
              defaultMessage="No friends to show"
            />
          }
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
                    ...(user?.privateChannelId === channelId
                      ? { backgroundColor: "lightgray" }
                      : {}),
                  }}
                  onClick={() =>
                    user?.privateChannelId === channelId
                      ? setChannelId(undefined)
                      : setChannelId(user?.privateChannelId)
                  }
                >
                  <div className="d-flex align-items-center justify-content-between">
                    <span>{user.username}</span>

                    <div onClick={(e) => e.stopPropagation()}>
                      {user._links?.removeFriend?.href && (
                        <IconButton
                          icon={BinIcon}
                          variant="outline-danger"
                          tooltipId="AddFriend"
                          tooltip={
                            <FormattedMessage
                              id="page.home.friendsList.removeFriend"
                              defaultMessage="Remove friend"
                            />
                          }
                          onClick={async () =>
                            await removeFriend(user, matchMutate).then(() =>
                              users.mutate()
                            )
                          }
                        />
                      )}
                    </div>
                  </div>
                </ListGroup.Item>
              ))}
            </ListGroup>
          )}
        </InfiniteScroll>
      </div>

      {searchUsers && (
        <SearchUsersModal onClose={() => setSearchUsers(false)} />
      )}

      {registerFriend && (
        <RegisterUserModal onClose={() => setRegisterFriend(false)} />
      )}
    </>
  );
};

const SearchUsersModal: React.FC<{ onClose: () => void }> = ({ onClose }) => {
  const intl = useIntl();
  const matchMutate = useMatchMutate();
  const { watch, register } = useForm<UserSearchCriteria>({
    defaultValues: {
      active: true,
      page: 0,
      size: 7,
    },
  });
  const users = useUsersForInfiniteScrolling(watch());

  return (
    <Modal show>
      <Modal.Header>
        <Modal.Title>
          <FormattedMessage
            id="page.home.friendsList.searchUsers.inSystem"
            defaultMessage="Search from all the users in the system"
          />
        </Modal.Title>
      </Modal.Header>
      <Modal.Body style={{ height: "20rem" }}>
        <Form.Control
          {...register("search")}
          className="mb-2"
          placeholder={intl.formatMessage({
            id: "page.home.friendsList.search.placeholder",
            defaultMessage: "Search",
          })}
        />
        <div
          style={{ height: "70%", overflow: "auto", scrollbarWidth: "thin" }}
        >
          <InfiniteScroll
            swr={users}
            emptyIndicator={
              <FormattedMessage
                id="page.home.friendsList.searchUsers.emptyMsg"
                defaultMessage="No friends to show"
              />
            }
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
                  <ListGroup.Item key={user.id}>
                    <div className="d-flex align-items-center justify-content-between">
                      <span>{user.username}</span>

                      <div>
                        {user._links?.addFriend?.href && (
                          <IconButton
                            icon={PlusIcon}
                            variant="outline-success"
                            tooltipId="AddFriend"
                            tooltip={
                              <FormattedMessage
                                id="page.home.friendsList.addFriend"
                                defaultMessage="Add friend"
                              />
                            }
                            onClick={async () =>
                              await addFriend(user, matchMutate).then(() =>
                                users.mutate()
                              )
                            }
                          />
                        )}
                        {user._links?.removeFriend?.href && (
                          <IconButton
                            icon={BinIcon}
                            variant="outline-danger"
                            tooltipId="RemoveFriend"
                            tooltip={
                              <FormattedMessage
                                id="page.home.friendsList.removeFriend"
                                defaultMessage="Remove friend"
                              />
                            }
                            onClick={async () =>
                              await removeFriend(user, matchMutate).then(() =>
                                users.mutate()
                              )
                            }
                          />
                        )}
                      </div>
                    </div>
                  </ListGroup.Item>
                ))}
              </ListGroup>
            )}
          </InfiniteScroll>
        </div>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onClose}>
          <FormattedMessage id="general.closeBtn" defaultMessage="Close" />
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

const RegisterUserModal: React.FC<{ onClose: () => void }> = ({ onClose }) => {
  const mutate = useMatchMutate();
  return (
    <Modal show>
      <Modal.Header>
        <Modal.Title>
          <FormattedMessage
            id="page.home.friendsList.registerFriend.title"
            defaultMessage="Register new friend in the system"
          />
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <RegisterUserForm
          onSubmit={(data) => createUser(data, mutate).then(onClose)}
          footer={
            <Modal.Footer className="m-0 p-0">
              <Button type="submit">
                <FormattedMessage
                  id="page.home.friendList.registerFriend.btn"
                  defaultMessage="Register"
                />
              </Button>
              <Button variant="secondary" onClick={onClose}>
                <FormattedMessage
                  id="general.closeBtn"
                  defaultMessage="Close"
                />
              </Button>
            </Modal.Footer>
          }
        />
      </Modal.Body>
    </Modal>
  );
};

export default FriendsListPanel;
