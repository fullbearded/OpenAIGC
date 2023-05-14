// @ts-ignore
/* eslint-disable */
import {request} from 'umi';

/** 创建免费应用 POST /api/app/create/anonymous */
export async function createFreeApp(body: API.CreateAppData, options?: { [key: string]: any }) {
  return request<API.CommonApiResponse>('/api/app/create/anonymous', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(body),
    ...(options || {}),
  });
}

/** 检查应用名是否可用 POST /api/app/check/anonymous */
export async function checkFreeApp(body: API.CheckAppParam, options?: { [key: string]: any }) {
  return request<API.CommonApiResponse>('/api/app/check/anonymous', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(body),
    ...(options || {}),
  });
}

/** 分页查询应用 POST /api/app/page/anonymous */
export async function pageFreeApp(body: API.PageAppParam, options?: { [key: string]: any }) {
  return request<API.CommonApiResponse>('/api/app/page/anonymous', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(body),
    ...(options || {}),
  });
}

/** 点赞应用 POST /api/app/like/anonymous */
export async function likeFreeApp(body: API.LikeAppParam, options?: { [key: string]: any }) {
  return request<API.CommonApiResponse>('/api/app/like/anonymous', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(body),
    ...(options || {}),
  });
}

/** 匿名查询用户列表 POST /api/app/list/anonymous */
export async function listFreeApp(body: { code: any }, options?: { [p: string]: any }) {
  return request<API.CommonApiResponse>('/api/app/list/anonymous', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data: JSON.stringify(body),
    ...(options || {}),
  });
}
