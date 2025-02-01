import { HalLink } from "hateoas-hal-types";
import HateoasModel from "../common/HateoasModel";

interface User extends HateoasModel {
  id: string;
  username: string;
  email: string;
  privateChannelId?: string;
  _links: {
    update?: HalLink;
    delete?: HalLink;
    addFriend?: HalLink;
    removeFriend?: HalLink;
  };
}

export default User;
