import type {ProFormInstance} from '@ant-design/pro-components';
import {ProCard, ProForm, StepsForm,} from '@ant-design/pro-components';
import {Button, Col, message, Row} from 'antd';
import {useRef, useState} from 'react';
import {useHistory} from 'react-router-dom';

// @ts-ignore
import {v4 as uuidv4} from 'uuid';
import FormDataContext from './FormDataContext';


import CreateInfoPartial from "@/pages/Website/AppCreate/partials/CreateInfoPartial";
import CreateFormPartial from "@/pages/Website/AppCreate/partials/CreateFormPartial";
import CreateRolesPartial from "@/pages/Website/AppCreate/partials/CreateRolesPartial";
import {AppstoreOutlined} from "@ant-design/icons";
import {checkFreeApp, createFreeApp} from "@/services/server/api";
import AppFormComponent from "@/pages/Website/AppCreate/partials/AppFormComponent";

// 初始化 formData
const initialFormData: API.CreateAppData = {
  id: uuidv4(),
  name: '回复老板',
  icon: '🔨',
  description: '用婉转的方式回复老板，拒绝PUA从我做起',
  category: 'PUBLIC',
  forms: [
    {
      id: uuidv4(),
      name: 'message',
      type: 'ProFormText',
      label: '',
      props: {
        placeholder: '请输入内容',
        type: 'textarea',
        default: "领导，我不想干这个活",
        options: []
      },
    },
  ],
  roles: [
    {
      id: uuidv4(),
      type: 'system',
      template: '如何委婉巧妙的回复老板，给出5种回答，内容是：',
      index: 11
    },
    {
      id: uuidv4(),
      type: 'user',
      template: '${message}',
      index: 22
    },
  ],
  author: '',
  chat: false,
  temperature: 0.8,
  abortController: null
};

export default () => {
  const formRef = useRef<ProFormInstance>();

  const [formData, setFormData] = useState(initialFormData);
  const history = useHistory();

  const onFinish = async () => {
    createFreeApp(formData)
      .then((res) => {
        if (res.code === "200") {
          message.success('创建成功');
          history.push('/apps');
        } else {
          message.error(res.message);
        }
      });
  };

  return (
    <div className="home-page-wrapper-content">
      <Row className="create-container" justify="center" align="middle" style={{minHeight: '100vh'}}>
        <Col xs={24} sm={16} md={12} lg={10} xl={8}>
          <Button type="link" icon={<AppstoreOutlined/>} className="back-link" href="/apps">返回应用列表</Button>,
          <ProCard className="app-creator-wrapper home-page">
            <FormDataContext.Provider value={[formData, setFormData]}>
              <StepsForm<{ name: string; }>
                formRef={formRef}
                onFinish={async () => {
                  console.log(formData);
                  await onFinish();
                }}
                formProps={{
                  validateMessages: {
                    required: '此项为必填项',
                  },
                }}
              >
                <StepsForm.StepForm<{
                  name: string;
                }>
                  name="step1"
                  title="创建应用(1/4)"
                  stepProps={{
                    description: '应用基本信息',
                  }}
                  onFinish={async () => {
                    try {
                      const body = {
                        name: formData.name
                      }
                      const response = await checkFreeApp(body)
                      return response.status === 200;
                      // console.log(formData)
                    } catch (error) {
                      console.error('Error:', error);
                      return false;
                    }
                  }}
                >
                  <CreateInfoPartial/>
                </StepsForm.StepForm>
                <StepsForm.StepForm<{
                  name: string;
                }>
                  name="step2"
                  title="创建应用(2/4)"
                  stepProps={{
                    description: '表单信息',
                  }}
                  onFinish={async () => {
                    // console.log(formRef.current?.getFieldsValue());
                    return true;
                  }}
                >
                  <ProForm.Group>
                    <div>
                      <CreateFormPartial/>
                    </div>
                  </ProForm.Group>
                </StepsForm.StepForm>
                <StepsForm.StepForm<{
                  name: string;
                }>
                  name="step3"
                  title="创建应用(3/4)"
                  stepProps={{
                    description: 'AI角色信息',
                  }}
                  onFinish={async () => {
                    console.log(formRef.current?.getFieldsValue());
                    console.log(formData)
                    return true;
                  }}
                >
                  <CreateRolesPartial/>
                </StepsForm.StepForm>
                <StepsForm.StepForm<{
                  name: string;
                }>
                  name="step4"
                  title="创建应用(4/4)"
                  stepProps={{
                    description: '预览测试',
                  }}
                  onFinish={async () => {
                    console.log(formRef.current?.getFieldsValue());

                    return false;
                  }}
                >
                  <AppFormComponent/>
                </StepsForm.StepForm>
              </StepsForm>
            </FormDataContext.Provider>
          </ProCard>
        </Col>
      </Row>
    </div>
  );
};
