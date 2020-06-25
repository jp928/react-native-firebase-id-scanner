import React from 'react';
import { requireNativeComponent } from 'react-native';

const Scanner = requireNativeComponent('ScannerView');

const IdScanner = ({ ...props }) => {
  return <Scanner {...props} />;
};

export default IdScanner;
