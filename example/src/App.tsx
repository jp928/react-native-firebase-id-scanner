import * as React from 'react';
import { StyleSheet, View, ViewStyle, TouchableOpacity, Text } from 'react-native';
import IdScanner, { FirebaseIdScanner } from 'react-native-firebase-id-scanner';

export default function App() {
  const onPress = () => {
    // console.log('hello world');
    FirebaseIdScanner.openCamera();
  };

  React.useEffect(() => {

  }, []);
  

  return (
    <View style={styles.container}>
      <TouchableOpacity onPress={onPress}>
        <Text>Try Me</Text>
      </TouchableOpacity>
      {/* <IdScanner
        style={
          {
            flex: 1,
            width: '100%',
            backgroundColor: 'blue',
          } as ViewStyle
        }
        onSuccess={(e: any) => {
          const { nativeEvent } = e;
          console.log(nativeEvent.result);
        }}
      /> */}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'green',
  },
});
