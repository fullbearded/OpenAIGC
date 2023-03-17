import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import { useIntl } from 'umi';

const Footer: React.FC = () => {
  const intl = useIntl();
  const defaultMessage = intl.formatMessage({
    id: 'app.copyright.produced',
    defaultMessage: 'OpenAIGC',
  });

  const currentYear = new Date().getFullYear();

  return (
    <DefaultFooter
      copyright={`${currentYear} ${defaultMessage}`}
      links={[
        {
          key: 'OpenAIGC',
          title: 'OpenAIGC',
          href: 'https://opaigc.com',
          blankTarget: true,
        },
        {
          key: 'github',
          title: <GithubOutlined />,
          href: 'https://github.com/fullbearded/OpenAIGC',
          blankTarget: true,
        },
        {
          key: 'OpenAIGC',
          title: 'OpenAIGC',
          href: 'https://github.com/fullbearded/OpenAIGC',
          blankTarget: true,
        },
      ]}
    />
  );
};

export default Footer;
