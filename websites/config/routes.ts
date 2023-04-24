export default [
  {
    path: '/user',
    access: 'canUser',
    name: 'user',
    icon: 'crown',
    menu: false,
    routes: [
      {
        name: 'user-home',
        path: '/',
        component: './User/Home',
        layout: false
      },
      {
        name: 'apps',
        path: '/users/apps',
        component: './Website/Apps',
      },
    ],
  },

  {
    path: '/admin',
    name: 'admin',
    icon: 'crown',
    access: 'canAdmin',
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
  },
  {
    path: '/apps',
    name: 'app',
    icon: 'smile',
    component: './Website/Apps',
    layout: false,
  },
  {
    path: '/apps/create',
    name: 'app',
    icon: 'smile',
    component: './Website/AppCreate',
    layout: false,
  },
  {
    path: '/app/:code',
    name: 'app',
    icon: 'smile',
    component: './Website/App',
    layout: false,
  },
  {
    path: '/generate',
    name: 'generate',
    icon: 'smile',
    component: './Generate',
    layout: false,
  },

  {
    path: '/*',
    name: '404',
    icon: 'smile',
    component: './Common/Result404',
    layout: false,
  },
];
