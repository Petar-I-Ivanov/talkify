import React, { useState } from "react";
import { Button, Form, ListGroup, Modal } from "react-bootstrap";
import { useForm } from "react-hook-form";
import IconButton from "../../components/IconButton";
import InfiniteScroll from "../../components/InfiniteScroll";
import RegisterUserForm from "../register/RegisterUserForm";
import { useSelectedChannelId } from "../../services/utils/useSelectedChannelId";
import useMatchMutate from "../../services/utils/useMatchMutate";
import { addFriend, removeFriend } from "../../services/apis/friendshipApi";
import {
  createUser,
  useUsersForInfiniteScrolling,
} from "../../services/apis/userApi";
import UserSearchCriteria from "../../models/user/UserSearchCriteria";

import SearchIcon from "../../assets/icons/search-icon.svg?react";
import BinIcon from "../../assets/icons/bin-icon.svg?react";
import PlusIcon from "../../assets/icons/plus-icon.svg?react";

const FriendsListPanel = () => {
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
        <h4>Friends list</h4>

        <div>
          <IconButton
            icon={SearchIcon}
            tooltipId="UsersSearch"
            placement="bottom"
            tooltip={<span>Search all users</span>}
            onClick={() => setSearchUsers(true)}
          />
          <IconButton
            icon={PlusIcon}
            variant="outline-success"
            tooltipId="UserRegister"
            placement="bottom"
            tooltip={<span>Register friend</span>}
            onClick={() => setRegisterFriend(true)}
          />
        </div>
      </div>
      <Form.Control
        className="mb-2"
        {...register("search")}
        placeholder="search"
      />

      <div style={{ overflow: "auto", scrollbarWidth: "thin" }}>
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
                          tooltip={<span>Remove friend</span>}
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
        <Modal.Title>Search from all the users in the system</Modal.Title>
      </Modal.Header>
      <Modal.Body style={{ height: "20rem" }}>
        <Form.Control
          {...register("search")}
          placeholder="Search"
          className="mb-2"
        />
        <div
          style={{ height: "70%", overflow: "auto", scrollbarWidth: "thin" }}
        >
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
                  <ListGroup.Item key={user.id}>
                    <div className="d-flex align-items-center justify-content-between">
                      <span>{user.username}</span>

                      <div>
                        {user._links?.addFriend?.href && (
                          <IconButton
                            icon={PlusIcon}
                            variant="outline-success"
                            tooltipId="AddFriend"
                            tooltip={<span>Add friend</span>}
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
                            tooltipId="AddFriend"
                            tooltip={<span>Remove friend</span>}
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
          Close
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
        <Modal.Title>Register new friend in the system</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <RegisterUserForm
          onSubmit={(data) => createUser(data, mutate).then(onClose)}
          footer={
            <Modal.Footer className="m-0 p-0">
              <Button type="submit">Register</Button>
              <Button variant="secondary" onClick={onClose}>
                Close
              </Button>
            </Modal.Footer>
          }
        />
      </Modal.Body>
    </Modal>
  );
};

export default FriendsListPanel;
