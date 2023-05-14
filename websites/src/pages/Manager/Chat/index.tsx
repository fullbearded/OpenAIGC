import {PageHeaderWrapper} from '@ant-design/pro-components';
import {Alert, Card, Table} from 'antd';
import React, {useEffect, useState} from 'react';
import {useModel} from "@@/plugin-model/useModel";
import {listUsers, userChatPage} from "@/services/user/api";
import type {ColumnsType} from 'antd/es/table';
import moment from "moment";

interface DataType {
  id: number;
  username: string;
  userCode: string;
  token: number;
  questions: string;
  answers: string;
  createdAt: string;
}

const UsersPage: React.FC = () => {
  const {initialState} = useModel('@@initialState');
  // @ts-ignore
  const {currentUser} = initialState;
  const [userChats, setUserChats] = useState({});

  useEffect(() => {
    userChatPage({}).then((res) => {
      setUserChats(res.data)
      console.log(res.data)
    });
  }, {});

  const columns: ColumnsType<DataType> = [
    {
      title: '序号',
      dataIndex: 'id',
      key: 'id'
    },
    {
      title: '用户',
      dataIndex: 'username',
      key: 'username',
    },
    {
      title: '令牌总数',
      dataIndex: 'token',
      key: 'token',
    },
    {
      title: '提问',
      dataIndex: 'questions',
      key: 'questions',
      width: '30%',
      render: (_, record) => (
        <>
          {record.questions?.messages[record.questions?.messages.length -1].content}
        </>
      ),
    },
    {
      title: '回答',
      dataIndex: 'answers',
      key: 'answers',
      width: '30%',
      render: (_, record) => (
        <>
          {record.answers.answer}
        </>
      ),
    },
    {
      title: '提问时间',
      dataIndex: 'createdAt',
      key: 'createdAt',
      render: (_, record) => (
        <>
        {moment(record.createdAt).format('YYYY-MM-DD HH:mm:ss')}
        </>
      )
    }
  ];

  return (
    <PageHeaderWrapper
      content={`你的企业${currentUser?.organizationName}，欢迎使用`}
    >
      <Card>
        <Alert
          message="会话记录"
          type="success"
          showIcon
          banner
          style={{
            margin: -12,
            marginBottom: 48,
          }}
        />
      </Card>
      <Table columns={columns} dataSource={userChats.content}/>
    </PageHeaderWrapper>
  );
};

export default UsersPage;
