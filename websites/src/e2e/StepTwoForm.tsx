import React from 'react';
import {
  ProFormText,
  ProFormSelect
} from '@ant-design/pro-components';

import {Button, Row, Col} from 'antd';
import "../pages/Website/AppCreate/partials/index.less"

interface FormItemProps {
  label: string;
  type: 'input' | 'select';
  inputType: 'input' | 'textarea';
  placeholder: string;
  defaultValue: string;
}

interface CustomProFormState {
  formItems: FormItemProps[];
  isExpanded: boolean[];
}

class StepTwoForm extends React.PureComponent<{}, CustomProFormState> {
  state: CustomProFormState = {
    formItems: [
      {
        label: '',
        type: 'input',
        inputType: 'input',
        placeholder: '',
        defaultValue: '',
      },
    ],
    isExpanded: [false],
  };

  handleToggleExpand = (index: number) => {
    const newIsExpanded = [...this.state.isExpanded];
    newIsExpanded[index] = !newIsExpanded[index];
    this.setState({isExpanded: newIsExpanded});
  };

  handleAddFormItem = (index: number) => {
    this.setState((prevState) => ({
      formItems: [
        ...prevState.formItems.slice(0, index + 1),
        {
          label: '',
          type: 'input',
          inputType: 'input',
          placeholder: '',
          defaultValue: '',
        },
        ...prevState.formItems.slice(index + 1),
      ],
      isExpanded: [
        ...prevState.isExpanded.slice(0, index + 1),
        false,
        ...prevState.isExpanded.slice(index + 1),
      ],
    }));
  };

  handleRemoveFormItem = (index: number) => {
    const newFormItems = [...this.state.formItems];
    const newIsExpanded = [...this.state.isExpanded];
    newFormItems.splice(index, 1);
    newIsExpanded.splice(index, 1);
    this.setState({formItems: newFormItems, isExpanded: newIsExpanded});
  };

  renderFormItem = (formItem: FormItemProps, index: number) => {
    const {isExpanded} = this.state;

    return (
      <div className="form-wrapper" key={index}>
        <Row className="operator-wrapper">
          <Col >
            <Button type="primary" onClick={() => this.handleAddFormItem(index)}>
              插入
            </Button>
          </Col>
          <Col>
            <Button
              danger
              onClick={() => this.handleRemoveFormItem(index)}
              disabled={this.state.formItems.length === 1}
            >
              删除
            </Button>
          </Col>
        </Row>
        <Row className="input-wrapper">
          <Col span={20}>
            <ProFormText
              className="main-input"
              label={`输入框 ${index + 1}`}
              name={`input_${index}`}
              rules={[{required: true, message: '输入框不能为空'}]}
            />
          </Col>
          <Col span={4}>
            <Button onClick={() => this.handleToggleExpand(index)}>
              {isExpanded[index] ? '收起' : '展开'}
            </Button>
          </Col>
        </Row>

        {isExpanded[index] && (
          <div>
            <ProFormText label="标签" name={`label_${index}`}/>
            <ProFormSelect
              label="类型"
              name={`type_${index}`}
              options={[
                {value: 'input', label: 'Input'},
                {value: 'select', label: 'Select'},
              ]}
            />
            <ProFormSelect
              label="输入框类型"
              name={`inputType_${index}`}
              options={[
                {value: 'input', label: 'Input'},
                {value: 'textarea', label: 'Textarea'},
              ]}
            />
            <ProFormText label="占位符" name={`placeholder_${index}`}/>
            <ProFormText label="默认值" name={`defaultValue_${index}`}/>
          </div>
        )}
      </div>
    );
  };

  render() {
    const {formItems} = this.state;
    return (
      <div>
        {formItems.map((formItem, index) => (
          this.renderFormItem(formItem, index)
        ))}
      </div>
    );
  }
}

export default StepTwoForm;
