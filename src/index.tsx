import { NativeModules } from 'react-native';

type FirebaseIdScannerType = {
  multiply(a: number, b: number): Promise<number>;
};

const { FirebaseIdScanner } = NativeModules;

export default FirebaseIdScanner as FirebaseIdScannerType;
