import {Form, Input, Button, Spin} from 'antd';
import {LoadingOutlined, AppstoreOutlined} from '@ant-design/icons';
import React, {PureComponent} from 'react';

const { TextArea } = Input;

const antIcon = <LoadingOutlined style={{fontSize: 24}} spin/>;

import './app-form.less'

class AppForm extends PureComponent {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
    };
  }

  onFinish = (values) => {
    this.setState({loading: true});
    // make API call here
    setTimeout(() => {
      this.setState({loading: false});
    }, 2000);
  };

  render() {
    const {loading} = this.state;
    return (
      <div className="container-wrapper">
        <Button type="link" icon={<AppstoreOutlined />} className="back-link">返回应用列表</Button>
        <div className="form-container">
          <h1 className="title">1231</h1>
          <p className="desc">输入工作内容，小助手帮你快速完成周报。</p>

          <Form onFinish={this.onFinish} className="app-form">
            <Form.Item name="text">
              <TextArea  className="form-textarea" placeholder="Autosize height based on content lines"
                         autoSize={{minRows: 6}}
                         showCount
                         maxLength={800}
              />
            </Form.Item>
            <Form.Item>
              <Button type="primary" htmlType="submit" className="form-button">
                Submit
              </Button>
            </Form.Item>
          </Form>
          {loading ? (
            <Spin indicator={antIcon} style={{marginTop: 20}}/>
          ) : (
            <div className="form-response">Response will be displayed here</div>
          )}
        </div>
      </div>

    );
  }
}

export default AppForm;
