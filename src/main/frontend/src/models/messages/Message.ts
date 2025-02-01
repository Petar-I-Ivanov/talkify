import HateoasModel from "../common/HateoasModel";

interface Message extends HateoasModel {
  id: string;
  text?: string;
  sender: string;
  sentAt: Date;
  editedAt?: Date;
}

export default Message;
