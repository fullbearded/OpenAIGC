import type {ProFormInstance} from '@ant-design/pro-components';
import {
  ProCard,
  ProForm,
  StepsForm,
} from '@ant-design/pro-components';
import {Col, message, Row} from 'antd';
import {useRef, useState} from 'react';
// @ts-ignore
import {v4 as uuidv4} from 'uuid';
import FormDataContext from './FormDataContext';

import CreateInfoPartial from "@/pages/Website/AppCreate/partials/CreateInfoPartial";
import CreateFormPartial from "@/pages/Website/AppCreate/partials/CreateFormPartial";
import CreateRolesPartial from "@/pages/Website/AppCreate/partials/CreateRolesPartial";

interface Role {
  id: string;
  template: string;
  type: string;
}

interface FormItem {
  id: string;
  name: string;
  type: string;
  props: FormItemProps;
  label: string;
}

interface FormItemProps {
  placeholder: string;
  type: string;
  default: string;
}


interface AppData {
  id: string;
  name: string;
  icon: string;
  description: string;
  forms: FormItem[];
  roles: Role[];
  chat: boolean;
  author: string;
}

// 初始化 formData
const initialFormData: AppData = {
  id: uuidv4(),
  name: '回复老板',
  icon: '🔨',
  description: '用婉转的方式回复老板，拒绝PUA从我做起',
  forms: [
    {
      id: uuidv4(),
      name: 'message',
      type: 'ProFormText',
      label: '',
      props: {
        placeholder: '比如说：领导，我不想干这个活',
        type: 'textarea',
        default: "领导，我不想干这个活"
      },
    },
  ],
  roles: [
    {
      id: uuidv4(),
      type: 'system',
      template: '如何委婉巧妙的回复老板，给出5种回答，内容是：',
    },
    {
      id: uuidv4(),
      type: 'user',
      template: '${message}',
    },
  ],
  author: '',
  chat: false
};

export default () => {
  const formRef = useRef<ProFormInstance>();
  const [formData, setFormData] = useState(initialFormData);

  return (
    <div className="home-page-wrapper">
      <Row justify="center" align="middle" style={{minHeight: '100vh'}}>
        <Col xs={24} sm={16} md={12} lg={10} xl={8}>
          <ProCard className="app-creator-wrapper home-page">
            <FormDataContext.Provider value={[formData, setFormData]}>
              <StepsForm<{ name: string; }>
                // stepsProps={{direction: 'vertical'}}
                formRef={formRef}
                onFinish={async () => {
                  // await waitTime(1000);
                  console.log(formData);
                  message.success('提交成功');
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
                  title="创建应用(1/3)"
                  stepProps={{
                    description: '应用基本信息',
                  }}
                  onFinish={async () => {
                    console.log(formRef.current?.getFieldsValue());
                    // await waitTime(2000);
                    return true;
                  }}
                >
                  <CreateInfoPartial/>
                </StepsForm.StepForm>
                <StepsForm.StepForm<{
                  name: string;
                }>
                  name="step2"
                  title="创建应用(2/3)"
                  stepProps={{
                    description: '表单信息',
                  }}
                  onFinish={async () => {
                    console.log(formRef.current?.getFieldsValue());
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
                  title="创建应用(3/3)"
                  stepProps={{
                    description: 'AI角色信息',
                  }}
                  onFinish={async () => {
                    console.log(formRef.current?.getFieldsValue());
                    return true;
                  }}
                >
                  <CreateRolesPartial/>
                </StepsForm.StepForm>
              </StepsForm>
            </FormDataContext.Provider>
          </ProCard>
        </Col>
      </Row>
    </div>
  );
};
