import Paginated from "../common/Paginated";

interface UserSearchCriteria extends Paginated {
  search?: string;
  username?: string;
  email?: string;
  inChannelId?: number;
  onlyFriends?: boolean;
  active?: boolean;
}

export default UserSearchCriteria;
