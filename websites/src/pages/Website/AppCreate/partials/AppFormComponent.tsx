import {Button, Form, Input, message, Spin, Select} from 'antd';

import {LoadingOutlined} from '@ant-design/icons';
import React, {useContext, useState} from 'react';
import {fetchEventSource,} from "@microsoft/fetch-event-source";
import './less/app-form-component.less'

import md from "@/components/markdown-it"
import FormDataContext from '../FormDataContext';

const {TextArea} = Input;
const {Option} = Select;

const antIcon = <LoadingOutlined style={{fontSize: 24}} spin/>;

let sse: Promise<void> | null = null;
let id: string | number | NodeJS.Timeout | null | undefined = null;

const AppFormComponent: React.FC = () => {

  const [formData] = useContext(FormDataContext);
  const [loading, setLoading] = useState(false);
  const [artifacts, setArtifacts] = useState('');
  const [messages, setMessages] = useState({});
  const [copyIsSuccess, setCopyIsSuccess] = useState(false);
  const [abortController, setAbortController] = useState<AbortController | null>(null);
  const formRef = React.createRef();

  const handleMessage = (parsedData: { message: string; }) => {
    setArtifacts((prevArtifacts) => prevArtifacts + parsedData.message);
  };

  const stopRequest = () => {
    if (abortController) {
      abortController.abort();
      setAbortController(null)
    }
  }

  const buildPayload = (values: any) => {
    return formData.roles.map((role: { template: string; type: string }) => {
      let template = role.template;
      Object.entries(values).forEach(([key, value]) => {
        if (typeof value === "string") {
          template = template.replace(`\${${key}}`, value);
        }
      });
      return {role: role.type, content: template};
    });
  };

  const handleSubmit = (values: any) => {
    console.log("formData.messages:", values);
    if (values && Object.keys(values).length !== 0) {
      const payload = {
        messages: buildPayload(values),
      };
      const ab = new AbortController();
      setLoading(true)
      setArtifacts('')
      setAbortController(ab);

      try {
        sse = fetchEventSource('/api/chat/stream/anonymous',
          {
            headers: {
              'Content-Type': 'application/json',
              Accept: 'text/event-stream',
              'Connection': 'keep-alive',
              'Cache-Control': 'no-cache'
            },
            method: 'POST',
            body: JSON.stringify(payload),
            signal: ab.signal,
            // @ts-ignore
            onopen: response => {
              // 设置 SSE 连接的超时时间为 10 分钟
              id = setTimeout(() => {
                ab.abort()
              }, 10 * 60 * 1000);

              if (response.ok && response.status === 200) {
                console.log("Connection made ", response);
              } else {
                if (response.status >= 400 && response.status < 500 && response.status !== 429) {
                  if (response.status === 403 || response.status === 401) {
                    message.error("网络错误，请重新再次");
                    ab.abort()
                  } else {
                    response.clone().json().then(errorData => {
                      try {
                        message.error(errorData.message);
                        console.log("Client side error ", response);
                      } catch (e) {
                        console.error("Error parsing JSON: ", e);
                      } finally {
                        ab.abort()
                      }
                    }).catch(e => {
                      console.error("Error reading response body: ", e);
                      ab.abort()
                      message.error(e.message);
                    });
                  }
                }
              }
              setLoading(false)
              setArtifacts('')
              setCopyIsSuccess(false)
            },
            onmessage: event => {
              console.log(event.data);
              const parsedData = JSON.parse(event.data);

              handleMessage(parsedData)
            },
            onclose: () => {
              if (id) {
                clearTimeout(id);
              }
              console.log("Connection closed by the server");
            },
            onerror: err => {
              console.log("There was an error from server", err);
              if (sse) {
                stopRequest()
              }
            },
          },
        )
      } catch (error) {
        console.error(error);
      }
    }
  };

  const onCopy = (event: any, value: any) => {
    navigator.clipboard.writeText(value)
    message.success('Result copied to clipboard ✂️')
    setCopyIsSuccess(true)

    setTimeout(() => {
      setCopyIsSuccess(false)
    }, 5000);
  }

  const onSubmitCapture = (e: any) => {
    // @ts-ignore
    const values = formRef.current.getFieldsValue();
    setLoading(true)
    setMessages(values)
    handleSubmit(values);
    e.preventDefault();
  };

  const generateOptions = (options: string[]) =>
    options.map((option, index) => ({
      value: option,
      label: option,
    }));

  const renderFormSelect = (item: any, index: any) => {
    console.log(item.props && item.props.default ? item.props.default : '')
    return (
      <Form.Item key={index} name={item.name} initialValue={item.props && item.props.default ? item.props.default : ''}>
        {
          <Select
            className="options-select"
            style={{width: '100%'}}
            placeholder={item.props.placeholder}
            options={item.props && item.props.options ? generateOptions(item.props.options) : []}
          >
          </Select>

        }
      </Form.Item>
    )
  }

  const renderFormText = (item: any, index: any) => {
    return (
      <Form.Item key={index} name={item.name}
                 rules={[
                   {
                     validator: (_, value) => {
                       const validateValue = value || item.props.default;
                       return validateValue && validateValue.trim() !== ""
                         ? Promise.resolve()
                         : Promise.reject(new Error("请输入你的提示词"));
                     },
                   },
                 ]} initialValue={item.props.default}>
        {
          <TextArea className="form-textarea" placeholder={item.props.placeholder}
                    autoSize={{minRows: 6}}
                    showCount
                    maxLength={800}
                    defaultValue={item.props.default}
          />
        }
      </Form.Item>
    )
  }

  return (
    <div className="container-wrapper">
      <div className="form-container">
        <h1 className="title">{formData.icon} {formData.name}</h1>
        <p className="desc">{formData.description}</p>
        <Form ref={formRef}
              onSubmitCapture={onSubmitCapture}
              onFinish={onSubmitCapture} className="app-form" initialValues={messages}>
          {formData && formData.forms && formData.forms.map((item: {
            name: string | undefined;
            props: {
              default: string | undefined;
              placeholder: string | undefined;
              options: string[] | [];
            };
            type: string | undefined
          }, index: React.Key | null | undefined) => (
            item.type === 'ProFormSelect' ?
              renderFormSelect(item, index) :
              renderFormText(item, index)
          ))}
          <Form.Item>
            <Button type="primary" htmlType="submit" className="form-button">
              预览测试
            </Button>
          </Form.Item>
          <Form.Item className="response-item">
            <div
              className={!!artifacts ? "form-response background-white shadow-div" : "form-response background-default center"}
              onClick={() => onCopy(null, artifacts)}>
              {loading ? (
                <Spin indicator={antIcon} style={{marginTop: 20}}/>
              ) : (
                <div dangerouslySetInnerHTML={{__html: md.render(artifacts)}}/>
              )}
            </div>
          </Form.Item>
          <Form.Item className={!!artifacts ? "" : "hide"}>
            <Button type="default" shape="round"
                    className={copyIsSuccess ? "success-btn copy-button form-button" : "copy-button form-button"}
                    onClick={(event) => onCopy(event, artifacts)}>{copyIsSuccess ? "复制成功" : "全部复制"}</Button>
          </Form.Item>
        </Form>
      </div>
    </div>

  );
}

export default AppFormComponent;
