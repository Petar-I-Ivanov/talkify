import { useForm } from "react-hook-form";
import ChannelSearchCriteria from "../../models/channel/ChannelSearchCriteria";
import {
  createChannel,
  deleteChannel,
  getChannelsExistsByName,
  makeAdmin,
  removeMember,
  updateChannel,
  useChannelMembers,
  useChannelsForInfiniteScrolling,
} from "../../services/apis/channelApi";
import { Button, Form, ListGroup, Modal } from "react-bootstrap";
import InfiniteScroll from "../../components/InfiniteScroll";
import { useSelectedChannelId } from "../../services/utils/useSelectedChannelId";
import IconButton from "../../components/IconButton";
import UserIcon from "../../assets/icons/user-icon.svg?react";
import EditIcon from "../../assets/icons/edit-icon.svg?react";
import BinIcon from "../../assets/icons/bin-icon.svg?react";
import PlusIcon from "../../assets/icons/plus-icon.svg?react";
import AdminIcon from "../../assets/icons/admin-icon.svg?react";
import React, { useState } from "react";
import Channel from "../../models/channel/Channel";
import ChannelCreateUpdateRequest from "../../models/channel/ChannelCreateUpdateRequest";
import {
  maxLength,
  minLength,
  REQUIRED_MSG,
} from "../../services/utils/reactHookFormValidations";
import useMatchMutate from "../../services/utils/useMatchMutate";

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

  const [createChannel, setCreateChannel] = useState<boolean>(false);
  const [showMembers, setShowMembers] = useState<Channel>();
  const [editChannel, setEditChannel] = useState<Channel>();
  const [deleteChannel, setDeleteChannel] = useState<Channel>();

  return (
    <>
      <div className="d-flex align-items-center justify-content-between">
        <h4>Channels list</h4>
        <IconButton
          icon={PlusIcon}
          tooltipId="ChannelAdd"
          tooltip={<span>Create channel</span>}
          onClick={() => setCreateChannel(true)}
        />
      </div>
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
                  <div className="d-flex align-items-center">
                    {editChannel && editChannel.id === channel.id ? (
                      <ChannelNameEdit
                        channel={editChannel}
                        onClose={() => setEditChannel(undefined)}
                      />
                    ) : (
                      <>{channel.name}</>
                    )}
                    <div className="ms-auto">
                      <IconButton
                        icon={UserIcon}
                        tooltipId="MembersShow"
                        tooltip={<span>Show channel members</span>}
                        onClick={(e) => {
                          e.stopPropagation();
                          setShowMembers(channel);
                        }}
                      />
                      {channel?._links?.update?.href && (
                        <IconButton
                          icon={EditIcon}
                          tooltipId="ChannelUpdate"
                          tooltip={<span>Update channel name</span>}
                          onClick={(e) => {
                            e.stopPropagation();
                            setEditChannel(channel);
                          }}
                        />
                      )}
                      {channel?._links?.delete?.href && (
                        <IconButton
                          icon={BinIcon}
                          tooltipId="ChannelDelete"
                          tooltip={<span>Delete channel</span>}
                          onClick={(e) => {
                            e.stopPropagation();
                            setDeleteChannel(channel);
                          }}
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

      {createChannel && (
        <ChannelCreateModal onClose={() => setCreateChannel(false)} />
      )}

      {deleteChannel && (
        <ChannelDeleteConfirm
          channel={deleteChannel}
          onClose={() => setDeleteChannel(undefined)}
        />
      )}

      {showMembers && (
        <PreviewChannelMembers
          channel={showMembers}
          onClose={() => setShowMembers(undefined)}
        />
      )}
    </>
  );
};

const ChannelCreateModal: React.FC<{ onClose: () => void }> = ({ onClose }) => {
  const mutate = useMatchMutate();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ChannelCreateUpdateRequest>();

  return (
    <Modal show>
      <Modal.Header>
        <Modal.Title>Pick a name for your new channel!</Modal.Title>
      </Modal.Header>
      <Form
        onClick={(e) => e.stopPropagation()}
        onSubmit={handleSubmit(
          async (data) => await createChannel(data, mutate).then(onClose)
        )}
      >
        <Modal.Body>
          <Form.Control
            {...register("name", {
              required: REQUIRED_MSG,
              minLength: minLength(3),
              maxLength: maxLength(64),
              validate: async (value) =>
                (value && !(await getChannelsExistsByName({ value }))) ||
                "Name is already taken!",
            })}
          />

          {errors.name?.message && (
            <Form.Text className="error">{errors.name.message}</Form.Text>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button type="submit">Submit</Button>
          <Button variant="secondary" onClick={onClose}>
            Close
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
};

const ChannelDeleteConfirm: React.FC<{
  channel: Channel;
  onClose: () => void;
}> = ({ channel, onClose }) => {
  const mutate = useMatchMutate();
  return (
    <Modal show>
      <Modal.Body>{`Are you sure you want to delete channel with name ${channel.name}?`}</Modal.Body>
      <Modal.Footer>
        <Button
          onClick={async () =>
            await deleteChannel(channel, mutate).then(() => onClose)
          }
        >
          Yes
        </Button>
        <Button variant="secondary" onClick={onClose}>
          No
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

const ChannelNameEdit: React.FC<{ channel: Channel; onClose: () => void }> = ({
  channel,
  onClose,
}) => {
  const mutate = useMatchMutate();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ChannelCreateUpdateRequest>({
    defaultValues: channel,
  });

  return (
    <Form
      onClick={(e) => e.stopPropagation()}
      onSubmit={handleSubmit(
        async (data) => await updateChannel(channel, data, mutate).then(onClose)
      )}
      onKeyDown={(e) => {
        if (e.key === "Enter") {
          handleSubmit(
            async (data) =>
              await updateChannel(channel, data, mutate).then(onClose)
          );
        }
        if (e.key === "Escape") {
          onClose();
        }
      }}
    >
      <Form.Control
        style={{ width: "70%" }}
        {...register("name", {
          required: REQUIRED_MSG,
          minLength: minLength(3),
          maxLength: maxLength(64),
          validate: async (value) =>
            (value &&
              !(await getChannelsExistsByName({
                value,
                exceptId: channel.id,
              }))) ||
            "Name is already taken!",
        })}
      />

      {errors.name?.message && (
        <Form.Text className="error">{errors.name.message}</Form.Text>
      )}
    </Form>
  );
};

const PreviewChannelMembers: React.FC<{
  channel: Channel;
  onClose: () => void;
}> = ({ channel, onClose }) => {
  const mutate = useMatchMutate();
  const { data: members } = useChannelMembers(channel.id);

  return (
    <Modal show>
      <Modal.Header>
        <Modal.Title>Channel members of {channel.name}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {members?.map((member) => (
          <div key={member.id} className="d-flex justify-content-between">
            <span>{`${member.username} - ${member.role}`}</span>

            <div>
              {member._links?.makeAdmin?.href && (
                <IconButton
                  icon={AdminIcon}
                  tooltipId="MakeChannelAdmin"
                  tooltip={<span>Make channel admin</span>}
                  onClick={async () => await makeAdmin(member, mutate)}
                />
              )}
              {member._links?.removeMember?.href && (
                <IconButton
                  icon={BinIcon}
                  tooltipId="RemoveChannelMember"
                  tooltip={<span>Remove channel member</span>}
                  onClick={async () => await removeMember(member, mutate)}
                />
              )}
            </div>
          </div>
        ))}
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={() => onClose()}>
          Close
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ChannelsListPanel;
