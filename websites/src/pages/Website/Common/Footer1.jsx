import React from 'react';
import TweenOne from 'rc-tween-one';
import OverPack from 'rc-scroll-anim/lib/ScrollOverPack';
import QueueAnim from 'rc-queue-anim';
import {Row, Col, Button} from 'antd';
import {isImg} from '../Home/utils';

const getChildrenToRender = (item, i) => {
  let tag = item.name.indexOf('title') === 0 ? 'h1' : 'div';
  tag = item.href ? 'a' : tag;
  let children =
    typeof item.children === 'string' && item.children.match(/\.(png|jpg|gif|jpeg|webp)$/)
      ? React.createElement('img', {src: item.children, alt: 'img'})
      : item.children;
  if (item.name.indexOf('button') === 0 && typeof item.children === 'object') {
    children = React.createElement(Button, {
      ...item.children,
    });
  }
  return React.createElement(tag, {key: i.toString(), ...item}, children);
};

class Footer extends React.Component {
  static defaultProps = {
    className: 'footer1',
  };

  getLiChildren = (data) =>
    data.map((item, i) => {
      const {title, childWrapper, ...itemProps} = item;
      return (
        <Col key={i.toString()} {...itemProps} title={null} content={null}>
          <h2 {...title}>
            {typeof title.children === 'string' && title.children.match(/\.(png|jpg|gif|jpeg|webp)$/) ? (
              <img src={title.children} width="100%" alt="img"/>
            ) : (
              title.children
            )}
          </h2>
          <div {...childWrapper}>{childWrapper.children.map(getChildrenToRender)}</div>
        </Col>
      );
    });

  render() {
    const {...props} = this.props;
    const {dataSource} = props;
    delete props.dataSource;
    delete props.isMobile;
    const childrenToRender = this.getLiChildren(dataSource.block.children);
    return (
      <div {...props} {...dataSource.wrapper}>
        <OverPack {...dataSource.OverPack}>
          <QueueAnim type="bottom" key="ul" leaveReverse component={Row} {...dataSource.block}>
            {childrenToRender}
          </QueueAnim>
          <TweenOne
            animation={{y: '+=30', opacity: 0, type: 'from'}}
            key="copyright"
            {...dataSource.copyrightWrapper}
          >
            <div {...dataSource.copyrightPage}>
              <div {...dataSource.copyright}>{dataSource.copyright.children}</div>
            </div>
          </TweenOne>
        </OverPack>
      </div>
    );
  }
}

export default Footer;
