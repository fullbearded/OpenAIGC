import {PageHeaderWrapper} from '@ant-design/pro-components';
import {Alert, Card, Form, Input, message, Modal, Space, Table} from 'antd';
import React, {useEffect, useState} from 'react';
import {useModel} from "@@/plugin-model/useModel";
import {listUsers, passwordChange, userUpdate} from "@/services/user/api";
import moment from "moment";

const {Column} = Table;


const statusMap = {
  'ENABLED': '启用',
  'DISABLED': '禁用',
}

const UsersPage: React.FC = () => {

  const {initialState} = useModel('@@initialState');
  // @ts-ignore
  const {currentUser} = initialState;
  const [userList, setUserList] = useState([]);

  const [selectUser, setSelectUser] = useState({
    username: undefined
  });
  const [form] = Form.useForm();
  const [userInfoForm] = Form.useForm();

  useEffect(() => {
    listUsers({}).then((res) => {
      setUserList(res.data)
    });
  }, {});

  const [open, setOpen] = useState(false);
  const [confirmLoading, setConfirmLoading] = useState(false);
  const showModal = (user: any) => {
    setSelectUser(user);
    setOpen(true);
    return
  };

  // User info Edit
  const [userOpen, setUserOpen] = useState(false);
  const [userConfirmLoading, setUserConfirmLoading] = useState(false);
  const showUserEditModal = (user: any) => {
    setSelectUser(user);
    setUserOpen(true);
    userInfoForm.setFieldsValue({
      username: user.username,
    });
    return
  }
  const handleUserCancel = () => {
    setUserOpen(false);
  };

  const handleUserInfoOk = () => {
    setConfirmLoading(true);

    // Get password value from the form
    const username = userInfoForm.getFieldValue('username');
    userUpdate({username: username, code: selectUser.code}).then((response) => {
      message.success("修改成功");
      selectUser.username = username;
      // @ts-ignore
      setUserList(userList.map(user => {
        if (user.code === selectUser.code) {
          return {...user, username: username};
        } else {
          return user;
        }
      }));
      setTimeout(() => {
        setUserOpen(false);
        setUserConfirmLoading(false);
      }, 1000);
    }).catch((error) => {
      setUserConfirmLoading(false);
    });
  };

  const handleOk = () => {
    setConfirmLoading(true);

    // Get password value from the form
    const password = form.getFieldValue('password');

    passwordChange({code: selectUser.code, password: password})
      .then((response) => {
        // Check the HTTP status
        if (response.status === 200) {
          message.success("修改成功");
          setTimeout(() => {
            setOpen(false);
            setConfirmLoading(false);
          }, 1000);
        }
      })
      .catch((error) => {
        setConfirmLoading(false);
        console.log(error);
      })
  };
  const handleCancel = () => {
    console.log('Clicked cancel button');
    setOpen(false);
  };

  return (
    <PageHeaderWrapper
      content={`你的企业${currentUser?.organizationName}，欢迎使用`}
    >
      <Card>
        <Alert
          message="管理您的企业用户"
          type="success"
          showIcon
          banner
          style={{
            margin: -12,
          }}
        />
      </Card>
      <Table dataSource={userList}>
        <Column title="用户编号" dataIndex="id" key="id"/>
        <Column title="用户名" dataIndex="username" key="username"/>
        <Column title="到期时间" dataIndex="expireDate" key="expireDate" render={expireDate => moment(expireDate).format('YYYY-MM-DD HH:mm:ss')}/>
        <Column title="今日消耗次数" dataIndex="todayUsedQuota" key="todayUsedQuota"/>
        <Column title="每日次数" dataIndex="dailyLimit" key="dailyLimit"/>
        <Column title="用户状态" dataIndex="status" key="status" render={status => statusMap[status] || status}/>
        <Column
          title="操作"
          key="action"
          render={(text, user: any) => (
            <Space size="middle">
              <a onClick={() => showModal(user)}>修改密码</a>

              <a onClick={() => showUserEditModal(user)}>信息修改</a>
            </Space>
          )}
        />
      </Table>
      <Modal
        title="密码修改"
        open={open}
        onOk={handleOk}
        confirmLoading={confirmLoading}
        onCancel={handleCancel}
      >
        <Form
          form={form}
          name="basic"
          style={{maxWidth: 800, textAlign: "left"}}
          autoComplete="off"
        >
          <Form.Item
            label="用户名"
          >
            {selectUser.username}
          </Form.Item>
          <Form.Item
            label="密码"
            name="password"
            rules={[{required: true, message: '请输入你的重置密码'}]}
          >
            <Input.Password placeholder="请输入你的重置密码"/>
          </Form.Item>
        </Form>
      </Modal>


      <Modal
        title="信息修改"
        open={userOpen}
        onOk={handleUserInfoOk}
        confirmLoading={userConfirmLoading}
        onCancel={handleUserCancel}
      >
        <Form
          form={userInfoForm}
          name="basic"
          style={{maxWidth: 800, textAlign: "left"}}
          autoComplete="off"
        >
          <Form.Item
            label="用户名"
            name="username"
            rules={[{required: true, message: '请输入用户名'}]}
          >
            <Input placeholder="请输入用户名" />
          </Form.Item>
        </Form>
      </Modal>
    </PageHeaderWrapper>
  );
};

export default UsersPage;
