/**
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */
export default function access(initialState: { currentUser?: API.CurrentUser } | undefined) {
  const { currentUser } = initialState ?? {};
  return {
    canSuperAdmin: currentUser && currentUser.userType === 'SUPER_ADMIN',
    canManager: currentUser && (currentUser.userType === 'ADMIN' || currentUser.userType === 'SUPER_ADMIN'),
    canUser: currentUser
  };
}
