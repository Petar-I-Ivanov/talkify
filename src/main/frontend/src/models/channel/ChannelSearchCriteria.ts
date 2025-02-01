import Paginated from "../common/Paginated";

interface ChannelSearchCriteria extends Paginated {
  name?: string;
  userId?: string;
  ownerId?: string;
  adminId?: string;
  guestId?: string;
  active?: boolean;
}

export default ChannelSearchCriteria;
