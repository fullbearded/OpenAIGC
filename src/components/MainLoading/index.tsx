import React from 'react';
import './index.less';

export default class MainLoading extends React.Component {
  render() {
    return (
      <div className="main home-page">
        <div className="main-loading">
          <div className="text">
            <span className="open">Open</span>
            <span className="aigc">AIGC</span>
          </div>
        </div>
      </div>
    );
  }
}
