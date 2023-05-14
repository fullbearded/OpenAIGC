// @ts-ignore
/* eslint-disable */
import {request} from 'umi';

export async function listUsers(body: {}, options?: { [p: string]: any }) {
  return request<API.CommonApiResponse>('/api/user/list', {
    method: 'POST',
    ...(options || {}),
    data: JSON.stringify(body),
  });
}

export async function passwordChange(body: API.UserPasswordChange, options?: { [key: string]: any }) {
  return request<API.CommonApiResponse>('/api/user/password/change', {
    method: 'POST',
    ...(options || {}),
    data: JSON.stringify(body),
  });
}

export async function userUpdate(body: API.UserUpdate, options?: { [key: string]: any }) {
  return request<API.CommonApiResponse>('/api/user/update', {
    method: 'POST',
    ...(options || {}),
    data: JSON.stringify(body),
  });
}

export async function userChatPage(body: API.UserChatPage, options?: { [key: string]: any }) {
  return request<API.CommonApiResponse>('/api/user-chat/page', {
    method: 'POST',
    ...(options || {}),
    data: JSON.stringify(body),
  });
}
