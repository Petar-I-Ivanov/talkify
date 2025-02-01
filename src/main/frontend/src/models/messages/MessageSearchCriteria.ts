import Paginated from "../common/Paginated";

interface MessageSearchCriteria extends Paginated {
  channelId?: string;
}

export default MessageSearchCriteria;
