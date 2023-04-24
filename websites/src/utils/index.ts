// 设置 token
export const setToken = (token: string) => {
  localStorage.setItem('token', token);
};

// 获取 token
export const getToken = () => {
  return localStorage.getItem('token');
};

// 删除 token
export const removeToken = () => {
  localStorage.removeItem('token');
};
