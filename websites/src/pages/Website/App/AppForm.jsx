import {Button, Form, Input, message, notification, Select, Spin, Affix} from 'antd';
import {AppstoreOutlined, LoadingOutlined} from '@ant-design/icons';
import React, {PureComponent} from 'react';
import {fetchEventSource,} from "@microsoft/fetch-event-source";
import { withRouter } from 'react-router-dom';
import './app-form.less'

import md from "@/components/markdown-it"
import {listFreeApp} from "@/services/server/api";
import { enquireScreen } from 'enquire-js';
const {TextArea} = Input;

const antIcon = <LoadingOutlined style={{fontSize: 24}} spin/>;

let sse = null;
let id = null;
let isMobile;
enquireScreen((b) => {
  isMobile = b;
});


class AppForm extends PureComponent {

  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      submitLoading: false,
      artifacts: '',
      formData: {
        forms: [],
      },
      messages: {},
      copyIsSuccess: false,
      code: '',
      abortController: null,
      responseCompleted: false,
      isMobile
    };
    this.messageApi = message;
    this.formRef = React.createRef();

    const { code } = this.props.match.params;
    listFreeApp({ code }).then(res => {
      let messages;
      if (res.data && res.data.length > 0) {
        messages = {}
        res.data[0].forms.map(item => {
          messages[item.name] = item.props.default;
        })

        this.setState({formData: res.data[0], code: code, messages: messages});
        this.formRef.current.setFieldsValue(messages);
      } else {
        notification.error({
          message: '暂无可用应用',
          description: '暂无可用应用',
        });
      }
    });
  }

  componentDidMount() {
    enquireScreen((b) => {
      this.setState({ isMobile: !!b });
    });
  }

  handleSubmit = () => {
    const {formData, messages, code} = this.state;
    console.log("formData.messages:", messages);
    if (messages) {
      const payload = {
        messages: messages,
        code: code
      };

      const abortController = new AbortController();
      this.setState({submitLoading: true, loading: true, artifacts: '', abortController: abortController});

      try {
        sse = fetchEventSource('/api/v2/chat/stream/anonymous',
          {
            headers: {
              'Content-Type': 'application/json',
              Accept: 'text/event-stream',
              'Connection': 'keep-alive',
              'Cache-Control': 'no-cache'
            },
            method: 'POST',
            body: JSON.stringify(payload),
            signal: abortController.signal,
            onopen: response => {
              // 设置 SSE 连接的超时时间为 10 分钟
              id = setTimeout(() => {
                abortController.abort()
              }, 10 * 60 * 1000);

              if (response.ok && response.status === 200) {
                console.log("Connection made ", response);
              } else {
                if (response.status >= 400 && response.status < 500 && response.status !== 429) {
                  if(response.status === 403 || response.status === 401) {
                    this.messageApi.error("网络错误，请重新再次");
                    abortController.abort();
                  } else {
                    response.clone().json().then(errorData => {
                      try {
                        this.messageApi.error(errorData.message);
                        console.log("Client side error ", response);
                      } catch (e) {
                        console.error("Error parsing JSON: ", e);
                      } finally {
                        abortController.abort();
                      }
                    }).catch(e => {
                      console.error("Error reading response body: ", e);
                      abortController.abort();
                      this.messageApi.error(e.message);
                    });
                  }
                }
              }
              this.setState({loading: false, artifacts: '', copyIsSuccess: false});
            },
            onmessage: event => {
              console.log(event.data);
              const parsedData = JSON.parse(event.data);
              this.setState((prevState) => ({artifacts: prevState.artifacts + parsedData.message}));
            },
            onclose: () => {
              if (id) {
                clearTimeout(id);
              }
              console.log("Connection closed by the server");
              this.setState({submitLoading: false})
            },
            onerror: err => {
              console.log("There was an error from server", err);
              if (sse) {
                this.stopRequest()
              }
            },
          },
        )
      } catch (error) {
        console.error(error);
      }
    }
  };

  stopRequest = () => {
    const { abortController, responseCompleted } = this.state;
    if (abortController && !responseCompleted) {
      abortController.abort();
      this.setState({ abortController: null });
    }
  }

  onFinish = () => {
    const { formData } = this.state;
    const values = this.formRef.current.getFieldsValue();
    console.log("formdata: " + values);
    this.setState({
      loading: true,
      messages: values
    });
    console.log("formdata: " + values);
    this.handleSubmit()
    return false;
  };

  onCopy = (event, value) => {
    navigator.clipboard.writeText(value)
    this.messageApi.success('Result copied to clipboard ✂️')
    this.setState({copyIsSuccess: true})

    setTimeout(() => {
      this.setState({copyIsSuccess: false})
    }, 5000);
  }

  renderFormSelect = (item, index) => {
    console.log(item.props && item.props.default ? item.props.default : '')
    return (
      <Form.Item key={index} name={item.name} initialValue={item.props && item.props.default ? item.props.default : ''}>
        {
          <Select
            className="options-select"
            style={{width: '100%'}}
            placeholder={item.props.placeholder}
            options={item.props && item.props.options ? this.generateOptions(item.props.options) : []}
          >
          </Select>

        }
      </Form.Item>
    )
  }

  renderFormText = (item, index) => {
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

  generateOptions = (options) =>
    options.map((option, index) => ({
      value: option,
      label: option,
    }));

  render() {
    const {loading, formData, artifacts, copyIsSuccess, messages, isMobile, submitLoading} = this.state;
    return (
      <div className="container-wrapper">
        {isMobile ? (
            <Affix style={{position: 'absolute', top: '80px', left: '20px', zIndex: 1000}}>
              <Button size="large" icon={<AppstoreOutlined />}  href="/apps">应用列表</Button>
            </Affix>
        ) : (
          <Button type="link" icon={<AppstoreOutlined />} className="back-link" href="/apps">返回应用列表</Button>
        )}
        <div className="form-container" style={isMobile ? {marginTop: "40px"} : {}}>
          <h1 className="title">{formData.icon}{formData.name}</h1>
          <p className="desc">{formData.description}</p>

          <Form ref={this.formRef} onFinish={this.onFinish} className="app-form" initialValues={messages}>
            {formData && formData.forms && formData.forms.map((item, index) => (
              item.type === 'ProFormSelect' ?
                this.renderFormSelect(item, index) :
                this.renderFormText(item, index)
            ))}
            <Form.Item>
              <Button type="primary" htmlType="submit" className="form-button" loading={submitLoading}>
                提交
              </Button>
            </Form.Item>
            <Form.Item className="response-item">
              <div
                className={!!artifacts ? "form-response background-white shadow-div" : "form-response background-default center"}
                onClick={() => this.onCopy(null, artifacts)}>
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
                      onClick={(event) => this.onCopy(event, artifacts)}>{copyIsSuccess ? "复制成功" : "全部复制"}</Button>
            </Form.Item>
          </Form>
        </div>
      </div>

    );
  }
}

export default withRouter(AppForm);
