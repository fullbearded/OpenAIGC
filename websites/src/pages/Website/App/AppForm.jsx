import {Button, Form, Input, message, Spin, Typography} from 'antd';
import {AppstoreOutlined, LoadingOutlined} from '@ant-design/icons';
import React, {PureComponent} from 'react';
import {fetchEventSource,} from "@microsoft/fetch-event-source";
import './app-form.less'

import md from "@/components/markdown-it"

const {TextArea} = Input;

const antIcon = <LoadingOutlined style={{fontSize: 24}} spin/>;

let sse = null;
let id = null;

class AppForm extends PureComponent {

  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      artifacts: '',
      formData: {prompt: ''},
      copyIsSuccess: false
    };
    this.messageApi = message;
  }

  handleSubmit = () => {
    const {formData} = this.state;
    if (formData.prompt) {
      const payload = {
        prompt: formData.prompt,
        user: 'user',
      };

      this.setState({loading: true, artifacts: ''});

      try {
        sse = fetchEventSource('http://localhost:8080/api/chat/stream',
          {
            headers: {
              'Content-Type': 'application/json',
              Accept: 'text/event-stream',
              'Connection': 'keep-alive',
              'Cache-Control': 'no-cache',
              Authorization: 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImNvZGUiOiI3YzhhZWE5ZDA0ZTcwYjNmM2RmMjg4MTVmNmU4YTJhYiIsImlhdCI6MTY4MDg1Nzc4MSwiZXhwIjoxNjgwOTQ0MTgxfQ.njwMFk2BPq1ShUkz08P8i88DWiFPq2BV4A6J9cYsa6k',
            },
            method: 'POST',
            body: JSON.stringify(payload),
            onopen: response => {
              // 设置 SSE 连接的超时时间为 10 分钟
              id = setTimeout(() => {
                if (sse) {
                  sse.close();
                  sse = null;
                }
              }, 10 * 60 * 1000);

              if (response.ok && response.status === 200) {
                this.setState({loading: false, artifacts: '', copyIsSuccess: false});
                console.log("Connection made ", response);
              } else if (
                response.status >= 400 &&
                response.status < 500 &&
                response.status !== 429
              ) {
                let resp = JSON.parse(response);
                this.messageApi.error(resp.message);
                console.log("Client side error ", response);
              }
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
            },
            onerror: err => {
              console.log("There was an error from server", err);
            },
          },
        )
      } catch (error) {
        console.error(error);
      }
    }
  };

  stopRequest = () => {
    if (sse) {
      sse.close();
      sse = null;
    }
  }

  onFinish = (values) => {
    this.setState({
      loading: true,
      formData: values
    });
    this.handleSubmit()
  };

  onCopy = (event, value) => {
    navigator.clipboard.writeText(value)
    this.messageApi.success('Result copied to clipboard ✂️')
    this.setState({copyIsSuccess: true})

    setTimeout(() => {
      this.setState({copyIsSuccess: false})
    }, 5000);
  }

  render() {
    const {loading, formData, artifacts, copyIsSuccess} = this.state;
    return (
      <div className="container-wrapper">
        <Button type="link" icon={<AppstoreOutlined/>} className="back-link" href="/apps">返回应用列表</Button>
        <div className="form-container">
          <h1 className="title">1231</h1>
          <p className="desc">12312312。</p>

          <Form onFinish={this.onFinish} className="app-form" initialValues={formData}>
            <Form.Item name="prompt" rules={[{required: true, message: 'Please input your prompt'}]}>
              <TextArea className="form-textarea" placeholder="Autosize height based on content lines"
                        autoSize={{minRows: 6}}
                        showCount
                        maxLength={800}
              />
            </Form.Item>
            <Form.Item>
              <Button type="primary" htmlType="submit" className="form-button">
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

export default AppForm;
