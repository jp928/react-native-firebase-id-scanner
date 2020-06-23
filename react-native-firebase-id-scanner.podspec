require "json"

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))
firebase_sdk_version = package['sdkVersions']['ios']['firebase'] || '~> 6.25.0'

Pod::Spec.new do |s|
  s.name         = "react-native-firebase-id-scanner"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = package["homepage"]
  s.license      = package["license"]
  s.authors      = package["author"]

  s.platforms    = { :ios => "9.0" }
  s.source       = { :git => "https://github.com/jp928/react-native-firebase-id-scanner.git", :tag => "#{s.version}" }

  
  s.source_files = "ios/**/*.{h,m,mm,swift}"

  # React Native dependencies
  s.dependency          'React'

  # Firebase dependencies
  s.dependency          'Firebase/CoreOnly', firebase_sdk_version
  s.dependency          'Firebase/MLVision', firebase_sdk_version
  s.dependency          'Firebase/MLVisionTextModel', firebase_sdk_version

  if defined?($RNFirebaseAsStaticFramework)
    Pod::UI.puts "#{s.name}: Using overridden static_framework value of '#{$RNFirebaseAsStaticFramework}'"
    s.static_framework = $RNFirebaseAsStaticFramework
  else
    s.static_framework = false
  end
end
