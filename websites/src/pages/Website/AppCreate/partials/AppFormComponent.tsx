import {Button, Form, Input, message, Select, Spin, Slider} from 'antd';

import {LoadingOutlined} from '@ant-design/icons';
import React, {useContext, useState} from 'react';
import {fetchEventSource,} from "@microsoft/fetch-event-source";
import './less/app-form-component.less'

import md from "@/components/markdown-it"
import FormDataContext from '../FormDataContext';

const {TextArea} = Input;

const antIcon = <LoadingOutlined style={{fontSize: 24}} spin/>;

let sse: Promise<void> | null = null;
let id: string | number | NodeJS.Timeout | null | undefined = null;

const AppFormComponent: React.FC = () => {

  const [formData, setformData] = useContext(FormDataContext);
  const [loading, setLoading] = useState(false);
  const [submitLoading, setSubmitLoading] = useState(false);
  const [artifacts, setArtifacts] = useState('');
  const [messages, setMessages] = useState({});
  const [copyIsSuccess, setCopyIsSuccess] = useState(false);
  const [abortController, setAbortController] = useState<AbortController | null>(null);
  const formRef = React.createRef();

  const handleMessage = (parsedData: { message: string; }) => {
    setArtifacts((prevArtifacts) => prevArtifacts + parsedData.message);
  };

  const stopRequest = () => {
    setLoading(false)
    setSubmitLoading(false)
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
        temperature: formData.temperature,
      };
      const ab = new AbortController();
      setLoading(true)
      setSubmitLoading(true)
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
                  setSubmitLoading(false)
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
              setSubmitLoading(false)
            },
            onerror: err => {
              console.log("There was an error from server", err);
              if (sse) {
                stopRequest()
              }
              setSubmitLoading(false)
            },
          },
        )
      } catch (error) {
        console.error(error);
        setSubmitLoading(false)
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

  const onTemperatureChange = (value: any) => {
    setformData({...formData, temperature: value})
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
    console.log(item.props && item.props.default ? item.props.default : '')
    console.log(item)
    return (
      <Form.Item key={index} name={item.name}
                 rules={[
                   {
                     validator: (_, value) => {
                       const validateValue = value;
                       return validateValue && validateValue.trim() !== ""
                         ? Promise.resolve()
                         : Promise.reject(new Error("请输入你的提示词"));
                     },
                   },
                 ]}
                 initialValue={item.props.default}
      >
        {
          <TextArea className="form-textarea" placeholder={item.props.placeholder}
                    autoSize={{minRows: 6}}
                    showCount
                    maxLength={800}
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
        {/*@ts-ignore*/}
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
          <Form.Item label="随机应变值（值越低，结果越准确；值越高，结果越有创意）">
            <Slider defaultValue={0.8} step={0.1} min={0.1} max={1.5}
                    onChange={onTemperatureChange}
            />
          </Form.Item>
          <Form.Item>
            {
              (<Button type="primary" htmlType="submit" className="form-button" loading={submitLoading}>
                预览测试
              </Button>)
            }
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
