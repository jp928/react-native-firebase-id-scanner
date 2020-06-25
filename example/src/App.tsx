import * as React from 'react';
import { StyleSheet, View, ViewStyle } from 'react-native';
import IdScanner from 'react-native-firebase-id-scanner';

export default function App() {
  return (
    <View style={styles.container}>
      <IdScanner
        style={
          {
            flex: 1,
            width: '100%',
            backgroundColor: '#eee',
          } as ViewStyle
        }
        onSuccess={(e: any) => {
          const { nativeEvent } = e;
          console.log(nativeEvent.result);
        }}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    // backgroundColor: 'green',
  },
});
