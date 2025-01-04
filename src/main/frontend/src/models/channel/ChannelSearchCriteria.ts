import Paginated from "../common/Paginated";

interface ChannelSearchCriteria extends Paginated {
  name?: string;
  userId?: number;
  ownerId?: number;
  adminId?: number;
  guestId?: number;
  active?: boolean;
}

export default ChannelSearchCriteria;
