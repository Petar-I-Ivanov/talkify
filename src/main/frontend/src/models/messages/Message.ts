import HateoasModel from "../common/HateoasModel";

interface Message extends HateoasModel {
  id: number;
  text?: string;
  sender: string;
  sentAt: Date;
  editedAt?: Date;
  currentUserSender: boolean;
}

export default Message;
