import React from 'react';
import { requireNativeComponent, NativeModules } from 'react-native';

const Scanner = requireNativeComponent('ScannerView');
export const { FirebaseIdScanner } = NativeModules;

const IdScanner: React.FC<any> = ({ ...props }) => {
  return <Scanner {...props} />;
};

export default IdScanner;
