import type {ProFormInstance} from '@ant-design/pro-components';
import {
  ProCard,
  ProForm,
  ProFormCheckbox,
  ProFormDatePicker,
  ProFormText,
  ProFormTextArea,
  StepsForm,
} from '@ant-design/pro-components';
import {message} from 'antd';
import {useRef} from 'react';

// import StepTwoForm from "./partials/StepTwoForm";
import CreateForm from "@/pages/Website/AppCreate/partials/CreateForm";

// const waitTime = (time: number = 100) => {
//   return new Promise((resolve) => {
//     setTimeout(() => {
//       resolve(true);
//     }, time);
//   });
// };

export default () => {
  const formRef = useRef<ProFormInstance>();

  return (
    <div className="home-page-wrapper">
      <ProCard className="app-creator-wrapper home-page">
        <StepsForm<{ name: string; }>
          // stepsProps={{direction: 'vertical'}}
          formRef={formRef}
          onFinish={async () => {
            // await waitTime(1000);
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
            name="base"
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
            <ProFormText
              name="icon"
              label="应用图标"
              width="md"
              tooltip=""
              placeholder="请输入名称"
            />
            <ProFormText
              name="name"
              label="应用名称"
              width="md"
              tooltip="最长为 24 位，用于标定的唯一 id"
              placeholder="请输入名称"
              rules={[{required: true}]}
            />
            <ProFormTextArea name="description" label="描述" width="lg" placeholder="请输入应用描述"
                             rules={[{required: true}]}
            />
          </StepsForm.StepForm>
          <StepsForm.StepForm<{
            checkbox: string;
          }>
            name="checkbox"
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
                {/*<StepTwoForm/>*/}
                <CreateForm/>
              </div>
            </ProForm.Group>
          </StepsForm.StepForm>
          <StepsForm.StepForm<{
            checkbox: string;
          }>
            name="step3"
            title="第三步"
            stepProps={{
              description: '这里填入运维参数',
            }}
            onFinish={async () => {
              console.log(formRef.current?.getFieldsValue());
              return true;
            }}
          >
            <ProFormCheckbox.Group
              name="checkbox"
              label="迁移类型"
              width="lg"
              options={['结构迁移', '全量迁移', '全量校验', '增量替换']}
            />
            <ProForm.Group>
              <ProFormText name="dbname" label="业务 DB 用户名"/>
              <ProFormDatePicker name="datetime" label="记录保存时间" width="sm"/>
            </ProForm.Group>
            <ProFormCheckbox.Group
              name="checkbox"
              label="迁移类型"
              options={['完整 LOB', '不同步 LOB', '受限制 LOB']}
            />
          </StepsForm.StepForm>
        </StepsForm>
      </ProCard>
    </div>
  );
};
