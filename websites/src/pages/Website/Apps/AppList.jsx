import React, {useEffect, useState} from 'react';
import {Row, Col, Button, Input} from 'antd';
import {PlayCircleOutlined, LikeOutlined, RightCircleOutlined} from '@ant-design/icons';

const {Search} = Input;

import {likeFreeApp, pageFreeApp} from "@/services/server/api";

function AppList(props) {
  const perPage = 20;

  const [appPageData, setAppPageData] = useState({page: 1, perPage: 20, content: [], totalPages: 1, total: 100});
  const [displayLoadMore, setDisplayLoadMore] = useState(true);
  const [searchValue, setSearchValue] = useState('');
  const [like, setLike] = useState({});

  useEffect(() => {
    pageFreeApp({perPage: perPage}).then((res) => {
      setAppPageData(res.data);
      if (res.data.page >= res.data.totalPages)  {
        setDisplayLoadMore(false)
      }
    });
  }, {});

  const loadMore = () => {
    let param = {page: appPageData.page + 1, perPage: perPage, nameLike: searchValue}
    pageFreeApp(param).then((res) => {
      setAppPageData({...res.data, page: res.data.page, content: appPageData.content.concat(res.data.content)});
      if (res.data.page >= res.data.totalPages)  {
        setDisplayLoadMore(false)
      }
    });
  };

  const handleSearch = (value) => {
    setDisplayLoadMore(true);
    setSearchValue(value);
    pageFreeApp({page: 1, nameLike: value, perPage: perPage}).then((res) => {
      setAppPageData(res.data);
      if (res.data.page >= res.data.totalPages)  {
        setDisplayLoadMore(false)
      }
    });
  }

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

  const {dataSource, isMobile, ...tagProps} = props;
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
  return (
    <div {...tagProps} className="home-page-wrapper feature7-wrapper">
      <div className="home-page feature7">
        <div className="search-wrapper">
          <div>
            <Search className="search"
                    placeholder={`搜索${appPageData.total}款应用`}
                    enterButton
                    size="large"
                    onSearch={handleSearch}
                    allowClear/>
          </div>
        </div>
        <div>
          <Row className="feature7-block-wrapper" gutter={24}>{childrenToRender}</Row>
        </div>
        {displayLoadMore && (
          <div className="more-app">
            <Button type="primary" shape="round" className="more-app-button" onClick={loadMore}>加载更多</Button>
          </div>
        )}
      </div>
    </div>
  );
}

export default AppList;
