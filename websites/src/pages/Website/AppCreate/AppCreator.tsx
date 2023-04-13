import React from 'react';
import {message} from 'antd';
import {StepsForm} from '@ant-design/pro-components';

import {
  ProFormSelect,
  ProFormText,
  ProForm,
  ProCard
} from '@ant-design/pro-components';


import FormCreator from '../../../e2e/FormCreator'

interface AppState {
  current: number;
  formData: any;
}

class AppCreator extends React.PureComponent<{}, AppState> {
  constructor(props: {}) {
    super(props);
    this.state = {
      current: 0,
      formData: {},
    };
  }

  steps = [
    {
      title: '填写APP信息',
      content: (
        <ProForm onFinish={(values) => this.onFinish(0, values)}>
          {/* 这里实现点击图标选择功能，需要另外实现一个弹窗或下拉菜单来展示图标库 */}
          <ProFormText name="icon" label="图标" initialValue="<AppOutlined />"/>
          <ProFormText name="appName" label="APP名称"/>
          <ProFormText name="description" label="描述"/>
        </ProForm>
      ),
    },
    {
      title: '创建工具',
      content: (
        <ProForm onFinish={(values) => this.onFinish(1, values)}>
          <ProForm.Item name="formInfo">
            <FormCreator onUpdate={(items) => console.log(items)}/>
          </ProForm.Item>
        </ProForm>
      ),
    },
    {
      title: '设定角色',
      content: (
        <ProForm onFinish={(values) => this.onFinish(2, values)}>
          <ProFormSelect
            name="role"
            label="角色"
            options={[
              {value: 'user', label: 'User'},
              {value: 'system', label: 'System'},
              {value: 'assistant', label: 'Assistant'},
            ]}
          />
          <ProFormText name="variable" label="变量输入框"/>
        </ProForm>
      ),
    },
  ];

  onFinish = async (current: number, values: any) => {
    const formData = {...this.state.formData, ...values};
    if (current === this.steps.length - 1) {
      // 调用后端接口进行保存
      fetch('http://localhost:8080/api/app/create', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(formData),
      })
        .then((res) => res.json())
        .then((data) => {
          message.success('创建成功');
        })
        .catch((error) => {
          message.error('创建失败');
        });
    } else {
      this.setState({current: current + 1, formData});
    }
  };

  render() {
    const {current} = this.state;
    return (
      <div className="home-page-wrapper">
        <ProCard className="app-creator-wrapper home-page">
          <StepsForm current={current}>
            {this.steps.map((item, index) => (
              <StepsForm.StepForm key={index} title={item.title}/>
            ))}
          </StepsForm>
          <div className="steps-content">{this.steps[current].content}</div>
        </ProCard>
      </div>
    )
  }
}

export default AppCreator;
