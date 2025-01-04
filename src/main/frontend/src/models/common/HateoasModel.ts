import { HalLink } from "hateoas-hal-types";

interface HateoasModel {
  _links?: {
    update?: HalLink;
    delete?: HalLink;
  };
}

export default HateoasModel;
