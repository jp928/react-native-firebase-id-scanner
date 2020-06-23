require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

appPackage = JSON.parse(File.read(File.join('..', 'app', 'package.json')))

coreVersionDetected = appPackage['version']
coreVersionRequired = package['peerDependencies'][appPackage['name']]
if appPackage['sdkVersions']
  firebase_sdk_version = appPackage['sdkVersions']['ios']['firebase']
else
  firebase_sdk_version = '~> 6.25.0'
end
if coreVersionDetected != coreVersionRequired
  Pod::UI.warn "NPM package '#{package['name']}' depends on '#{appPackage['name']}' v#{coreVersionRequired} but found v#{coreVersionDetected}, this might cause build issues or runtime crashes."
end

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

  if defined?($FirebaseSDKVersion)
    Pod::UI.puts "#{s.name}: Using user specified Firebase SDK version '#{$FirebaseSDKVersion}'"
    firebase_sdk_version = $FirebaseSDKVersion
  end

  # Firebase dependencies
  s.dependency          'Firebase/CoreOnly', firebase_sdk_version
  s.dependency          'Firebase/MLVision', firebase_sdk_version
  if FirebaseJSON::Config.get_value_or_default('ml_vision_ocr_model', false)
    s.dependency          'Firebase/MLVisionTextModel', firebase_sdk_version
  end

  if defined?($RNFirebaseAsStaticFramework)
    Pod::UI.puts "#{s.name}: Using overridden static_framework value of '#{$RNFirebaseAsStaticFramework}'"
    s.static_framework = $RNFirebaseAsStaticFramework
  else
    s.static_framework = false
  end
end
