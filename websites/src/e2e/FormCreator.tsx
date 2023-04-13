import React from 'react';
import {
  ProForm,
  ProFormSelect,
  ProFormText,
} from '@ant-design/pro-components';
import {Form, Input} from "antd";

interface FormCreatorProps {
  onUpdate: (formItems: any[]) => void;
}

interface FormCreatorState {
  items: any[];
}

class FormCreator extends React.PureComponent<FormCreatorProps, FormCreatorState> {

  handleItemChange = (id: number, key: string, value: any) => {
    const items = this.state.items.map((item) => {
      if (item.id === id) {
        return {...item, [key]: value};
      }
      return item;
    });

    this.setState({items}, () => {
      this.props.onUpdate(items);
    });
  };

  addItem = () => {
    const items = [...this.state.items];
    const newItem = {
      id: items.length,
      label: 'message',
      type: 'input',
      placeholder: '',
      defaultValue: '',
    };
    items.push(newItem);
    this.setState({ items });
  };

  removeItem = (index: number) => {
    const items = this.state.items.filter((_, i) => i !== index);
    this.setState({ items });
  };

  handleSubmit = async (values: Record<string, any>): Promise<void> => {
    console.log('表单数据: ', values);
    // 在这里处理表单数据，例如将数据发送到后端
  };


  renderPreview = () => {
    const { items } = this.state;
    return (
      <Form layout="vertical">
        {items.map((item, index) => {
          if (item.type === 'input') {
            return (
              <Form.Item key={index} label={item.label}>
                <Input
                  placeholder={item.placeholder}
                  defaultValue={item.defaultValue}
                  readOnly
                />
              </Form.Item>
            );
          }
          if (item.type === 'select') {
            return (
              <Form.Item key={index} label={item.label}>
                <ProFormSelect placeholder={item.placeholder} initialValue={item.defaultValue} disabled>
                  {/* 添加选项（options） */}
                </ProFormSelect>
              </Form.Item>
            );
          }
          return null;
        })}
      </Form>
    );
  };

  renderItem = (item: any, index: number) => {
    return (
      <div key={item.id}>
        <ProFormText
          name={`label${item.id}`}
          label="标签"
          initialValue={item.label}
          fieldProps={{
            onChange: (e: any) => this.handleItemChange(item.id, 'label', e.target.value),
          }}
        />
        <ProFormSelect
          name={`type${item.id}`}
          label="类型"
          initialValue={item.type}
          options={[
            {value: 'input', label: 'Input'},
            {value: 'select', label: 'Select'},
          ]}
          fieldProps={{
            onChange: (value: any) => this.handleItemChange(item.id, 'type', value),
          }}
        />
        <ProFormText
          name={`placeholder${item.id}`}
          label="占位符"
          initialValue={item.placeholder}
          fieldProps={{
            onChange: (e: any) => this.handleItemChange(item.id, 'placeholder', e.target.value),
          }}
        />
        <ProFormText
          name={`defaultValue${item.id}`}
          label="默认值"
          initialValue={item.defaultValue}
          fieldProps={{
            onChange: (e: any) => this.handleItemChange(item.id, 'defaultValue', e.target.value),
          }}
        />
        {index > 0 && (
          <button type="button" onClick={() => this.removeItem(index)}>
            删除
          </button>
        )}
        {index === this.state.items.length - 1 && (
          <button type="button" onClick={this.addItem}>
            添加
          </button>
        )}
      </div>
    );
  };

  render() {
    const { items } = this.state;
    return (
      <>
        <ProForm onFinish={this.handleSubmit}>
          {items.map(this.renderItem)}
          <ProForm.Item>
            <button type="submit">提交</button>
          </ProForm.Item>
        </ProForm>
        <div>
          <h3>预览</h3>
          {this.renderPreview()}
        </div>
      </>
    );
  }
}

export default FormCreator;
