import React from 'react';

const FormDataContext = React.createContext<[any, (value: any) => void]>([
  {},
  () => {},
]);

export default FormDataContext;
