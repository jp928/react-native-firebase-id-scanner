import { requireNativeComponent } from 'react-native';

// type FirebaseIdScannerType = {
//   multiply(a: number, b: number): Promise<number>;
// };

// const { FirebaseIdScanner } = NativeModules;
const Scanner = requireNativeComponent('ScannerView');

// export { FirebaseIdScanner };
export { Scanner };
