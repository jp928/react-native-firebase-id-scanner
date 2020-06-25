import React from 'react';
import { requireNativeComponent } from 'react-native';

const Scanner = requireNativeComponent('ScannerView');

const IdScanner: React.FC<any> = ({ ...props }) => {
  return <Scanner {...props} />;
};

export default IdScanner;
