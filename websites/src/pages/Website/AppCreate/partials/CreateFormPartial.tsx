import React, {useContext, useState} from 'react';
// @ts-ignore
import {v4 as uuidv4} from 'uuid';
import {Button, Divider, Collapse} from 'antd';
import {
  ProForm,
  ProFormSelect,
  ProFormText
} from '@ant-design/pro-components';
import {PlusOutlined, MinusOutlined} from '@ant-design/icons';
import FormDataContext from '../FormDataContext';
import './less/common.less';
import './less/createForm.less';

const {Panel} = Collapse;

const formTypes = [
  {
    value: 'ProFormText',
    label: 'Input',
  }
];


const CreateFormPartial: React.FC = () => {
  const [formData, setFormData] = useContext(FormDataContext);
  const handleFormChange = (index: number, key: string, value: any) => {
    const newForms = [...formData.forms];
    newForms[index] = {
      ...newForms[index],
      [key]: value,
    };
    setFormData({...formData, forms: newForms});
  };

  const [remainingChars, setRemainingChars] = useState<{ [key: string]: number }>(
    formData.forms.reduce((acc: { [x: string]: number; }, cur: {
      name: any;
      id: string | number; }) => {
      const currentValueLength = cur.name.length;
      acc[cur.id] = 20 - currentValueLength;
      return acc;
    }, {})
  );

  const [activeCollapseId, setActiveCollapseId] = useState<{ [id: string]: boolean }>({
    [formData.forms[0].id]: false,
  });
  const [collapseStatus, setCollapseStatus] = useState<{ [id: string]: boolean }>(
    formData.forms.reduce((acc: { [x: string]: boolean; }, cur: { id: string | number; }) => {
      acc[cur.id] = true;
      return acc;
    }, {})
  );

  function insertForm(index: number) {
    const newForms = [...formData.forms];
    const newAttrs = {
      id: uuidv4(),
      props: {},
      name: `message${newForms.length}`,
      type: 'ProFormText',
    }
    newForms.splice(index + 1, 0, newAttrs);
    setFormData({...formData, forms: newForms});
    // set the name length
    setRemainingChars((prevState) => ({
      ...prevState,
      [newAttrs.id]: 20 - newAttrs.name.length,
    }));
    // set the collapse status
    setCollapseStatus((prev) => {
      const nextState = {...prev};
      nextState[newAttrs.id] = true;
      return nextState;
    })
  }

  function removeForm(id: string) {
    const newForms = formData.forms.filter((element: any, _: number) => element.id !== id);
    setFormData({...formData, forms: newForms});
  }

  const handleTextChange = (id: string, value: string | any[]) => {
    setRemainingChars((prevState) => ({
      ...prevState,
      [id]: 20 - value.length,
    }));
  };

  const handleButtonClick = (groupId: string) => {
    setActiveCollapseId((prev) => {
      const nextState = {...prev};
      nextState[groupId] = !nextState[groupId];
      return nextState;
    });
    setCollapseStatus((prev) => {
      const nextState = {...prev};
      nextState[groupId] = !nextState[groupId];
      return nextState;
    })
  };

  return (
    <div className="create-form-container">
      <ProForm submitter={false}>
        {formData.forms.map((item: {
          label: any;
          type: any;
          name: any;
          props: any;
          id: string;
        }, index: number) => (
          <Collapse bordered={false}
                    expandIcon={({isActive}) => ""}
                    expandIconPosition="right"
                    accordion ghost className="custom-collapse"
                    activeKey={Object.keys(activeCollapseId).filter((id) => activeCollapseId[id])}
          >
            <Panel
              key={item.id}
              header={
                <ProFormText
                  name={[index, 'name']}
                  label="名称"
                  placeholder="请输入名称"
                  required
                  initialValue={item.name}
                  width="sm"
                  rules={[
                    {required: true, message: '请输入名称'},
                    {max: 20, message: '文本不能超过20个字符'},
                  ]}
                  fieldProps={{
                    value: item.name,
                    onChange: (e: { target: { value: string | any[]; }; }) => {
                      handleTextChange(item.id, e.target.value);
                      handleFormChange(index, 'name', e.target.value);
                    },
                    suffix: `${remainingChars[item.id]} / 20`,
                    addonAfter: (
                      <div>
                        <Button
                          id="expand"
                          onClick={() => handleButtonClick(item.id)}
                          icon={collapseStatus[item.id] ? <PlusOutlined/> : <MinusOutlined/>}>
                          {collapseStatus[item.id] ? '展开' : '收起'}
                        </Button>
                        <div className="form-buttons">
                          <Button
                            type="primary"
                            size="small"
                            icon={<PlusOutlined/>}
                            onClick={() => insertForm(index)}
                            className="form-button"
                          >
                            插入
                          </Button>
                          <Button
                            type="primary"
                            danger
                            size="small"
                            icon={<MinusOutlined/>}
                            onClick={() => removeForm(item.id)}
                            disabled={formData.forms.length === 1}
                            className="form-button"
                          >
                            删除
                          </Button>
                        </div>
                      </div>
                    ),
                    maxLength: 20,
                  }}
                />
              }
            >
              <ProFormText
                name={[index, 'label']}
                label="标签"
                placeholder="请输入标签"
                fieldProps={{
                  maxLength: 20,
                  // value: item.label
                }}
                initialValue={item.label}
              />
              <ProFormSelect
                name={[index, 'type']}
                label="类型"
                options={formTypes}
                placeholder="选择类型"
                rules={[{required: true, message: '类型必填'}]}
                initialValue={item.type}
                fieldProps={{
                  value: item.type
                }}
              />
              <Divider>属性</Divider>

              <ProFormSelect
                name={[index, 'props', 'type']}
                label="输入框类型"
                options={[
                  {
                    value: 'textarea',
                    label: 'Textarea',
                  },
                ]}
                initialValue="textarea"
                fieldProps={{
                  allowClear: true
                }}
              />
              <ProFormText name={[index, 'props', 'placeholder']}
                           label="占位符"
                           fieldProps={{
                             maxLength: 100,
                           }}
                           initialValue={item.props.placeholder}
                           placeholder="请输入占位符"
              />
              <ProFormText
                name={[index, 'props', 'default']}
                label="默认值"
                fieldProps={{
                  maxLength: 100,
                  // value: item.props.default
                }}
                placeholder="请输入默认值"
                initialValue={item.props.default}
              />
            </Panel>

          </Collapse>
        ))}
      </ProForm>
    </div>
  );
}

export default CreateFormPartial;
