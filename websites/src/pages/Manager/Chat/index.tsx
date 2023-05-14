import {PageHeaderWrapper} from '@ant-design/pro-components';
import {Alert, Card, Table} from 'antd';
import React, {useEffect, useState} from 'react';
import {useModel} from "@@/plugin-model/useModel";
import {listUsers, userChatPage} from "@/services/user/api";
import type {ColumnsType} from 'antd/es/table';
import moment from "moment";
import md from "@/components/markdown-it"

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
  const [totalUserChats, setTotalUserChats] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [isLoading, setLoading] = useState(false);


  useEffect(() => {
    setLoading(true);
    userChatPage({ page: currentPage })
      .then(response => {
        setUserChats(response.data);
        setTotalUserChats(response.total);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [currentPage]);

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
      width: '20%',
      render: (_, record) => (
        <div dangerouslySetInnerHTML={{__html: md.render(record.questions?.messages[record.questions?.messages.length -1].content)}}/>
      ),
    },
    {
      title: '回答',
      dataIndex: 'answers',
      key: 'answers',
      width: '20%',
      render: (_, record) => (
        <div dangerouslySetInnerHTML={{__html: md.render(record.answers.answer)}}/>
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
  const handlePaginationChange = (page) => {
    setCurrentPage(page);
  };

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
      <Table
        columns={columns}
             dataSource={userChats.content}
             pagination={{
               defaultCurrent: currentPage,
               defaultPageSize: userChats.perPage,
               total: userChats.total,
               showSizeChanger: true,
               pageSizeOptions: ['20']}}
             onChange={({ current }) => handlePaginationChange(current)}
      />
    </PageHeaderWrapper>
  );
};

export default UsersPage;
