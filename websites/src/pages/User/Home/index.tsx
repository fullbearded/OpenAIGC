import {PageHeaderWrapper} from '@ant-design/pro-components';
import {Alert, Avatar, Card, Descriptions, Divider} from 'antd';
import React from 'react';
import {useModel} from "@@/plugin-model/useModel";
import {UserOutlined} from "@ant-design/icons";
import moment from "moment";

const userTypeMap = {
  'SUPER_ADMIN': '超级管理员',
  'ADMIN': '管理员',
  'SYSTEM': '系统用户',
  'USER': '会员用户',
}

const UserHome: React.FC = () => {
  const {initialState} = useModel('@@initialState');
  const {currentUser} = initialState;
  return (
    <PageHeaderWrapper>
      <Card>
        <Alert
          message="个人信息"
          type="success"
          showIcon
          banner
          style={{
            margin: -12,
            marginBottom: 48,
          }}
        />
      </Card>
      <Card>
        <Descriptions title="用户信息">
          <Descriptions.Item label="用户名">{currentUser.username}</Descriptions.Item>
          <Descriptions.Item label="用户类型">{userTypeMap[currentUser.userType] || currentUser.userType}</Descriptions.Item>
          <Descriptions.Item label="公司">{currentUser.organizationName}</Descriptions.Item>
          <Descriptions.Item label="会员到期日">{moment(currentUser.expireDate).format('YYYY-MM-DD HH:mm:ss')}</Descriptions.Item>
        </Descriptions>
        <Divider />
        <Descriptions title="数据统计">
          <Descriptions.Item label="今日使用">{currentUser.todayUsedQuota}</Descriptions.Item>
          <Descriptions.Item label="每日限制">{currentUser.dailyLimit}</Descriptions.Item>
          <Descriptions.Item label="总次数">{currentUser.totalQuota}</Descriptions.Item>
        </Descriptions>
      </Card>
    </PageHeaderWrapper>
  );
};

export default UserHome;
