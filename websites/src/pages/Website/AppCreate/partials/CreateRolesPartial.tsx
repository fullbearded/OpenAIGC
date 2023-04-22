import React, {useMemo} from 'react';
import FormDataContext from '../FormDataContext';
import {useContext} from "react";
// @ts-ignore
import {v4 as uuidv4} from 'uuid';
import {
  ProFormSelect,
  ProFormTextArea,
  ProForm,
  ProCard,
} from '@ant-design/pro-components';
import {Button, Alert, Tag} from 'antd';
import {MinusOutlined, PlusOutlined} from "@ant-design/icons";
import './less/common.less'
import './less/createRoles.less'


const roleOptions = [
  {
    value: 'system',
    name: 'System',
  },
  {
    value: 'user',
    name: 'User',
  },
  {
    value: 'assistant',
    name: 'Assistant',
  },
];

const CreateRolePartial: React.FC = () => {
  const [formData, setFormData] = useContext(FormDataContext);
  const updateRole = (callback: (roles: any) => any) => {
    setFormData((prevFormData: { roles: any; }) => {
      const newRoles = callback(prevFormData.roles);
      return { ...prevFormData, roles: newRoles };
    });
  };

  const handleFormChange = (id: React.Key | null | undefined, key: string, value: any) => {
    updateRole((roles) => {
      const index = roles.findIndex((v: { id: React.Key | null | undefined; }) => v.id === id);
      const updatedRole = { ...roles[index], [key]: value, index: index };
      return [...roles.slice(0, index), updatedRole, ...roles.slice(index + 1)];
    });
  };
  const messageOrderWarning = useMemo(() => {
    const roles = formData.roles;
    const firstSystem = roles.find((v: { type: string; }) => v.type === 'system');
    if (firstSystem && firstSystem !== roles[0]) {
      return '通常，对话会以系统消息作为开头。';
    } else if (
      roles.length > 1 &&
      roles.some((role: { type: any; }, index: number, arr: { type: any; }[]) => arr[index - 1]?.type === role.type)
    ) {
      return '通常，对话会在用户和助手之间交替进行。';
    } else if (roles[roles.length - 1].type !== 'user') {
      return '通常，最后一条对话是用户发出的。';
    } else {
      return '';
    }
  }, [formData.roles, formData.roles.length]);

  const insertRole = (index: number) => {
    const newRoles = [...formData.roles];
    const prevRoleType = newRoles[index].type;
    let newRoleType = 'user';
    if (prevRoleType === 'system' || prevRoleType === 'assistant') {
      newRoleType = 'user';
    } else if (prevRoleType === 'user') {
      newRoleType = 'assistant';
    }
    const newRole = {
      id: uuidv4(),
      template: '',
      type: newRoleType,
    };
    updateRole((roles) => {
      return [...roles.slice(0, index + 1), newRole, ...roles.slice(index + 1)];
    });
  };

  const removeRole = (id: React.Key | null | undefined) => {
    const newForms = formData.roles.filter((element: any, _: number) => element.id !== id);
    setFormData({...formData, roles: newForms});
  };

  const appendVariable = (index: number, _var: string) => {
    const newRoles = [...formData.roles];
    newRoles[index].template += `\${${_var}}`;

    setFormData({...formData, roles: newRoles});
  };

  const roleEnum = roleOptions.reduce((obj, item) => {
    obj[item.value] = item.name;
    return obj;
  }, {});

  return (
    <ProCard className="create-roles-from-wrapper">
      {messageOrderWarning ? (
        <Alert
          message={messageOrderWarning}
          type="warning"
          showIcon
          className="mb-2"
        />
      ) : (
        <Alert
          message="会话格式正确"
          type="success"
          showIcon
          className="mb-2"
        />
      )
      }
      <ProForm submitter={false}>
        {formData.roles.map((item: { id: React.Key | null | undefined; type: any; template: any;  index: any;}, index: number) => (
          <React.Fragment key={item.id}>
            <div className="item-wrapper">
              <div className="form-buttons">
                <Button
                  type="primary"
                  size="small"
                  icon={<PlusOutlined/>}
                  onClick={() => insertRole(index)}
                  className="form-button"
                >
                  插入
                </Button>
                <Button
                  type="primary"
                  danger
                  size="small"
                  icon={<MinusOutlined/>}
                  onClick={() => removeRole(item.id)}
                  disabled={formData.roles.length === 1}
                  className="form-button"
                >
                  删除
                </Button>
              </div>
            </div>
            <ProForm.Group>
              <ProFormSelect
                className="role-type-select"
                width="sm"
                label="角色"
                valueEnum={roleEnum}
                name={`roles[${index}].type`}
                initialValue={item.type}
                fieldProps={{
                  onSelect: (value) => {
                    handleFormChange(item.id, "type", value);
                  },
                  value: item.type
                }}
              />
              <div>
                <ProForm.Item label='模板'>
                  <div className="variables">
                    变量：
                    {formData.forms.map((form: { id: React.Key | null | undefined; name: any; }, formIndex: number) => (
                      <Tag color="blue"
                           onClick={() => {
                             appendVariable(index, form.name);
                           }}
                      >{form.name}</Tag>
                    ))}
                  </div>
                  <ProFormTextArea
                    width="sm"
                    name={`roles[${index}].template`}
                    initialValue={item.template}
                    placeholder={'请输入模板'}
                    fieldProps={{
                      onChange: (e) => {
                        handleFormChange(item.id, 'template', e.target.value);
                      },
                      value: item.template
                    }}
                  />
                </ProForm.Item>
              </div>
            </ProForm.Group>
          </React.Fragment>
        ))}
      </ProForm>
    </ProCard>
  );
};

export default CreateRolePartial;
