export default [
  {
    path: '/user',
    access: 'canUser',
    name: 'user',
    icon: 'crown',
    routes: [
      {
        name: 'user-home',
        path: '/user',
        component: './User/Home',
      }
    ],
  },
  {
    path: '/manager',
    access: 'canManager',
    name: 'manager',
    icon: 'crown',
    routes: [
      {
        name: 'manager-users',
        path: '/manager/users',
        component: './Manager/Users',
        icon: 'crown',
      },
      {
        name: 'manager-chat',
        path: '/manager/chat',
        component: './Manager/Chat',
        icon: 'crown',
      }

    ],
  },
  {
    path: '/admin',
    name: 'admin',
    icon: 'crown',
    access: 'canSuperAdmin',
    menu: false,
    routes: [
      {
        path: '/admin/sub-page',
        name: 'sub-page',
        icon: 'smile',
        component: './account/Welcome',
      },
      {
        component: './Common/Result404',
        layout: false,
      },
    ],
  },
  {
    path: '/',
    name: 'home',
    icon: 'smile',
    component: './Website/Home',
    layout: false,
  },
  {
    name: 'login',
    path: '/login',
    component: './Login',
    layout: false,
    access: 'canSuperAdmin',
  },
  {
    path: '/apps',
    name: 'app',
    icon: 'smile',
    component: './Website/Apps',
    layout: false,
    menu: false,
  },
  {
    path: '/apps/create',
    name: 'app',
    icon: 'smile',
    component: './Website/AppCreate',
    layout: false,
    menu: false,
  },
  {
    path: '/app/:code',
    name: 'app',
    icon: 'smile',
    component: './Website/App',
    layout: false,
    menu: false,
  },
  {
    path: '/generate',
    name: 'generate',
    access: 'canSuperAdmin',
    icon: 'smile',
    component: './Generate',
    layout: false,
    menu: false,
  },

  {
    path: '/*',
    name: '404',
    icon: 'smile',
    menu: false,
    component: './Common/Result404',
    layout: false,
  },
];
