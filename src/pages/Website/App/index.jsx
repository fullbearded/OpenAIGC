import React from 'react';
import { enquireScreen } from 'enquire-js';
import Nav3 from '@/pages/Website/Common/Nav3';
import Footer1 from "@/pages/Website/Common/Footer1";
import { Nav30DataSource, Footer10DataSource } from '../Common/data.source';
import AppForm from './AppForm';

import './index.less'


let isMobile;
enquireScreen((b) => {
  isMobile = b;
});

export default class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isMobile,
      show: !location.port, // 如果不是 dva 2.0 请删除
    };
  }

  componentDidMount() {
    // 适配手机屏幕;
    enquireScreen((b) => {
      this.setState({ isMobile: !!b });
    });
    if (location.port) {
      setTimeout(() => {
        this.setState({
          show: true,
        });
      }, 500);
    }
  }

  render() {
    const children = [
      <Nav3 id="Nav3_0" key="Nav3_0" dataSource={Nav30DataSource} isMobile={this.state.isMobile} />,
      <AppForm />,
      <Footer1
        id="Footer1_0"
        key="Footer1_0"
        dataSource={Footer10DataSource}
        isMobile={this.state.isMobile}
      />,
    ];

    return (
      <div
        className="app-top-wrapper"
        ref={(d) => {
          this.dom = d;
        }}
      >
        {/* 如果不是 dva 2.0 替换成 {children} start */}
        {this.state.show && children}
        {/* 如果不是 dva 2.0 替换成 {children} end */}
      </div>
    );
  }
}
