import React from 'react';
import {
  requireNativeComponent,
  NativeModules,
  NativeEventEmitter,
} from 'react-native';

const Scanner = requireNativeComponent('ScannerView');
export const { FirebaseIdScanner } = NativeModules;

export const FirebaseEvent = new NativeEventEmitter(FirebaseIdScanner);

const IdScanner: React.FC<any> = ({ ...props }) => {
  return <Scanner {...props} />;
};

export default IdScanner;
