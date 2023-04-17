import {
  ProFormText, ProFormTextArea
} from '@ant-design/pro-components';

import FormDataContext from '../FormDataContext';
import {useContext, useState} from "react";
import EmojiPicker from 'emoji-picker-react';

const CreateInfoPartial: React.FC = () => {
  const [formData, setFormData] = useContext(FormDataContext);
  const handleFormChange = (key: string, value: any) => {
    setFormData({...formData, [key]: value});
  };

  const [emojiPickerVisible, setEmojiPickerVisible] = useState(false);

  const handleEmojiPickerVisible = () => {
    setEmojiPickerVisible(!emojiPickerVisible);
  };

  const onEmojiClick = (_: any, emojiObject: any) => {
    setFormData({...formData, icon: emojiObject.emoji})
    setEmojiPickerVisible(false);
  };

  return (
    <div>
      <div onClick={handleEmojiPickerVisible} style={{ position: 'relative' }}>
        <ProFormText
          name="icon"
          label="应用图标"
          width="sm"
          tooltip="应用图标,请输入 emoji 或纯文本"
          placeholder="应用图标,请输入 emoji 或纯文本"
          fieldProps={{
            readOnly: true, // 禁止手动输入
            value: formData.icon,
          }}
          initialValue={formData.icon}
        />
        {emojiPickerVisible && (
          <div style={{ position: 'absolute', zIndex: 1 }}>
            <EmojiPicker onEmojiClick={onEmojiClick} />
          </div>
        )}
      </div>
      <ProFormText
        name="name"
        label="应用名称"
        width="sm"
        tooltip="应用名称"
        placeholder="请输入名称"
        rules={[{required: true}]}
        fieldProps={{
          onChange: (e) => handleFormChange('name', e.target.value),
          // value: formData.name,
        }}
        initialValue={formData.name}
      />
      <ProFormTextArea
        name="description"
        label="描述"
        width="sm"
        placeholder="请输入应用描述"
        rules={[{required: true}]}
        fieldProps={{
          onChange: (e) => handleFormChange('description', e.target.value),
          // value: formData.description,
        }}
        initialValue={formData.description}
      />
    </div>
  )
}

export default CreateInfoPartial;
