import HateoasModel from "../common/HateoasModel";

interface User extends HateoasModel {
  id: number;
  username: string;
  email: string;
}

export default User;
