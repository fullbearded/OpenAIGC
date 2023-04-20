import React, {useEffect, useState} from 'react';
import { Row, Col, Button } from 'antd';
import { PlayCircleOutlined, LikeOutlined } from '@ant-design/icons';

import { getChildrenToRender } from './utils';
import {likeFreeApp, pageFreeApp} from "@/services/server/api";

function Feature7(props) {
  const perPage = 16;

  const [appPageData, setAppPageData] = useState({page: 1, perPage: 20, content: [], totalPages: 1, total: 100});
  const [like, setLike] = useState({});
  const [showSection, setShowSection] = useState(true);

  useEffect(() => {
    pageFreeApp({recommend: "HOME", perPage: perPage}).then((res) => {
      debugger
      if (res.data.content.length === 0) {
        setShowSection(false);
      }
      setAppPageData(res.data);
    });
  }, {});

  const handleLikeApp = (value) => {
    likeFreeApp({code: value}).then(r => {
      let timeout;
      setLike({...like, [value]: true});
      timeout = setTimeout(() => {
        setLike({...like, [value]: false});
      }, 800);
      return () => {
        clearTimeout(timeout);
      }
    });
  }

  const childrenToRender = appPageData.content.map((item, i) => (
    <Col md={6} xs={24} className="feature7-block" key={item.code}>
      <div className="feature7-block-group">
        <div className="summary">
          <div className="feature7-block-image">{item.icon}</div>
          <h1 className="feature7-block-title">{item.name}</h1>
          <div className="feature7-block-content">{item.description}</div>
        </div>
        <div className="scenes">
          <Button
            type=""
            className={like[item.code] ? "like-success like" : "like"}
            icon={<LikeOutlined/>}
            size="middle"
            loading={false}
            block={false}
            onClick={() => {handleLikeApp(item.code)}}
          >
            点赞
          </Button>
          <Button
            type=""
            className="run"
            icon={<PlayCircleOutlined/>}
            size="middle"
            loading={false}
            block={false}
            href={`/app/${item.code}`}
          >
            运行
          </Button>
        </div>
      </div>
    </Col>
  ));

  const { dataSource, isMobile, ...tagProps } = props;
  const { blockWrapper, titleWrapper } = dataSource;
  return (
      <div {...tagProps} {...dataSource.wrapper} style={showSection ? {} : {display: 'none'}}>
        <div {...dataSource.page}>
          <div {...dataSource.titleWrapper}>{titleWrapper.children.map(getChildrenToRender)}</div>
          <div>
            <Row className="feature7-block-wrapper" gutter={24}>{childrenToRender}</Row>
          </div>
          <div className="more-app">
            <Button type="primary" shape="round" className="more-app-button" href="/apps">查看更多</Button>
          </div>
        </div>
      </div>
  );
}

export default Feature7;
