import React, { useState } from "react";
import { FormattedMessage, useIntl } from "react-intl";
import Select from "react-select";
import { Controller, useForm } from "react-hook-form";
import { Button, Form, ListGroup, Modal } from "react-bootstrap";
import InfiniteScroll from "~/components/InfiniteScroll";
import IconButton from "~/components/IconButton";
import { useSelectedChannelId } from "~/services/utils/useSelectedChannelId";
import useMatchMutate from "~/services/utils/useMatchMutate";
import {
  isNameTaken,
  maxLength,
  minLength,
  requiredMsg,
} from "~/services/utils/reactHookFormValidations";
import { useUsersByCriteria } from "~/services/apis/userApi";
import {
  createChannel,
  deleteChannel,
  updateChannel,
  useChannelsForInfiniteScrolling,
} from "~/services/apis/channelApi";
import {
  addChannelMember,
  makeAdmin,
  removeMember,
  useChannelMembers,
} from "~/services/apis/channelMemberApi";
import Channel from "~/models/channel/Channel";
import ChannelSearchCriteria from "~/models/channel/ChannelSearchCriteria";
import ChannelCreateUpdateRequest from "~/models/channel/ChannelCreateUpdateRequest";
import AddChannelGuestRequest from "~/models/channel/member/AddChannelGuestRequest";

