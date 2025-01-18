import { HalLink } from "hateoas-hal-types";
import HateoasModel from "../common/HateoasModel";

interface Channel extends HateoasModel {
  id: number;
  name: string;
  _links: {
    update?: HalLink;
    delete?: HalLink;
    addMember?: HalLink;
  };
}

export default Channel;
