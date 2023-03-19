import React from 'react';

export const Nav30DataSource = {
  wrapper: { className: 'header3 home-page-wrapper jzih1dpqqrg-editor_css' },
  page: { className: 'home-page' },
  logo: {
    className: 'header3-logo jzjgnya1gmn-editor_css',
    children: 'https://d1cdbsaqdizhxd.cloudfront.net/logo/Logo1.png',
  },
  Menu: {
    className: 'header3-menu',
    children: [
      {
        name: 'item1',
        className: 'header3-item',
        children: {
          href: '#',
          children: [{ children: <p>帮助中心</p>, name: 'text' }],
        },
      },
      {
        name: 'item2',
        className: 'header3-item',
        children: {
          href: '#',
          children: [{ children: <p>登录</p>, name: 'text' }],
        },
      },
      {
        name: 'item3',
        className: 'header3-item',
        children: {
          href: '#',
          children: [{ children: <p>免费试用</p>, name: 'text' }],
        },
      },
    ],
  },
  mobileMenu: { className: 'header3-mobile-menu' },
};
export const Footer10DataSource = {
  wrapper: { className: 'home-page-wrapper footer1-wrapper' },
  OverPack: { className: 'footer1', playScale: 0.2 },
  block: {
    className: 'home-page',
    gutter: 0,
    children: [
      {
        name: 'block0',
        xs: 24,
        md: 8,
        className: 'block',
        title: {
          className: 'logo jzl0qcpyjra-editor_css',
          children: 'https://d1cdbsaqdizhxd.cloudfront.net/logo/Logo3.png',
        },
        childWrapper: {
          className: 'slogan',
          children: [{ name: 'content0', children: <p>智能AI助手，创建海量的智能应用</p> }],
        },
      },
      {
        name: 'block2',
        xs: 24,
        md: 8,
        className: 'block',
        title: { children: <p>联系我们</p> },
        childWrapper: {
          children: [
            {
              name: 'image~jzl0tcm4f1d',
              className: 'wechat',
              children: 'https://d1cdbsaqdizhxd.cloudfront.net/material/IMG_0027.jpg',
            },
            {
              href: '#',
              name: 'link0',
              children: <p>进 OpenAIGC 交流群加小助手</p>,
              className: 'jzl0u1bko6-editor_css',
            },
          ],
        },
      },
      {
        name: 'block3',
        xs: 24,
        md: 8,
        className: 'block',
        title: { children: '帮助文档' },
        childWrapper: {
          children: [
            { href: '#', name: 'link0', children: '常见问题' },
            { href: '#', name: 'link1', children: '' },
          ],
        },
      },
    ],
  },
  copyrightWrapper: { className: 'copyright-wrapper' },
  copyrightPage: { className: 'home-page' },
  copyright: {
    className: 'copyright',
    children: (
      <span>
        Copyright © 2023 OpenAIGC
        <br />
      </span>
    ),
  },
};
