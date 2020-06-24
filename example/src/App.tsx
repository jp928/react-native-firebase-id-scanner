import * as React from 'react';
import { StyleSheet, View } from 'react-native';
import { Scanner } from 'react-native-firebase-id-scanner';

export default function App() {
  return (
    <View style={styles.container}>
      <Scanner
        style={{
          flex: 1,
          width: '100%',
          backgroundColor: 'yellow',
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
