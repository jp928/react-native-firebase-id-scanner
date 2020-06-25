#import <UIKit/UIKit.h>
#import <React/RCTView.h>
#import <React/RCTViewManager.h>

@interface ScannerModule : RCTViewManager

// or RCTBubblingEventBlock
@property (nonatomic, copy) RCTDirectEventBlock onSuccess;

@end
