#import <React/RCTViewManager.h>
#import "ScannerModule.h"

@interface RCT_EXTERN_MODULE(ScannerViewManager, RCTViewManager)
    RCT_EXPORT_VIEW_PROPERTY(onSuccess, RCTDirectEventBlock)
@end
