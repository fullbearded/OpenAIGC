import {useState} from 'react';
// @ts-ignore
import {v4 as uuidv4} from 'uuid';
import {Button, Divider, Collapse} from 'antd';
import {
  ProForm,
  ProFormDependency,
  ProFormSelect,
  ProFormText
} from '@ant-design/pro-components';
import {PlusOutlined, MinusOutlined} from '@ant-design/icons';

import './index.less';

const {Panel} = Collapse;

const formTypes = [
  {
    value: 'ElInput',
    label: 'Input',
  },
  {
    value: 'ElSelect',
    label: 'Select',
  },
];

interface FormItem {
  id: string;
  props: any;
  name: string;
  type: string;
}

function CreateForm() {
  const [form] = ProForm.useForm();
  const [remaining, setRemaining] = useState(0);
  const [forms, setForms] = useState<FormItem[]>([
    {
      id: uuidv4(),
      props: {},
      name: 'message0',
      type: 'ElInput',
    },
  ]);
  const [activeCollapseId, setActiveCollapseId] = useState<{ [id: string]: boolean }>({
    [forms[0].id]: false,
  });
  const [collapseStatus, setCollapseStatus] = useState<{ [id: string]: boolean }>(
    forms.reduce((acc, cur) => {
      acc[cur.id] = true;
      return acc;
    }, {})
  );


  function insertForm(index: number) {
    const newForms = [...forms];
    newForms.splice(index + 1, 0, {
      id: uuidv4(),
      props: {},
      name: `message${newForms.length}`,
      type: 'ElInput',
    });
    setForms(newForms);
  }

  function removeForm(index: number) {
    const newForms = [...forms];
    newForms.splice(index, 1);
    setForms(newForms);
  }

  const handleTextChange = (value: string | any[]) => {
    setRemaining(value.length);
  };

  const handleButtonClick = (groupId: string) => {
    setActiveCollapseId((prev) => {
      const nextState = {...prev};
      nextState[groupId] = !nextState[groupId];
      return nextState;
    });
  };

  return (
    <div className="create-form-container">
      <h3 className="create-form-title">Create Forms</h3>
      <Collapse bordered={false}
                expandIcon={({isActive}) => ""}
                expandIconPosition="right"
                accordion ghost className="custom-collapse"
                activeKey={Object.keys(activeCollapseId).filter((id) => activeCollapseId[id])}
      >
        {forms.map((item, index) => (
          <Panel
            key={item.id}
            header={
              <ProFormText
                name={[index, 'name']}
                label="名称"
                placeholder="请输入名称"
                required
                initialValue="message"
                onChange={(e: { target: { value: string | any[]; }; }) => handleTextChange(e.target.value)}
                rules={[
                  {required: true, message: '请输入名称'},
                  {max: 20, message: '文本不能超过20个字符'},
                ]}
                fieldProps={{
                  suffix: `${remaining} / 20`,
                  addonAfter: (
                    <Button id="expand"
                            onClick={() => handleButtonClick(item.id)}
                            icon={collapseStatus[item.id] ?  <MinusOutlined/> : <PlusOutlined/>}>
                      {collapseStatus[item.id] ? '收齐' : '展开'}
                    </Button>
                  ),
                  maxLength: 20,
                }}
              />
            }
          >
            <ProFormText
              name={[index, 'label']}
              label="Label"
              fieldProps={{
                maxLength: 20,
              }}
            />
            <ProFormSelect
              name={[index, 'type']}
              label="Type"
              options={formTypes}
              placeholder="Select form type"
              rules={[{required: true, message: 'Type is required'}]}
            />
            <Divider>Props</Divider>
            <ProFormDependency name={[index, 'type']}>
              {({type}) => (
                <>
                  {type === 'ElInput' && (
                    <ProFormSelect
                      name={[index, 'props', 'type']}
                      label="Input Type"
                      options={[
                        {
                          value: 'textarea',
                          label: 'Textarea',
                        },
                      ]}
                      fieldProps={{
                        allowClear: true,
                      }}
                    />
                  )}
                  {type === 'ElSelect' && (
                    <ProFormSelect
                      name={[index, 'props', 'options']}
                      label="Options"
                      fieldProps={{
                        mode: 'multiple',
                        allowClear: true,
                        showSearch: true,
                        optionFilterProp: 'children',
                        placeholder: 'Add options',
                      }}
                    />
                  )}
                  <ProFormText name={[index, 'props', 'placeholder']}
                               label="Placeholder"
                               fieldProps={{
                                 maxLength: 100,
                               }}
                  />
                  <ProFormText
                    name={[index, 'props', 'default']}
                    label="Default Value"
                    fieldProps={{
                      maxLength: 100,
                    }}
                  />
                </>
              )}
            </ProFormDependency>
            <div className="form-buttons">
              <Button
                type="primary"
                icon={<PlusOutlined/>}
                onClick={() => insertForm(index)}
                className="form-button"
              >
                Insert
              </Button>
              <Button
                danger
                icon={<MinusOutlined/>}
                onClick={() => removeForm(index)}
                disabled={forms.length === 1}
                className="form-button"
              >
                Delete
              </Button>
            </div>
          </Panel>
        ))}
      </Collapse>
    </div>
  );
}

export default CreateForm;
