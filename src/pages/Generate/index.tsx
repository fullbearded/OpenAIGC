import { Form, Input, Button, Spin } from 'antd';
import { LoadingOutlined } from '@ant-design/icons';
import React, { PureComponent } from 'react';

const antIcon = <LoadingOutlined style={{ fontSize: 24 }} spin />;

class App extends PureComponent {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
    };
  }

  onFinish = (values) => {
    this.setState({ loading: true });
    // make API call here
    setTimeout(() => {
      this.setState({ loading: false });
    }, 2000);
  };

  formStyle = {
    width: '100%',
    maxWidth: 400,
    paddingLeft: '20px',
    paddingRight: '20px'
  }

  buttonStyle = {
    width: '100%',
    backgroundColor: '#1890ff',
    color: 'white',
    borderRadius: '10px',
    paddingLeft: '20px',
    paddingRight: '20px'
  }

  responseStyle = {
    marginTop: 20,
    backgroundColor: 'white',
    width: '100%',
    height: '200px',
    borderRadius: '10px',
    paddingLeft: '20px',
    paddingRight: '20px',
    maxWidth: 400
  }

  containerStyle = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    gap: '20px'
  }

  titleStyle = {
    fontSize: '2rem',
    textAlign: 'center'
  }

  paragraphStyle = {
    fontSize: '1.2rem',
    textAlign: 'center',
    maxWidth: 400
  }

  textAreaStyle = {
    width: '100%',
    height: '500px',
    paddingLeft: '20px',
    paddingRight: '20px',
    maxWidth: 400
  }

  render() {
    const { loading } = this.state;
    return (
      <div style={this.containerStyle}>
        <a href="#" className="back-link">返回应用列表</a>
        <h1 style={this.titleStyle}>周报生成器</h1>

        <p style={{...this.paragraphStyle, maxWidth: 400}}>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed euismod, sapien vel bibendum bibendum, velit sapien bibendum sapien, vel bibendum sapien velit sed sapien. Sed euismod, sapien vel bibendum bibendum, velit sapien bibendum sapien, vel bibendum sapien velit sed sapien.</p>
        <Form onFinish={this.onFinish} style={{...this.formStyle, width: '300px'}}>
          <Form.Item name="text">
            <Input.TextArea rows={10} style={{ ...this.textAreaStyle, maxWidth: 400 }} placeholder="Lorem ipsum dolor sit amet, consectetur adipiscing elit." />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" style={this.buttonStyle}>
              Submit
            </Button>
          </Form.Item>
        </Form>
        {loading ? (
          <Spin indicator={antIcon} style={{ marginTop: 20 }} />
        ) : (
          <div style={this.responseStyle}>Response will be displayed here</div>
        )}
      </div>
    );
  }
}

export default App;
