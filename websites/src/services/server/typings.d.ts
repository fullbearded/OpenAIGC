// @ts-ignore
/* eslint-disable */

declare namespace API {

  type Role = {
    id?: string;
    template?: string;
    type?: string;
    index?: number
  }

  type FormItem = {
    id: string;
    name: string;
    type: string;
    props: FormItemProps;
    label: string;
  }

  type FormItemProps = {
    placeholder: string;
    type: string;
    default: string;
    options: string[];
  }


  type CreateAppData = {
    id: string;
    name: string;
    icon: string;
    description: string;
    forms: FormItem[];
    roles: Role[];
    chat: boolean;
    author: string;
    category: string;
  }


  type CreateFreeAppResponse = {
    "status": number,
    "code": string,
    "message": string,
    "data": any
  }

  type CheckAppParam = {
    name: string;
  }

  type ListAppParam = {
    name: string;
    code: string;
    recommend: string;
  }

  type PageAppParam = {
    name?: string;
    code?: string;
    recommend?: string;
    page?: number;
    perPage?: number;
  }

  type LikeAppParam = {
    code: string;
  }

}
