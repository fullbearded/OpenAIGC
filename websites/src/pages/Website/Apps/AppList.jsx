import React from 'react';
import { Row, Col, Button, Input } from 'antd';
import { PlayCircleOutlined, LikeOutlined,RightCircleOutlined } from '@ant-design/icons';
const { Search } = Input;

import { getChildrenToRender } from './utils';

function AppList(props) {
  const { dataSource, isMobile, ...tagProps } = props;
  const { blockWrapper, titleWrapper } = dataSource;
  const childrenToRender = blockWrapper.children.map((item, i) => (
    <Col {...item} key={i.toString()}>
      <div {...item.children}>
        <div className="summary">{item.children.children.map(getChildrenToRender)}</div>
        <div className="scenes">
          <Button
            type=""
            className="like"
            icon={<LikeOutlined />}
            size="middle"
            loading={false}
            block={false}
          >
            点赞
          </Button>
          <Button
            type=""
            className="run"
            icon={<PlayCircleOutlined />}
            size="middle"
            loading={false}
            block={false}
            target="_blank"
            href="/app/xxxx"
          >
            运行
          </Button>
        </div>
      </div>
    </Col>
  ));
  return (
    <div {...tagProps} {...dataSource.wrapper}>
      <div {...dataSource.page}>
        <div className="search-wrapper">
          <div>
            <Search className="search" placeholder="搜索8183款应用" enterButton size="large" allowClear/>
          </div>
        </div>
        <div>
          <Row {...blockWrapper}>{childrenToRender}</Row>
        </div>
        <div className="more-app">
          <Button type="primary" shape="round" className="more-app-button">加载更多</Button>
        </div>
      </div>
    </div>
  );
}

export default AppList;