import UserIcon from "~/assets/icons/user-icon.svg?react";
import EditIcon from "~/assets/icons/edit-icon.svg?react";
import BinIcon from "~/assets/icons/bin-icon.svg?react";
import PlusIcon from "~/assets/icons/plus-icon.svg?react";
import AdminIcon from "~/assets/icons/admin-icon.svg?react";
import UserAddIcon from "~/assets/icons/user-add-icon.svg?react";

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
  const [addMember, setAddMember] = useState<Channel>();
  const [showMembers, setShowMembers] = useState<Channel>();
  const [editChannel, setEditChannel] = useState<Channel>();
  const [deleteChannel, setDeleteChannel] = useState<Channel>();

  return (
    <>
      <div className="d-flex align-items-center justify-content-between">
        <h4>
          <FormattedMessage
            id="page.home.channelList.title"
            defaultMessage="Channels list"
          />
        </h4>
        <IconButton
          icon={PlusIcon}
          variant="outline-success"
          tooltipId="ChannelAdd"
          tooltip={
            <FormattedMessage
              id="page.home.channelList.create"
              defaultMessage="Create new channel"
            />
          }
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
          emptyIndicator={
            <FormattedMessage
              id="page.home.channelList.empty"
              defaultMessage="No channels to show"
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
                      {channel?._links?.addMember?.href && (
                        <IconButton
                          icon={UserAddIcon}
                          variant="outline-success"
                          tooltipId="AddChannelMember"
                          tooltip={
                            <FormattedMessage
                              id="page.home.channelList.addMember"
                              defaultMessage="Add channel member"
                            />
                          }
                          onClick={(e) => {
                            e.stopPropagation();
                            setAddMember(channel);
                          }}
                        />
                      )}
                      <IconButton
                        icon={UserIcon}
                        tooltipId="MembersShow"
                        tooltip={
                          <FormattedMessage
                            id="page.home.channelList.showMembers"
                            defaultMessage="Show channel members"
                          />
                        }
                        onClick={(e) => {
                          e.stopPropagation();
                          setShowMembers(channel);
                        }}
                      />
                      {channel?._links?.update?.href && (
                        <IconButton
                          icon={EditIcon}
                          variant="outline-warning"
                          tooltipId="ChannelUpdate"
                          tooltip={
                            <FormattedMessage
                              id="page.home.channelList.update"
                              defaultMessage="Update channel name"
                            />
                          }
                          onClick={(e) => {
                            e.stopPropagation();
                            setEditChannel(channel);
                          }}
                        />
                      )}
                      {channel?._links?.delete?.href && (
                        <IconButton
                          icon={BinIcon}
                          variant="outline-danger"
                          tooltipId="ChannelDelete"
                          tooltip={
                            <FormattedMessage
                              id="page.home.channelList.delete"
                              defaultMessage="Delete channel"
                            />
                          }
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

      {addMember && (
        <AddChannelMemberModal
          channel={addMember}
          onClose={() => setAddMember(undefined)}
        />
      )}
    </>
  );
};

const AddChannelMemberModal: React.FC<{
  channel: Channel;
  onClose: () => void;
}> = ({ channel, onClose }) => {
  const intl = useIntl();
  const mutate = useMatchMutate();
  const { data: users } = useUsersByCriteria({
    active: true,
    onlyFriends: true,
    notInChannelId: channel.id,
  });

  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<AddChannelGuestRequest>();

  return (
    <Modal show>
      <Modal.Header>
        <Modal.Title>
          <FormattedMessage
            id="page.home.channelList.addMemberModal.title"
            defaultMessage="Select user to join <b>{channelName}</b> channel"
            values={{
              channelName: channel.name,
              b: (chunks) => <b>{chunks}</b>,
            }}
          />
        </Modal.Title>
      </Modal.Header>
      <Form
        onSubmit={handleSubmit(async (data) =>
          addChannelMember(channel, data, mutate).then(onClose)
        )}
      >
        <Modal.Body>
          <Controller
            control={control}
            name="userId"
            rules={{ required: requiredMsg(intl) }}
            render={({ field: { value, onChange } }) => (
              <Select
                styles={{
                  menuPortal: (base) => ({
                    ...base,
                    zIndex: 9999,
                  }),
                }}
                menuPortalTarget={document.body}
                menuPlacement="auto"
                placeholder={intl.formatMessage({
                  id: "page.home.channelList.addMemberModal.selectUser.placeholder",
                  defaultMessage: "Select a user",
                })}
                options={users}
                value={users?.find((user) => user.id === value) ?? null}
                getOptionLabel={(option) => option.username}
                getOptionValue={(option) => option.id + ""}
                onChange={(option) => onChange(option?.id ?? null)}
                isClearable
              />
            )}
          />

          {errors.userId?.message && (
            <Form.Text className="error">{errors.userId.message}</Form.Text>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button type="submit">
            <FormattedMessage
              id="page.home.channelList.addMemberModal.addMemberBtn"
              defaultMessage="Add in channel"
            />
          </Button>
          <Button variant="secondary" onClick={onClose}>
            <FormattedMessage id="general.closeBtn" defaultMessage="Close" />
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
};

const ChannelCreateModal: React.FC<{ onClose: () => void }> = ({ onClose }) => {
  const intl = useIntl();
  const mutate = useMatchMutate();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ChannelCreateUpdateRequest>();

  return (
    <Modal show>
      <Modal.Header>
        <Modal.Title>
          <FormattedMessage
            id="page.home.channelList.createModal.title"
            defaultMessage="Pick a name for your new channel!"
          />
        </Modal.Title>
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
              required: requiredMsg(intl),
              minLength: minLength(3, intl),
              maxLength: maxLength(64, intl),
              validate: async (value) =>
                value && (await isNameTaken(intl, { value })),
            })}
          />

          {errors.name?.message && (
            <Form.Text className="error">{errors.name.message}</Form.Text>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button type="submit">
            <FormattedMessage id="general.createBtn" defaultMessage="Create" />
          </Button>
          <Button variant="secondary" onClick={onClose}>
            <FormattedMessage id="general.closeBtn" defaultMessage="Close" />
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
      <Modal.Body>
        <FormattedMessage
          id="page.home.channelList.deleteModal.title"
          defaultMessage="Are you sure you want to delete channel with name <b>{channelName}</b>?"
          values={{ channelName: channel.name, b: (chunks) => <b>{chunks}</b> }}
        />
      </Modal.Body>
      <Modal.Footer>
        <Button
          onClick={async () =>
            await deleteChannel(channel, mutate).then(onClose)
          }
        >
          <FormattedMessage id="general.yes" defaultMessage="Yes" />
        </Button>
        <Button variant="secondary" onClick={onClose}>
          <FormattedMessage id="general.no" defaultMessage="No" />
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

const ChannelNameEdit: React.FC<{ channel: Channel; onClose: () => void }> = ({
  channel,
  onClose,
}) => {
  const intl = useIntl();
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
          required: requiredMsg(intl),
          minLength: minLength(3, intl),
          maxLength: maxLength(64, intl),
          validate: async (value) =>
            value && (await isNameTaken(intl, { value })),
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
        <Modal.Title>
          <FormattedMessage
            id="page.home.channelList.showMembers.modal.title"
            defaultMessage="Channel members of <b>{channelName}</b>"
            values={{ channelName: channel.name, b: (chunk) => <b>{chunk}</b> }}
          />
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {members?.map((member) => (
          <div key={member.id} className="d-flex justify-content-between">
            <span>{`${member.username} - ${member.role}`}</span>

            <div>
              {member._links?.makeAdmin?.href && (
                <IconButton
                  icon={AdminIcon}
                  variant="outline-info"
                  tooltipId="MakeChannelAdmin"
                  tooltip={
                    <FormattedMessage
                      id="page.home.channelList.showMembers.modal.makeAdminBtn"
                      defaultMessage="Make channel admin"
                    />
                  }
                  onClick={async () => await makeAdmin(member, mutate)}
                />
              )}
              {member._links?.removeMember?.href && (
                <IconButton
                  icon={BinIcon}
                  variant="outline-danger"
                  tooltipId="RemoveChannelMember"
                  tooltip={
                    <FormattedMessage
                      id="page.home.channelList.showMembers.modal.removeMemberBtn"
                      defaultMessage="Remove channel member"
                    />
                  }
                  onClick={async () => await removeMember(member, mutate)}
                />
              )}
            </div>
          </div>
        ))}
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={() => onClose()}>
          <FormattedMessage id="general.closeBtn" defaultMessage="Close" />
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ChannelsListPanel;
