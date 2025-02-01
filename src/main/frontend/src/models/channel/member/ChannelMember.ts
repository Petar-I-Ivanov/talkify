import { HalLink } from "hateoas-hal-types";
import ChannelMemberRole from "./ChannelMemberRole";

interface ChannelMember {
  id: string;
  username: string;
  role: ChannelMemberRole;
  _links?: {
    removeMember?: HalLink;
    makeAdmin?: HalLink;
  };
}

export default ChannelMember;
