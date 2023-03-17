import React from 'react';
import { Row, Col, Button } from 'antd';
import { PlayCircleOutlined, LikeOutlined } from '@ant-design/icons';

import { getChildrenToRender } from './utils';

function Feature7(props) {
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
            href="/scenes/xxxx"
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
        <div {...dataSource.titleWrapper}>{titleWrapper.children.map(getChildrenToRender)}</div>
        <div>
          <Row {...blockWrapper}>{childrenToRender}</Row>
        </div>
      </div>
    </div>
  );
}

export default Feature7;
