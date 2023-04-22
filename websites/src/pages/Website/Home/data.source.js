import React from 'react';
import BannerFinal from './assets/banner-final.jpg';

export const Banner50DataSource = {
  wrapper: { className: 'home-page-wrapper banner5' },
  page: { className: 'home-page banner5-page' },
  childWrapper: {
    className: 'banner5-title-wrapper',
    children: [
      { name: 'title', children: 'Open AIGC', className: 'banner5-title' },
      {
        name: 'explain',
        className: 'banner5-explain',
        children: '用于内容生成的 ChatGPT 应用',
      },
      {
        name: 'content',
        className: 'banner5-content',
        children:
          '使用经过您的内容和文档训练的 AI 聊天机器人，您可以获得即时的答案。这种机器人不仅可以帮助您，还可以为您的客户和团队提供帮助。通过利用现有的业务知识，您可以节省资金并改善客户的支持体验和团队的工作效率。此外，使用 AI 文案还可以提高效率。',
      },
      {
        name: 'button',
        className: 'banner5-button-wrapper',
        children: {
          href: '/apps/create',
          className: 'banner5-button',
          type: 'primary',
          children: '创建你自己的免费应用',
        },
      },
    ],
  },
  image: {
    className: 'banner5-image',
    children: BannerFinal,
  },
};
export const Feature70DataSource = {
  wrapper: { className: 'home-page-wrapper feature7-wrapper' },
  page: { className: 'home-page feature7' },
  OverPack: { playScale: 1 },
  titleWrapper: {
    className: 'feature7-title-wrapper',
    children: [
      {
        name: 'title',
        className: 'feature7-title-h1',
        children: '立即使用海量的 ChatGPT 应用',
      },
    ],
  },
  blockWrapper: {
    className: 'feature7-block-wrapper',
    gutter: 24,
    children: [
      {
        md: 6,
        xs: 24,
        name: 'block0',
        className: 'feature7-block',
        children: {
          className: 'feature7-block-group',
          children: [
            {
              name: 'image',
              className: 'feature7-block-image',
              children:
                'https://gw.alipayobjects.com/zos/basement_prod/e339fc34-b022-4cde-9607-675ca9e05231.svg',
            },
            {
              name: 'title',
              className: 'feature7-block-title',
              children: '周报生成器🔥',
            },
            {
              name: 'content',
              className: 'feature7-block-content',
              children: '简单描述工作内容，智能助手助手将帮你快速生成完整周报。',
            },
          ],
        },
      },
      {
        md: 6,
        xs: 24,
        name: 'block1',
        className: 'feature7-block',
        children: {
          className: 'feature7-block-group',
          children: [
            {
              name: 'image',
              className: 'feature7-block-image',
              children:
                'https://gw.alipayobjects.com/zos/basement_prod/e339fc34-b022-4cde-9607-675ca9e05231.svg',
            },
            {
              name: 'title',
              className: 'feature7-block-title',
              children: '周公解梦🔥',
            },
            {
              name: 'content',
              className: 'feature7-block-content',
              children: '请描述你的梦境，智能助手将为你作出简单解析。',
            },
          ],
        },
      },
      {
        md: 6,
        xs: 24,
        name: 'block2',
        className: 'feature7-block',
        children: {
          className: 'feature7-block-group',
          children: [
            {
              name: 'image',
              className: 'feature7-block-image',
              children:
                'https://gw.alipayobjects.com/zos/basement_prod/e339fc34-b022-4cde-9607-675ca9e05231.svg',
            },
            {
              name: 'title',
              className: 'feature7-block-title',
              children: '甩锅小助手🔥',
            },
            {
              name: 'content',
              className: 'feature7-block-content',
              children: '请输入想要甩锅的事件，小助手会为你生成一段甩锅的话。',
            },
          ],
        },
      },
      {
        md: 6,
        xs: 24,
        name: 'block3',
        className: 'feature7-block',
        children: {
          className: 'feature7-block-group',
          children: [
            {
              name: 'image',
              className: 'feature7-block-image',
              children:
                'https://gw.alipayobjects.com/zos/basement_prod/e339fc34-b022-4cde-9607-675ca9e05231.svg',
            },
            {
              name: 'title',
              className: 'feature7-block-title',
              children: '高情商回复小助手🔥',
            },
            {
              name: 'content',
              className: 'feature7-block-content',
              children: '输入收到的不好回答的问题，小助手为你生成一段高情商的回复。',
            },
          ],
        },
      },
      {
        md: 6,
        xs: 24,
        name: 'block4',
        className: 'feature7-block',
        children: {
          className: 'feature7-block-group',
          children: [
            {
              name: 'image',
              className: 'feature7-block-image',
              children:
                'https://gw.alipayobjects.com/zos/basement_prod/e339fc34-b022-4cde-9607-675ca9e05231.svg',
            },
            {
              name: 'title',
              className: 'feature7-block-title',
              children: '小红书风格模拟器🔥',
            },
            {
              name: 'content',
              className: 'feature7-block-content',
              children: '输入你想发布的内容，帮你生成小红书的风格',
            },
          ],
        },
      },
      {
        md: 6,
        xs: 24,
        name: 'block5',
        className: 'feature7-block',
        children: {
          className: 'feature7-block-group',
          children: [
            {
              name: 'image',
              className: 'feature7-block-image',
              children:
                'https://gw.alipayobjects.com/zos/basement_prod/e339fc34-b022-4cde-9607-675ca9e05231.svg',
            },
            {
              name: 'title',
              className: 'feature7-block-title',
              children: '餐厅点评小助手🔥',
            },
            {
              name: 'content',
              className: 'feature7-block-content',
              children: '输入关键词，小助手帮你生成餐厅点评',
            },
          ],
        },
      },
      {
        md: 6,
        xs: 24,
        name: 'block6',
        className: 'feature7-block',
        children: {
          className: 'feature7-block-group',
          children: [
            {
              name: 'image',
              className: 'feature7-block-image',
              children:
                'https://gw.alipayobjects.com/zos/basement_prod/e339fc34-b022-4cde-9607-675ca9e05231.svg',
            },
            {
              name: 'title',
              className: 'feature7-block-title',
              children: '杠精助手',
            },
            {
              name: 'content',
              className: 'feature7-block-content',
              children: '输入对方的话语，小助手帮你杠一下',
            },
          ],
        },
      },
      {
        md: 6,
        xs: 24,
        name: 'block7',
        className: 'feature7-block',
        children: {
          className: 'feature7-block-group',
          children: [
            {
              name: 'image',
              className: 'feature7-block-image',
              children:
                'https://gw.alipayobjects.com/zos/basement_prod/e339fc34-b022-4cde-9607-675ca9e05231.svg',
            },
            {
              name: 'title',
              className: 'feature7-block-title',
              children: '回复老板',
            },
            {
              name: 'content',
              className: 'feature7-block-content',
              children: '用婉转的方式回复老板，拒绝PUA从我做起',
            },
          ],
        },
      },
    ],
  },
};
export const Feature60DataSource = {
  wrapper: { className: 'home-page-wrapper feature6-wrapper' },
  OverPack: { className: 'home-page feature6', playScale: 0.3 },
  Carousel: {
    className: 'feature6-content',
    dots: false,
    wrapper: { className: 'feature6-content-wrapper' },
    titleWrapper: {
      className: 'feature6-title-wrapper',
      barWrapper: {
        className: 'feature6-title-bar-wrapper',
        children: { className: 'feature6-title-bar' },
      },
      title: { className: 'feature6-title' },
    },
    children: [
      {
        title: { className: 'feature6-title-text', children: '站点指标' },
        className: 'feature6-item',
        name: 'block0',
        children: [
          {
            md: 8,
            xs: 24,
            className: 'feature6-number-wrapper',
            name: 'child0',
            number: {
              className: 'feature6-number',
              unit: { className: 'feature6-unit', children: '个' },
              toText: true,
              children: '3770',
            },
            children: { className: 'feature6-text', children: '应用数量' },
          },
          {
            md: 8,
            xs: 24,
            className: 'feature6-number-wrapper',
            name: 'child1',
            number: {
              className: 'feature6-number',
              unit: { className: 'feature6-unit', children: '人' },
              toText: true,
              children: '452',
            },
            children: { className: 'feature6-text', children: '用户数量' },
          },
          {
            md: 8,
            xs: 24,
            className: 'feature6-number-wrapper',
            name: 'child2',
            number: {
              className: 'feature6-number',
              unit: { className: 'feature6-unit', children: '万次' },
              toText: true,
              children: '10.11',
            },
            children: { className: 'feature6-text', children: '服务调用' },
          },
        ],
      },
      {
        title: { className: 'feature6-title-text', children: '高级指标' },
        className: 'feature6-item',
        name: 'block1',
        children: [
          {
            md: 8,
            xs: 24,
            name: 'child0',
            className: 'feature6-number-wrapper',
            number: {
              className: 'feature6-number',
              unit: { className: 'feature6-unit', children: '个' },
              toText: true,
              children: '2456',
            },
            children: { className: 'feature6-text', children: '私有应用' },
          },
          {
            md: 8,
            xs: 24,
            name: 'child1',
            className: 'feature6-number-wrapper',
            number: {
              className: 'feature6-number',
              unit: { className: 'feature6-unit', children: '人' },
              toText: true,
              children: '210',
            },
            children: { className: 'feature6-text', children: '付费用户' },
          },
          {
            md: 8,
            xs: 24,
            name: 'child2',
            className: 'feature6-number-wrapper',
            number: {
              className: 'feature6-number',
              unit: { className: 'feature6-unit', children: '万次' },
              toText: true,
              children: '20.10',
            },
            children: { className: 'feature6-text', children: '服务调用' },
          },
        ],
      },
    ],
  },
};
export const Feature30DataSource = {
  wrapper: { className: 'home-page-wrapper content3-wrapper' },
  page: { className: 'content3' },
  OverPack: { playScale: 0.3 },
  titleWrapper: {
    className: 'title-wrapper',
    children: [
      {
        name: 'title',
        children: '基于 ChatGPT 驱动,在几分钟内训练和部署自定义聊天机器人！',
        className: 'title-h1',
      },
      {
        name: 'content',
        className: 'title-content',
        children:
          '您是否已经厌倦了无休止地回答相同的问题？是否缺乏灵感并感到困扰于文件编辑？您是否希望能够自动化客户支持，并使个人或团队有更多的时间专注于其他任务？通过使用 OpenAIGC，您可以轻松构建 ChatGPT 支持的机器人，通过训练它们与您的内容和文档相互匹配，为您提供即时且详尽的答案。这将使您的工作生活变得更加高效、有趣，并让您的团队有更多时间来关注其他任务。',
      },
    ],
  },
  block: {
    className: 'content3-block-wrapper',
    children: [
      {
        name: 'block0',
        className: 'content3-block',
        md: 8,
        xs: 24,
        children: {
          icon: {
            className: 'content3-icon',
            children: 'https://zos.alipayobjects.com/rmsportal/ScHBSdwpTkAHZkJ.png',
          },
          textWrapper: { className: 'content3-text' },
          title: { className: 'content3-title', children: '问答机器人' },
          content: {
            className: 'content3-content',
            children:
              '让您的文档与我们的问答机器人互动。获取有关您的产品的详细而直接的答案，包括代码示例和格式化输出。',
          },
        },
      },
      {
        name: 'block1',
        className: 'content3-block',
        md: 8,
        xs: 24,
        children: {
          icon: {
            className: 'content3-icon',
            children: 'https://zos.alipayobjects.com/rmsportal/NKBELAOuuKbofDD.png',
          },
          textWrapper: { className: 'content3-text' },
          title: { className: 'content3-title', children: '定制文案' },
          content: {
            className: 'content3-content',
            children:
              '需要帮助撰写营销文案和博客文章吗？使用 OpenAIGC，您也可以做到这一点。使用了解您产品所有信息的自定义 ChatGPT，因此它可以帮助您立即生成高质量的内容。',
          },
        },
      },
      {
        name: 'block2',
        className: 'content3-block',
        md: 8,
        xs: 24,
        children: {
          icon: {
            className: 'content3-icon',
            children: 'https://zos.alipayobjects.com/rmsportal/xMSBjgxBhKfyMWX.png',
          },
          textWrapper: { className: 'content3-text' },
          title: { className: 'content3-title', children: '内部知识机器人' },
          content: {
            className: 'content3-content',
            children:
              '员工花费太多时间只是为了寻找他们需要的东西。OpenAIGC 可以通过索引您的内部知识库和文档来帮助他们立即找到答案。',
          },
        },
      },
      {
        name: 'block3',
        className: 'content3-block',
        md: 8,
        xs: 24,
        children: {
          icon: {
            className: 'content3-icon',
            children: 'https://zos.alipayobjects.com/rmsportal/MNdlBNhmDBLuzqp.png',
          },
          textWrapper: { className: 'content3-text' },
          title: { className: 'content3-title', children: '回复支持票' },
          content: {
            className: 'content3-content',
            children:
              '厌倦了一遍又一遍地写同样的回复来支持工单？根据您的支持历史记录和文档训练您的 OpenAIGC，以便它可以自动回复新工单，从而节省您的时间和金钱！（即将推出）',
          },
        },
      },
      {
        name: 'block4',
        className: 'content3-block',
        md: 8,
        xs: 24,
        children: {
          icon: {
            className: 'content3-icon',
            children: 'https://zos.alipayobjects.com/rmsportal/UsUmoBRyLvkIQeO.png',
          },
          textWrapper: { className: 'content3-text' },
          title: { className: 'content3-title', children: '网址和站点地图' },
          content: {
            className: 'content3-content',
            children:
              '使用我们的 url 和站点地图导入器在几分钟内为网页、您的支持文档或整个网站编制索引。只需添加一个链接，剩下的交给我们。安排定期更新以保持您的内容新鲜（即将推出）。',
          },
        },
      },
      {
        name: 'block5',
        className: 'content3-block',
        md: 8,
        xs: 24,
        children: {
          icon: {
            className: 'content3-icon',
            children: 'https://zos.alipayobjects.com/rmsportal/ipwaQLBLflRfUrg.png',
          },
          textWrapper: { className: 'content3-text' },
          title: { className: 'content3-title', children: '应用程序接口' },
          content: {
            className: 'content3-content',
            children:
              '我们的 API 允许您将 AI 聊天集成到您自己的产品中。从您的网站、APP、小程序向您的用户提供答案。使用我们强大的管理 API 为任何用例构建您自己的数据摄取管道。（即将推出）',
          },
        },
      },
    ],
  },
};
