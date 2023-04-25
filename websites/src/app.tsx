import Footer from '@/components/Footer';
import RightContent from '@/components/RightContent';
import {BookOutlined, LinkOutlined} from '@ant-design/icons';
import type {Settings as LayoutSettings} from '@ant-design/pro-components';
import {SettingDrawer} from '@ant-design/pro-components';
import type {RunTimeLayoutConfig} from 'umi';
import {history, Link} from 'umi';
import { RequestConfig } from 'umi';
import { ResponseError } from 'umi-request';
import defaultSettings from '../config/defaultSettings';
import {currentUser as queryCurrentUser} from './services/ant-design-pro/api';

import MainLoading from '@/components/MainLoading';
import {message, notification} from "antd";
import {getToken} from "@/utils";
const isDev = process.env.NODE_ENV === 'development';
const loginPath = '/login';
const homePath = '/';
const adminPath = '/admin';
const userPath = '/user'

/** 获取用户信息比较慢的时候会展示一个 loading */
export const initialStateConfig = {
  loading: <MainLoading/>,
};

const errorHandler = async (error: ResponseError) => {
  const {response} = error;
  if (response && response.status) {
    if (response.status === 422) {
      if (response && response.body) {
        try {
          const errorData = await response.clone().json(); // 或者使用 .text()
          const errorMessage = errorData.message || '业务异常';
          message.error(errorMessage);
        } catch (e) {
          message.error('业务异常，请检查网络');
        }
      } else {
        message.error('业务异常，请稍后再次');
      }
    }
    if (response.status === 401) {
      try {
        const errorData = await response.clone().json(); // 或者使用 .text()
        const errorMessage = errorData.message || '业务异常';
        message.error(errorMessage);
      } catch (e) {
        message.error('登录失败，请重新登录');
      }
      history.push(loginPath);
    }
    if (response.status === 403) {
      message.error('没有权限，请联系管理员');
    }
    if (response.status === 404) {
      message.error('请求资源不存在');
    }
    if (response.status === 500 || response.status === 502 || response.status === 503 || response.status === 504) {
      notification.error({
        description: '服务器异常，请稍后再试',
        message: '服务器异常',
      });
    }
  }
  if (!response) {
    notification.error({
      description: '您的网络发生异常，无法连接服务器',
      message: '网络异常',
    });
  }
  throw error;
};

const authHeaderInterceptor = (url: string, options: any) => {
  if(url.includes("anonymous")) {
    return {url: `${url}`, options: {...options}}
  }

  const token = getToken()
  const authHeader = { Authorization: 'Bearer ' + token };
  return {
    url: `${url}`,
    options: { ...options, interceptors: true, headers: authHeader },
  };
};

export const request: RequestConfig = {
  errorHandler,
  requestInterceptors: [authHeaderInterceptor]
};

/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 * */
export async function getInitialState(): Promise<{
  settings?: Partial<LayoutSettings>;
  currentUser?: API.CurrentUser;
  loading?: boolean;
  fetchUserInfo?: () => Promise<API.CurrentUser | undefined>;
}> {
  const fetchUserInfo = async () => {
    try {
      const msg = await queryCurrentUser();
      return msg.data;
    } catch (error) {
      history.push(loginPath);
    }
    return undefined;
  };
  const {location} = history;

  console.log(location.pathname);

  if (location.pathname === homePath) {
    return {
      fetchUserInfo,
      settings: defaultSettings as Partial<LayoutSettings>,
    };
  }


  // 如果需要权限验证
  if (history.location.pathname.startsWith(adminPath) ||
    history.location.pathname.startsWith(userPath)) {
    const currentUser = await fetchUserInfo();
    return {
      fetchUserInfo,
      currentUser,
      settings: defaultSettings,
    };
  }
  return {
    settings: defaultSettings,
  };
}

// ProLayout 支持的api https://procomponents.ant.design/components/layout
export const layout: RunTimeLayoutConfig = ({initialState, setInitialState}) => {
  return {
    rightContentRender: () => <RightContent/>,
    disableContentMargin: false,
    waterMarkProps: {
      content: initialState?.currentUser?.username,
    },
    footerRender: () => <Footer/>,
    onPageChange: () => {
      const {location} = history;
      // 如果没有登录，重定向到 login
      if(!initialState?.currentUser) {
        if(location.pathname.startsWith(adminPath) || location.pathname.startsWith(userPath)) {
          history.push(loginPath);
        }
      }
    },
    links: isDev
      ? [
        <Link key="openapi" to="/umi/plugin/openapi" target="_blank">
          <LinkOutlined/>
          <span>OpenAPI 文档</span>
        </Link>,
        <Link to="/~docs" key="docs">
          <BookOutlined/>
          <span>业务组件文档</span>
        </Link>,
      ]
      : [],
    menuHeaderRender: undefined,
    // 自定义 403 页面
    // unAccessible: <div>unAccessible</div>,
    // 增加一个 loading 的状态
    childrenRender: (children: any, props: { location: { pathname: string | string[] } }) => {
      if (initialState?.loading) return <MainLoading />;
      return (
        <>
          {children}
          {!props.location?.pathname?.includes(loginPath) && (
            <SettingDrawer
              disableUrlParams
              enableDarkTheme
              settings={initialState?.settings}
              onSettingChange={(settings) => {
                setInitialState((preInitialState) => ({
                  ...preInitialState,
                  settings,
                }));
              }}
            />
          )}
        </>
      );
    },
    ...initialState?.settings,
  };
};
