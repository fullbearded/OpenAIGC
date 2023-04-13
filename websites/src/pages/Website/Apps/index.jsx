/* eslint no-undef: 0 */
/* eslint arrow-parens: 0 */
import React from 'react';
import {enquireScreen} from 'enquire-js';

import {Nav30DataSource, Footer10DataSource} from '../Common/data.source';
import Nav3 from '@/pages/Website/Common/Nav3';
import Footer1 from '@/pages/Website/Common/Footer1';

import {
  Content110DataSource,
  Feature70DataSource,
} from './data.source';

import Banner from './BannerFragment'
import AppList from './AppList';

import './less/antMotionStyle.less';

let isMobile;
enquireScreen((b) => {
  isMobile = b;
});

const {location = {}} = typeof window !== 'undefined' ? window : {};

export default class Home extends React.Component {
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
      this.setState({isMobile: !!b});
    });
    // dva 2.0 样式在组件渲染之后动态加载，导致滚动组件不生效；线上不影响；
    /* 如果不是 dva 2.0 请删除 start */
    if (location.port) {
      // 样式 build 时间在 200-300ms 之间;
      setTimeout(() => {
        this.setState({
          show: true,
        });
      }, 500);
    }
    /* 如果不是 dva 2.0 请删除 end */
  }

  render() {
    const children = [
      <Nav3 id="Nav3_0" key="Nav3_0" dataSource={Nav30DataSource} isMobile={this.state.isMobile}/>,
      <Banner
        id="Content11_0"
        key="Content11_0"
        dataSource={Content110DataSource}
        isMobile={this.state.isMobile}
      />,
      <AppList
        id="Feature7_0"
        key="Feature7_0"
        dataSource={Feature70DataSource}
        isMobile={this.state.isMobile}
      />,
      <Footer1
        id="Footer1_0"
        key="Footer1_0"
        dataSource={Footer10DataSource}
        isMobile={this.state.isMobile}
      />,
    ];
    return (
      <div
        className="templates-wrapper"
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
