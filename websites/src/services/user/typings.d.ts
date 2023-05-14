// @ts-ignore
/* eslint-disable */

declare namespace API {

  type ListUserData = {
    status: string;
    userType: string;
  }

  type UserPasswordChange = {
    code: string;
    password: string;
  }

  type UserUpdate = {
    username: string;
  }

  type UserChatPage = {
    username?: string;
    appId?: string;
    category?: string;
    createdAt?: string;
    createdBy?: string;
    page?: number;
    perPage?: number;
  }
}
