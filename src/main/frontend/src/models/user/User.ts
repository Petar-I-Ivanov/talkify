import { HalLink } from "hateoas-hal-types";
import HateoasModel from "../common/HateoasModel";

interface User extends HateoasModel {
  id: number;
  username: string;
  email: string;
  privateChannelId?: number;
  _links: {
    update?: HalLink;
    delete?: HalLink;
    addFriend?: HalLink;
    removeFriend?: HalLink;
  };
}

export default User;
