import Paginated from "../common/Paginated";

interface MessageSearchCriteria extends Paginated {
  channelId?: number;
}

export default MessageSearchCriteria;
