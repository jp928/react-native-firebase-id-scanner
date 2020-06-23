import * as React from 'react';
import { StyleSheet, View, Text } from 'react-native';
import { FirebaseIdScanner, Scanner } from 'react-native-firebase-id-scanner';

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();

  React.useEffect(() => {
    FirebaseIdScanner.multiply(3, 7).then(setResult);
  }, []);

  return (
    <View style={styles.container}>
      <View
        style={{
          flex: 1,
          width: '100%',
          backgroundColor: 'yellow',
          justifyContent: 'center',
          alignItems: 'center',
        }}
      >
        <Text>Result: {result}</Text>
      </View>
      <Scanner
        style={{
          flex: 1,
          width: '100%',
          backgroundColor: 'blue',
          justifyContent: 'center',
          alignItems: 'center',
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
